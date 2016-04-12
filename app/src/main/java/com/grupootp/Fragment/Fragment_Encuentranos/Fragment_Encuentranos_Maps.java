package com.grupootp.Fragment.Fragment_Encuentranos;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grupootp.CreateSQLite;
import com.grupootp.Modelos.Centros;
import com.grupootp.grupootp.R;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Fragment_Encuentranos_Maps extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap googleMap;
    private CreateSQLite db;
    private View view;
    private FloatingActionButton fab;
    private ArrayList<Centros> centrosOTP = new ArrayList<Centros>();
    private String identificador;
    private LocationManager locManager;
    private LocationListener locListener;

    double latitude;
    double longitude;
    LocationManager mLocationManager;

    private static final int SOLICITUD_PERMISO_FINE_LOCATION = 1;
    private static final int SOLICITUD_PERMISO_GET_PROVIDERS = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null){

            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);

        }

        try {

            view = inflater.inflate(R.layout.fragment_encuentranos_maps, container, false);

        }catch (InflateException e){

        }

        Log.d("Identificador", "" + getId());

        ocultarTeclado();

        /**
         * Base de datos
         */

        db = new CreateSQLite(getContext());
        db.abrirDB();
        db.insertarCentros();
        centrosOTP = db.consultarCentros();

        googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaMaps)).getMap();


        /**
         * Comprobamos el permiso para ACCION
         */

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
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

        fab = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab.setVisibility(View.INVISIBLE);

        //Listener Marcador
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                fab.setVisibility(View.VISIBLE);
                identificador = marker.getId().substring(1, marker.getId().length());

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putInt("id_centro", Integer.parseInt(identificador));
                        Intent mostrarOficina = new Intent(getActivity(), Activity_Mostrar_Oficina.class);
                        mostrarOficina.putExtras(bundle);
                        getActivity().startActivity(mostrarOficina);

                    }
                });

                return false;
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (fab != null) {

                    fab.setVisibility(View.INVISIBLE);

                }
            }
        });


        colocarMarcadores(centrosOTP);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(40.416716, -3.703786), 5);
        googleMap.animateCamera(cameraUpdate);

        if (!isGPSEnabled()) {
            showGPSDisabledAlertToUser();
        }

        //comenzarLocalizacion();

        return view;
    }

    private double calculaDistancia() {

        Double distancia = 0.0;

        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> listaProviders = locManager.getAllProviders();

        LocationProvider provider = locManager.getProvider(listaProviders.get(0));
        int precision = provider.getAccuracy();
        boolean obtieneAltitud = provider.supportsAltitude();
        int consumoRecursos = provider.getPowerRequirement();

        /**
         * Mejor proveedor de localizacion
         */

        Criteria req = new Criteria();
        req.setAccuracy(Criteria.ACCURACY_FINE);
        req.setAltitudeRequired(true);

        //Mejor proveedor por criterio
        String mejorProviderCrit = locManager.getBestProvider(req, false);

        //Lista de proveedores por criterio
        List<String> listaProvidersCrit = locManager.getProviders(req, false);

        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //mostrarPosicion(location);
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


        return distancia;
    }


    private boolean isGPSEnabled(){

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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

    public void colocarMarcadores(ArrayList<Centros> centros){

        for (int i = 0; i < centros.size(); i++){

            LatLng ubicacionCent = new LatLng(Double.parseDouble(centros.get(i).getLatitud()), Double.parseDouble(centros.get(i).getLongitud()));

            MarkerOptions cent = new MarkerOptions().position(ubicacionCent)
                    .title(centros.get(i).getCentro())
                    .snippet(centros.get(i).getCalle());


            googleMap.addMarker(cent);
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 20);
        googleMap.animateCamera(cameraUpdate);

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

    /**
     * Trabajamos con los permisos
     */

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

    public void ocultarTeclado(){

        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

    }
}