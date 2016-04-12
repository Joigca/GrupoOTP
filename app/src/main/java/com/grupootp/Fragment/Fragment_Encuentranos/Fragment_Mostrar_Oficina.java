package com.grupootp.Fragment.Fragment_Encuentranos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grupootp.grupootp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Fragment_Mostrar_Oficina extends Fragment {

    private ImageView imgMostrarOficina;
    private TextView txtNomMostrarOficina, txtDireccionMostrarOficina, txtUbicacionMostrarOficina, txtTelefonoMostrarMapa, txtCorreoMostrarMapa;
    private GoogleMap mapOficina;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mostrar_oficina, container, false);

        //Asignamos los componentes
        imgMostrarOficina = (ImageView) view.findViewById(R.id.imgMostrarOficina);
        txtNomMostrarOficina = (TextView) view.findViewById(R.id.txtNomMostrarOficina);
        txtDireccionMostrarOficina = (TextView) view.findViewById(R.id.txtDireccionMostrarOficina);
        txtUbicacionMostrarOficina = (TextView) view.findViewById(R.id.txtUbicacionMostrarOficina);
        txtTelefonoMostrarMapa = (TextView) view.findViewById(R.id.txtTelefonoMostrarOficina);
        txtCorreoMostrarMapa = (TextView) view.findViewById(R.id.txtCorreoMostrarOficina);

        mapOficina = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapOficina)).getMap();

        txtNomMostrarOficina.setText(getArguments().getString("nomCentro"));
        txtDireccionMostrarOficina.setText((getArguments().getString("calle")) + (getArguments().getString("codigoPostal")));
        txtUbicacionMostrarOficina.setText((getArguments().getString("provincia")) + " (" + (getArguments().getString("localidad")) + ")");
        txtTelefonoMostrarMapa.setText(getArguments().getString("telefono"));
        txtCorreoMostrarMapa.setText(getArguments().getString("correo"));


        //Listeners TextView
        txtTelefonoMostrarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + getArguments().getString("telefono")));
                //startActivity(callIntent);

                try {

                    startActivity(Intent.createChooser(callIntent, "Llamar desde..."));

                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(getActivity(), "No se puede realizar la llamada", Toast.LENGTH_SHORT).show();

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

                    Toast.makeText(getActivity(), "No hay Aplicaciones de correo disponibles", Toast.LENGTH_SHORT).show();

                }
            }
        });

        final String latitud = getArguments().getString("latitud");
        final String longitud = getArguments().getString("longitud");
        final LatLng ubicacionCentro = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        final String direccion = obtenerCalle(latitud, longitud);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicacionCentro, 15);
        mapOficina.animateCamera(cameraUpdate);

        MarkerOptions centro = new MarkerOptions().position(ubicacionCentro).title(getArguments().getString("nomCentro")).snippet(direccion);
        mapOficina.addMarker(centro);

        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mapOficina.setMyLocationEnabled(true);


        } else {

            // Show rationale and request permission.

        }

        mapOficina.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                String dirPersonalizada = direccion.trim() + "," + getArguments().getString("provincia").trim();

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + dirPersonalizada));

                try {
                    startActivity(Intent.createChooser(i, "Buscar la Oficina desde..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "No hay aplicaciones GPS", Toast.LENGTH_SHORT).show();
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

        return view;
    }


    public String obtenerCalle(String lat, String lng) {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
}