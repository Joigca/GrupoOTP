package com.grupootp.Fragment.Fragment_Encuentranos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grupootp.CreateSQLite;
import com.grupootp.Modelos.Centros;
import com.grupootp.ResizeImage;
import com.grupootp.grupootp.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Activity_Mostrar_Oficina extends FragmentActivity {

    private ImageView imgMostrarOficina1;
    private TextView txtNomMostrarOficina, txtDireccionMostrarOficina, txtUbicacionMostrarOficina, txtTelefonoMostrarMapa, txtCorreoMostrarMapa;
    private GoogleMap mapOficina;
    Centros centro;
    CreateSQLite bd = new CreateSQLite(this);
    Bundle bundle;

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


    private static final int SOLICITUD_PERMISO_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mostrar_oficina);

        bundle = getIntent().getExtras();
        bd.abrirDB();
        mapOficina = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapOficina)).getMap();

        //Asignamos los componentes
        imgMostrarOficina1 = (ImageView) findViewById(R.id.imgMostrarOficina);
        txtNomMostrarOficina = (TextView) findViewById(R.id.txtNomMostrarOficina);
        txtDireccionMostrarOficina = (TextView) findViewById(R.id.txtDireccionMostrarOficina);
        txtUbicacionMostrarOficina = (TextView) findViewById(R.id.txtUbicacionMostrarOficina);
        txtTelefonoMostrarMapa = (TextView) findViewById(R.id.txtTelefonoMostrarOficina);
        txtCorreoMostrarMapa = (TextView) findViewById(R.id.txtCorreoMostrarOficina);

        centro = bd.consultarCentro(bundle.getInt("id_centro"));

        txtNomMostrarOficina.setText(centro.getCentro());
        txtDireccionMostrarOficina.setText((centro.getCalle()) + " " + (centro.getCodigoPostal()));
        txtUbicacionMostrarOficina.setText((centro.getProvincia()) + " (" + (centro.getLocalidad()) + ")");
        txtTelefonoMostrarMapa.setText(centro.getTelefono());
        txtCorreoMostrarMapa.setText(centro.getCorreo());

        //Listeners TextView
        txtTelefonoMostrarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + centro.getTelefono()));
                //startActivity(callIntent);
                try {
                    startActivity(Intent.createChooser(callIntent, "Llamar desde..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "No se puede realizar la llamada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCorreoMostrarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                        txtCorreoMostrarMapa.getText().toString(), null));
                try {
                    startActivity(Intent.createChooser(i, "Enviar correo desde..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "No hay Aplicaciones de correo disponibles", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * Mostrando Imagen de la oficina
         */

        //Toast.makeText(getApplicationContext(), centro.getImgCentro()+"----"+centro.getIdCentro(),Toast.LENGTH_SHORT).show();


        if (!(centro.getImgCentro() == 0)) {

            imgMostrarOficina1.setImageResource((centro.getImgCentro()));

        } else {

            imgMostrarOficina1.setImageResource(R.mipmap.ic_launcher);
        }

/*
        if (!centro.getImgCentro().equals("")) {

            ImageView imgMostrarOficina = (ImageView) findViewById(R.id.imgMostrarOficina);
            asyncImagen imagen = new asyncImagen(imgMostrarOficina);
            Log.d("Imagen", centro.getImgCentro());
            imagen.execute(centro.getImgCentro());

        }else {

            imgMostrarOficina1.setImageResource(R.mipmap.ic_launcher);

        }
*/
        //final String latitud = bundle.getString("latitud");
        //final String longitud = bundle.getString("longitud");

        final String latitud = centro.getLatitud();
        final String longitud = centro.getLongitud();
        ubicacionCentro = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        final String direccion = obtenerCalle(latitud, longitud);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicacionCentro, 15);
        mapOficina.animateCamera(cameraUpdate);

        MarkerOptions puntoCentro = new MarkerOptions().position(ubicacionCentro).title(centro.getCentro()).snippet(direccion);
        mapOficina.addMarker(puntoCentro);

        /**
         * Comprobamos el permiso para ACCION
         */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mapOficina.setMyLocationEnabled(true);
            mapOficina.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
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

        mapOficina.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                String dirPersonalizada = direccion.trim() + "," + centro.getProvincia().trim();

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + dirPersonalizada));

                try {
                    startActivity(Intent.createChooser(i, "Buscar oficina desde..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "No hay aplicaciones GPS", Toast.LENGTH_SHORT).show();
                }

            }
        });

        txtDireccionMostrarOficina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicacionCentro, 15);
                mapOficina.animateCamera(cameraUpdate);

            }
        });

        comenzarLocalizacion();

    }

    public String obtenerCalle(String lat, String lng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> list = null;
        String dir = "";
        try {
            list = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            if (!list.isEmpty()) {
                Address address = list.get(0);
                dir = address.getAddressLine(0);
                //Toast.makeText(getActivity(), "Estas en --> "+address.getAddressLine(0), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dir;

    }

    /**
     * Trabajamos con los permisos
     */

    private void explicarUsoPermiso(){

        //Comprobamos si el usuario ha marcado "No volver a preguntar"
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(this, "Explicamos el porque necesitamos los permisos", Toast.LENGTH_SHORT).show();

        }

    }

    private void solicitarPermiso(){

        //Pedimos el permiso con un cuadro de dialogo
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SOLICITUD_PERMISO_FINE_LOCATION);

    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void mostrarPosicion(Location loc) {

        miLocation = new Location("myLocation");
        miLocation.setLatitude(39.411021);
        miLocation.setLongitude(-0.395312);

        Log.d("Localizacion", miLocation.toString());
        finalLocation = new Location("finalLocation");
        latitudFinal = Double.parseDouble(centro.getLatitud());
        longitudFinal = Double.parseDouble(centro.getLongitud());
        finalLocation.setLatitude(latitudFinal);
        finalLocation.setLongitude(longitudFinal);

        if (loc != null){

            miLocation = new Location("myLocation");
            latitudInicial = loc.getLatitude();
            longitudInicial = loc.getLongitude();
            //miLocation.setLatitude(latitudInicial);
            //miLocation.setLongitude(longitudFinal);
            //miLocation.setLatitude(39.411021);b
            //miLocation.setLongitude(-0.395312);

            Log.d("Localizacion", miLocation.toString());
            finalLocation = new Location("finalLocation");
            latitudFinal = Double.parseDouble(centro.getLatitud());
            longitudFinal = Double.parseDouble(centro.getLongitud());
            finalLocation.setLatitude(latitudFinal);
            finalLocation.setLongitude(longitudFinal);

            Log.d("DistanciaUbicacion", latitudInicial + " " + longitudInicial);
            Log.d("DistanciaCentro", latitudFinal + " " + longitudFinal);

        }else {
            Log.d("Distancia", "Loc es NUll");
        }

    }

    private void comenzarLocalizacion() {

        //Obtenemos una referencia al LocationManager
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                //Log.d("ubicacion", loc.getLatitude()+"");

                //Mostramos la última posición conocida
                mostrarPosicion(loc);
                calculaDistanciaACentro(latitudInicial, longitudInicial, latitudFinal, longitudFinal);
                calculadistancia(miLocation, finalLocation);

                //Nos registramos para recibir actualizaciones de la posición
                locListener = new LocationListener() {
                    public void onLocationChanged(Location location) {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            mostrarPosicion(location);
                            calculaDistanciaACentro(latitudInicial, longitudInicial, latitudFinal, longitudFinal);
                            calculadistancia(miLocation, finalLocation);
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

    /**
     * Asynctask Obtener imagenes de los centros de Grupo OTP
     */

    class asyncImagen extends AsyncTask<String, Void, Bitmap> {

        ImageView imgView;

        public asyncImagen(ImageView imgView){

            this.imgView = imgView;

        }

        @Override
        protected Bitmap doInBackground(String... params) {


            String urlDisplay = params[0];
            Bitmap background = null;

            try {

                InputStream is = null;
                is = new URL(urlDisplay).openStream();
                background = BitmapFactory.decodeStream(is);

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Error", "No ha descargado la Imagen");
            }

            return background;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (imgView != null){

                ResizeImage image = new ResizeImage();
                //imgBlog.setImageDrawable(draw);

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Drawable d = new BitmapDrawable(getResources(), bitmap);
                imgView.setImageDrawable(image.resize(d, 120, 120));
                //Picasso.with(getApplicationContext()).load().resize(punto.y, punto.x - punto.x / 8).into(imgView);

            }
        }
    }

}

