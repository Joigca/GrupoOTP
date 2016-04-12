package com.grupootp.Fragment.Fragment_Encuentranos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.grupootp.Adaptador;
import com.grupootp.CreateSQLite;
import com.grupootp.Modelos.Centros;
import com.grupootp.ResizeImage;
import com.grupootp.grupootp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Fragment_Encuentranos_Oficinas extends Fragment {

    private CreateSQLite db;
    private ArrayList<Centros> centrosOTP = new ArrayList<Centros>();
    private ListView listViewOficinas;
    private View view;
    private Adaptador adaptador;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_encuentranos_oficinas, container, false);

        Log.d("Identificador", "" + getId());

        ocultarTeclado();

        listViewOficinas = (ListView) view.findViewById(R.id.listViewOficinas);

        /**
         * LLamamos a la BASE DE DATOS
         */

        db = new CreateSQLite(getContext());

        db.abrirDB();
        db.insertarCentros();
        centrosOTP = db.consultarCentros();

        rellenarLista(centrosOTP);

        listViewOficinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * INICIAMOS LA ACTIVITY
                 */

                Centros cent = (Centros) listViewOficinas.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id_centro", cent.getIdCentro());
                /**
                bundle.putString("nomCentro", cent.getCentro());
                bundle.putString("calle", cent.getCalle());
                bundle.putString("codigoPostal", cent.getCodigoPostal());
                bundle.putString("provincia", cent.getProvincia());
                bundle.putString("localidad", cent.getLocalidad());
                bundle.putString("telefono", cent.getTelefono());
                bundle.putString("correo", cent.getCorreo());
                bundle.putString("latitud", cent.getLatitud());
                bundle.putString("longitud", cent.getLongitud());
                bundle.putString("imgCentro", cent.getImgCentro());
*/
                Intent mostrarOficina = new Intent(getActivity(), Activity_Mostrar_Oficina.class);
                mostrarOficina.putExtras(bundle);
                getActivity().startActivity(mostrarOficina);

            }
        });

        return view;
    }

    public void rellenarLista(ArrayList<Centros> centros){
        listViewOficinas.setAdapter(new Adaptador(getActivity(), R.layout.elemento_oficinas, centros) {
            @Override
            public void onEntrada(Object object, View v) {

                TextView txtNomOficina = (TextView) v.findViewById(R.id.txtNomBuscador);
                if (txtNomOficina != null) {
                    txtNomOficina.setText(((Centros) object).getCentro());
                }

                TextView txtDireccionOficina = (TextView) v.findViewById(R.id.txtDireccionBuscador);
                if (txtDireccionOficina != null) {
                    String calle = ((Centros) object).getCalle();
                    String CP = ((Centros) object).getCodigoPostal();
                    txtDireccionOficina.setText(calle + "  " + CP);
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

            }
        });
    }

    /**
     * Asynctask descarga imagen
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

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Drawable d = new BitmapDrawable(getResources(), bitmap);
                imgView.setImageDrawable(image.resize(d, 120, 120));
                //Picasso.with(getApplicationContext()).load().resize(punto.y, punto.x - punto.x / 8).into(imgView);

            }
        }
    }

    public void ocultarTeclado(){

        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

    }

}
