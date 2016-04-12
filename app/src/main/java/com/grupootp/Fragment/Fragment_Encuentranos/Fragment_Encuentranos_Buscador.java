package com.grupootp.Fragment.Fragment_Encuentranos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grupootp.Adaptador;
import com.grupootp.CreateSQLite;
import com.grupootp.Modelos.Centros;
import com.grupootp.ResizeImage;
import com.grupootp.grupootp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Fragment_Encuentranos_Buscador extends Fragment {

    private static View view;
    private SearchView barraBuscar;
    private ImageView imgOcultar;
    private ListView listaBusacdor;
    private CreateSQLite db;
    private ArrayList<Centros> centrosEncontrados;
    private GoogleMap mapBuscador;

    private LatLng ubicacionCentro;
    Location miLocation, finalLocation;

    private LocationManager locManager;
    private LocationListener locListener;

    //Ubicacion Usuario
    private double latitudInicial;
    private double longitudInicial;

    //Ubicación punto
    private double latitudFinal;
    private double longitudFinal;

    LocationManager mLocationManager;

    private static final int SOLICITUD_PERMISO_FINE_LOCATION = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){

            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);

        }

        try {

            view = inflater.inflate(R.layout.encuentranos_buscador, container, false);

        }catch (InflateException e){

        }

        if (!isGPSEnabled()){
            showGPSDisabledAlertToUser();
        }

        /* Use the LocationManager class to obtain GPS locations */
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener mlocListener = new MyLocationListener();
        mlocListener.setMainActivity(this);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mlocListener);


        Log.d("Identificador", ""+getId());

        barraBuscar = (SearchView) view.findViewById(R.id.sbBuscador);
        //barraBuscar.setBackgroundColor(Color.BLUE);
        mapBuscador = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapBuscador)).getMap();
        imgOcultar = (ImageView) view.findViewById(R.id.imgOcultar);
        imgOcultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaBusacdor.getVisibility() == View.VISIBLE) {
                    listaBusacdor.setVisibility(View.GONE);
                } else {
                    listaBusacdor.setVisibility(View.VISIBLE);
                }
            }
        });

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(40.416716, -3.703786), 5);
        mapBuscador.animateCamera(cameraUpdate);

        listaBusacdor  = (ListView) view.findViewById(R.id.listaBuscador);

        ocultarTeclado();

        db = new CreateSQLite(getContext());
        db.abrirDB();
        db.insertarCentros();
        rellenarLista(db.consultarCentros());
        addMarkers(db.consultarCentros());

        barraBuscar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    barraBuscar.setBackgroundColor(getResources().getColor(R.color.blancoSemitransparente));

                } else {
                    barraBuscar.setBackgroundColor(Color.TRANSPARENT);
                }

                //Toast.makeText(getActivity(), String.valueOf(hasFocus), Toast.LENGTH_SHORT).show();


            }
        });

        barraBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();

                centrosEncontrados = db.consultarPorNombre(query);

                if (centrosEncontrados.isEmpty()) {

                    Toast.makeText(getActivity(), "No hay resultados", Toast.LENGTH_SHORT).show();

                    mapBuscador.clear();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(40.416716, -3.703786), 5);
                    mapBuscador.animateCamera(cameraUpdate);

                    listaBusacdor.setVisibility(View.GONE);


                } else {

                    if (centrosEncontrados.size() == 1) {

                        mapBuscador.clear();

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(centrosEncontrados.get(0).getLatitud()),
                                Double.parseDouble(centrosEncontrados.get(0).getLongitud())), 5);
                        mapBuscador.animateCamera(cameraUpdate);

                        listaBusacdor.setVisibility(View.VISIBLE);

                        rellenarLista(centrosEncontrados);
                        addMarkers(centrosEncontrados);

                    } else {

                        mapBuscador.clear();

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(40.416716, -3.703786), 5);
                        mapBuscador.animateCamera(cameraUpdate);

                        listaBusacdor.setVisibility(View.VISIBLE);

                        rellenarLista(centrosEncontrados);
                        addMarkers(centrosEncontrados);
                    }


                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listaBusacdor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * INICIAMOS LA ACTIVITY
                 */

                Centros cent = (Centros) listaBusacdor.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id_centro", cent.getIdCentro());

                Intent mostrarOficina = new Intent(getActivity(), Activity_Mostrar_Oficina.class);
                mostrarOficina.putExtras(bundle);
                getActivity().startActivity(mostrarOficina);
            }
        });


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mapBuscador.setMyLocationEnabled(true);
            mapBuscador.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {

                    if (!isGPSEnabled()) {
                        showGPSDisabledAlertToUser();
                    }

                    return false;
                }
            });

        }else {

            explicarUsoPermiso();
            solicitarPermiso();

        }



        return view;
    }

    public void rellenarLista(ArrayList<Centros> centros){
        listaBusacdor.setAdapter(new Adaptador(getActivity(), R.layout.elemento_oficinas, centros) {
            @Override
            public void onEntrada(Object object, View v) {

                TextView txtNomBuscador = (TextView) v.findViewById(R.id.txtNomBuscador);
                if (txtNomBuscador != null) {
                    txtNomBuscador.setText(((Centros) object).getCentro());
                }

                TextView txtDireccionBuscador = (TextView) v.findViewById(R.id.txtDireccionBuscador);
                if (txtDireccionBuscador != null) {
                    String calle = ((Centros) object).getCalle();
                    String CP = ((Centros) object).getCodigoPostal();
                    txtDireccionBuscador.setText(calle + "  " + CP);
                }

                TextView txtUbicación = (TextView) v.findViewById(R.id.txtUbicacionOficina);
                if (txtUbicación != null) {
                    String localidad = ((Centros) object).getLocalidad();
                    String provincia = ((Centros) object).getProvincia();
                    txtUbicación.setText(provincia + " (" + localidad + ")");
                }

                ImageView imgOficina = (ImageView) v.findViewById(R.id.imgOficina);

                if (!(((Centros) object).getImgCentro() == 0)) {

                    imgOficina.setImageResource(((Centros) object).getImgCentro());

                } else {

                    imgOficina.setImageResource(R.mipmap.ic_launcher);

                }

                /*
                if (!((Centros) object).getImgCentro().equals("")) {

                    ImageView imgOficina2 = (ImageView) v.findViewById(R.id.imgOficina);
                    asyncImagen imagen = new asyncImagen(imgOficina2);
                    //Log.d("Imagen", ((Centros)object).getImgCentro());
                    imagen.execute(((Centros) object).getImgCentro());

                } else {

                    imgOficina.setImageResource(R.mipmap.ic_launcher);

                }
                */
            }
        });
    }

    public void centrosMasCercanos(ArrayList<Centros> allCentros){

        /**
         * Pondremos en orden los centros de mas a menos cercanos
         */

        ArrayList<Centros> centrosOrdenados = new ArrayList<Centros>();

        Log.d("Localizacion", "Estamos dentro de CentrosMasCercanos");




/*
        for (int i = 0; i < allCentros.size(); i++){

            finalLocation = new Location("finalLocation");
            latitudFinal = Double.parseDouble(allCentros.get(i).getLatitud());
            longitudFinal = Double.parseDouble(allCentros.get(i).getLongitud());
            finalLocation.setLatitude(latitudFinal);
            finalLocation.setLongitude(longitudFinal);



        }
        */
    }

    /**
     * METODOS PARA LA UBICACION
     */

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(R.string.activarGPS)
                .setCancelable(false).setPositiveButton((R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGpsSettingIntent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGpsSettingIntent);

            }
        });

        alertDialogBuilder.setNegativeButton(R.string.denegar,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private boolean isGPSEnabled(){

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void mostrarPosicion(Location loc) {

        if (loc != null){

            miLocation = new Location("myLocation");
            latitudInicial = loc.getLatitude();
            longitudInicial = loc.getLongitude();
            miLocation.setLatitude(latitudInicial);
            miLocation.setLongitude(longitudFinal);

            Log.d("Localizacion", miLocation.getLatitude() + " " + miLocation.getLongitude());
            Log.d("Localizacion", latitudInicial + " " + longitudInicial);

            //Log.d("DistanciaUbicacion", latitudInicial + " " + longitudInicial);
            //Log.d("DistanciaCentro", latitudFinal + " " + longitudFinal);

        }else {

            Log.d("Localizacion", "loc == null");

        }

    }

    private void comenzarLocalizacion() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            //Obtenemos una referencia al LocationManager
            locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            //Obtenemos la última posición conocida
            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //Mostramos la última posición conocida
            mostrarPosicion(loc);


            //Nos registramos para recibir actualizaciones de la posición
            locListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    //mostrarPosicion(location);
                    if (location != null){

                        miLocation = new Location("myLocation");
                        latitudInicial = location.getLatitude();
                        longitudInicial = location.getLongitude();
                        miLocation.setLatitude(latitudInicial);
                        miLocation.setLongitude(longitudFinal);

                        Log.d("Localizacion", miLocation.getLatitude() + " " + miLocation.getLongitude());
                        Log.d("Localizacion", latitudInicial + " " + longitudInicial);

                        //Log.d("DistanciaUbicacion", latitudInicial + " " + longitudInicial);
                        //Log.d("DistanciaCentro", latitudFinal + " " + longitudFinal);

                    }else {
                        Log.d("Localizacion", "loc == null");
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);

        }else {

            explicarUsoPermiso();
            solicitarPermiso();

        }
    }

    private void calculaDistanciaACentro(double stLat, double stLng, double endLat, double endLng){

        float [] dist = new float[3];

        Location.distanceBetween(stLat, stLng, endLat, endLng, dist);

        for (int i = 0; i <= dist.length-1; i++){

            //Toast.makeText(this, "Distancia" +i+ "-->" +Double.toString((double)dist[i]),Toast.LENGTH_SHORT).show();
            Log.d("Distancia", "Distancia1" + i + " -->" + Double.toString((double) dist[i]));

        }

    }

    private void calculadistancia(Location miUbicacion, Location suUbicaion){
        if (miUbicacion != null && suUbicaion != null){

            float distance = miUbicacion.distanceTo(suUbicaion);
            Log.d("Distancia", "Distancia2 -->" + distance);

        }
    }

    private void explicarUsoPermiso(){

        //Comprobamos si el usuario ha marcado "No volver a preguntar"
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(getActivity(), "Explicamos el porque necesitamos los permisos", Toast.LENGTH_SHORT).show();

        }

    }

    private void solicitarPermiso(){

        //Pedimos el permiso con un cuadro de dialogo
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SOLICITUD_PERMISO_FINE_LOCATION);

    }

    public void addMarkers(ArrayList<Centros> centros){

        for (int i = 0; i < centros.size(); i++){

            String titulo, snippet;
            double latit, longi;

            titulo = centros.get(i).getCentro();
            snippet = centros.get(i).getCalle();

            latit = Double.parseDouble(centros.get(i).getLatitud());
            longi = Double.parseDouble(centros.get(i).getLongitud());

            LatLng ubicacionCent = new LatLng(latit, longi);

            MarkerOptions cent = new MarkerOptions().position(ubicacionCent)
                    .title(titulo)
                    .snippet(snippet);


            mapBuscador.addMarker(cent);

        }

    }

    public void ocultarTeclado(){

        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

    }

    public void setLocation(Location loc) {
        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    Log.d("UBICACION", "Mi direcci—n es: \n"
                            + address.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Class My Location Listener */
    public class MyLocationListener implements LocationListener {
        Fragment_Encuentranos_Buscador buscadorActivity;

        public Fragment_Encuentranos_Buscador getMainActivity() {
            return buscadorActivity;
        }

        public void setMainActivity(Fragment_Encuentranos_Buscador buscadorActivity) {
            this.buscadorActivity = buscadorActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este mŽtodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la detecci—n de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            String Text = "Mi ubicaci—n actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            Log.d("Ubicación", Text);
            this.buscadorActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este mŽtodo se ejecuta cuando el GPS es desactivado

        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este mŽtodo se ejecuta cuando el GPS es activado
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Este mŽtodo se ejecuta cada vez que se detecta un cambio en el
            // status del proveedor de localizaci—n (GPS)
            // Los diferentes Status son:
            // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
            // TEMPORARILY_UNAVAILABLE -> Temp˜ralmente no disponible pero se
            // espera que este disponible en breve
            // AVAILABLE -> Disponible
        }

    }/* End of Class MyLocationListener */

}
