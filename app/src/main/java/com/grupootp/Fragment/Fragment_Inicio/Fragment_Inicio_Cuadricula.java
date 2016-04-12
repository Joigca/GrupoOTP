package com.grupootp.Fragment.Fragment_Inicio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.grupootp.Adaptador;
import com.grupootp.Modelos.Noticias;
import com.grupootp.ResizeImage;
import com.grupootp.grupootp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Fragment_Inicio_Cuadricula extends Fragment {

    private GridView gridView;
    private ImageSwitcher imageSwitcher;
    View view;
    private Boolean variable;
    private AlertDialog alertDialog;

    private int[] gallery ={ R.drawable.slide_hospital, R.drawable.slide_master, R.drawable.slide_simulador};

    private int position;
    private static final Integer DURATION = 4500;
    private Timer timer = null;
    private Display pantalla;
    private Point punto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inicio_cuadricula, container, false);


        variable = false;
        ocultarTeclado();
        pantalla = getActivity().getWindowManager().getDefaultDisplay();
        punto = new Point();
        pantalla.getSize(punto);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.informacion)
                .setMessage(R.string.carga);
        alertDialog = builder.create();

        alertDialog.show();

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setNumColumns(2);
        start();

        if (isOnline()){

            String url = "http://blog.grupotp.org/feed/";
            new asyncXML(this).execute(url);

        }else {

            Toast.makeText(getActivity(), R.string.conexionI + "\n" +
                    R.string.conexionII , Toast.LENGTH_SHORT).show();

        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Noticias not = (Noticias) gridView.getAdapter().getItem(position);
                String url = not.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

                Log.d("categorias", not.getCategorias().toString());

                startActivity(intent);

            }
        });

        /**
         * ImageSwitcher
         */

        imageSwitcher = (ImageSwitcher) view.findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getActivity());
            }
        });

        /**
         * Listener del ImageSwitcher
         */

        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.grupotp.org/curso-seguridad-vial.php"));
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://hospitaloptimista.org/"));
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://master-organizaciones-saludables.grupotp.org/"));
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    /**
     * starts or restarts the slider
     */

    public boolean isOnline(){

        //Comprobamos que el dispositivo tiene conexion a Internet

        //Conexion
        ConnectivityManager connection = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connection.getActiveNetworkInfo();

        //ComprobaciÃ³n de la conectividad
        if (networkInfo != null && networkInfo.isConnected()){

            return true;

        }else{

            return false;
        }

    }

    public void start() {
        if (timer != null) {
            timer.cancel();
        }

        position = 0;
        startSlider();

    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void startSlider() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                // avoid exception:
                // "Only the original thread that created a view hierarchy can touch its views"
                if (variable) {


                }else {

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (timer == null) {
                                stop();
                            }

                            Drawable myIcon = getResources().getDrawable(gallery[position]);
                            ResizeImage image = new ResizeImage();

                            //imageSwitcher.setImageDrawable(image.resize(myIcon, 2 * punto.y + punto.x / 2, 2 * punto.x));
                            //imageSwitcher.setImageDrawable(myIcon);

                            int medi = punto.x - punto.x / 8;
                            Picasso.with(getActivity()).load(gallery[position]).resize(punto.y, punto.x - punto.x / 8).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                    Drawable img = new BitmapDrawable(getResources(), bitmap);
                                    imageSwitcher.setImageDrawable(img);
                                    alertDialog.dismiss();

                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });

                            position++;

                            if (position == gallery.length) {
                                position = 0;
                            }

                        }
                    });
                }
            }
        }, 0, DURATION);
    }

    // Stops the slider when the Activity is going into the background
    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }

        //Log.d("variable en pause", variable.toString());

    }

    @Override
    public void onResume() {
        super.onResume();
        variable = false;
        if (timer != null) {
            startSlider();
        }

        //Log.d("variable en resume", variable.toString());

    }

    @Override
    public void onDestroy() {
        //Toast.makeText(getContext(), "Totalemte fuera", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        variable = true;
        if (timer == null){
            stop();
        }

        //Log.d("variable en destroy", variable.toString());
    }

    public void ventanaCarga(){

    }

    public void rellenarLista(ArrayList<Noticias> array){
        //Log.d("DATOS", "Dentro de rellenar Lista");
        gridView.setAdapter(new Adaptador(getActivity(), R.layout.elemento_blog_cuadricula, array) {
            @Override
            public void onEntrada(Object object, View v) {

                if (object != null) {

                    TextView txtTituloBlog = (TextView) v.findViewById(R.id.txtTituloBlogCuadricula);
                    //TextView txtDescripcionBlog = (TextView) v.findViewById(R.id.txtDescripcionBlog);
                    //ImageView imgBlog = (ImageView) v.findViewById(R.id.imgBlog);
                    //layout = (RelativeLayout) v.findViewById(R.id.contenedor);

                    if (txtTituloBlog != null)
                        txtTituloBlog.setText(((Noticias) object).getTitulo());
                    //Log.d("DATOS", "Entrada --> " + ((Noticias) object).getTitulo());
                    //if (txtDescripcionBlog != null)
                    //    txtDescripcionBlog.setText(((Noticias) object).getDesc());

                    Boolean pred = ((Noticias) object).getImgPred();
                    //Log.d("PRED", pred.toString());

                    if (((Noticias) object).getImgPred() == true) {
                        Resources res = getResources();
                        Drawable draw = res.getDrawable(R.drawable.imgsalud);
                        ResizeImage image = new ResizeImage();
                        //imgBlog.setImageDrawable(draw);

                        Display display = getActivity().getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.contenedorCuadricula);
                        Log.d("TAMANYO", "x-- "+size.x+", y-- "+size.y);

                        relativeLayout.setBackground(image.resize(draw, size.x, 120));
                        //layout.setBackground(image.resize(draw, size.x, 120));

                    }
                    if (!((Noticias) object).getImgPred()) {

                        RelativeLayout relLayout = (RelativeLayout) v.findViewById(R.id.contenedorCuadricula);
                        asyncImagen imagen = new asyncImagen(relLayout);
                        imagen.execute(((Noticias) object).getUrlImagen());
/*
                        String selectImage = "";
                        String optionImage = "";
                        final String NOTICIAS = "Últimas noticias";
                        final String MOS = "Máster Organizaciones Saludables";
                        final String BLOG = "Blog";

                        for (int i = 0; i < ((Noticias)object).getCategorias().size(); i++){

                            optionImage = ((Noticias) object).getCategorias().get(i);

                            if (selectImage.equals(MOS)){
                                Log.d("Informacion", MOS);
                                break;

                            }else if (selectImage.equals(NOTICIAS) && optionImage.equals(MOS)){
                                Log.d("Informacion", MOS);
                                selectImage = MOS;

                            }else if (selectImage.equals(BLOG) && optionImage.equals(MOS)){
                                Log.d("Informacion", MOS);
                                selectImage = MOS;

                            }else if (selectImage.equals(BLOG) && optionImage.equals(NOTICIAS)){
                                Log.d("Informacion", NOTICIAS);
                                selectImage = NOTICIAS;

                            }else if (selectImage.equals("")){
                                Log.d("Informacion", "PrimeraVEZ");
                                selectImage = optionImage;
                                Log.d("informacion",selectImage);

                            }else {

                                Log.d("Informacion", "Predeterminado");
                                selectImage = "Predeterminado";

                            }

                        }


                        switch (selectImage){

                            case MOS:

                                ResizeImage imageMos = new ResizeImage();
                                Bitmap iconMos = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.slide_master);
                                Drawable dMos = new BitmapDrawable(iconMos);
                                relLayout.setBackground(imageMos.resize(dMos, 150, 150));
                                break;

                            case NOTICIAS:

                                ResizeImage imageNot = new ResizeImage();
                                Bitmap iconNot = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.noticias);
                                Drawable dNot = new BitmapDrawable(iconNot);
                                relLayout.setBackground(imageNot.resize(dNot, 150, 150));
                                break;

                            case BLOG:

                                ResizeImage imageBlo = new ResizeImage();
                                Bitmap iconBlo = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.blog);
                                Drawable dBlo = new BitmapDrawable(iconBlo);
                                relLayout.setBackground(imageBlo.resize(dBlo, 150, 150));
                                break;
                        }
*/
                    }

                }
            }
        });
    }

    /**
     * AsyncTask para parsear XML
     */

    class asyncXML extends AsyncTask<String, Void, ArrayList<Noticias>>{

        String ns = null;
        Fragment_Inicio_Cuadricula fragment_inicio_cuadricula;

        private final String RSS  = "rss";
        private final String CHANNEL = "channel";
        private final String ITEM = "item";
        private final String TITULO = "title";
        private final String LINK = "link";
        private final String DESCRIPCION = "description";
        private final String CATEGORY = "category";

        public asyncXML(Fragment_Inicio_Cuadricula fragment_inicio_cuadricula){this.fragment_inicio_cuadricula = fragment_inicio_cuadricula;}

        @Override
        protected void onPostExecute(ArrayList<Noticias> noticiases) {
            fragment_inicio_cuadricula.rellenarLista(noticiases);
        }

        private ArrayList<Noticias> readrss(XmlPullParser parser) throws XmlPullParserException, IOException {
            ArrayList<Noticias> noticias = new ArrayList();  //On guardarem les dades a tornar

            parser.require(XmlPullParser.START_TAG, ns, RSS); //INICI DOCUMENT
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                if (name.equals(CHANNEL)) {
                    noticias = readChannel(parser);

                }

            }

            return noticias;
        }

        private ArrayList<Noticias> readChannel(XmlPullParser parser) throws IOException, XmlPullParserException {

            ArrayList<Noticias> items = new ArrayList<Noticias>();
            parser.require(XmlPullParser.START_TAG, ns, CHANNEL);

            int cont = 0;

            while (parser.next() != XmlPullParser.END_TAG) {

                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();

                if (name.equals(ITEM)) {

                    Noticias item = readItem(parser);

                    if (item != null && cont <= 5)

                        items.add(item);
                        cont = cont +1 ;

                } else {

                    skip(parser);

                }
            }

            return items;
        }

        private Noticias readItem(XmlPullParser parser) throws IOException, XmlPullParserException {

            Noticias i = new Noticias();
            ArrayList<String> categories = new ArrayList<String>();
            parser.require(XmlPullParser.START_TAG, ns, ITEM);

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals(TITULO)) {

                    String titulo = readText(parser, TITULO);
                    i.setTitulo(titulo);

                } else if (name.equals(LINK)) {

                    String link = readText(parser, LINK);
                    i.setLink(link);

                }  else if (name.equals(DESCRIPCION)) {

                    String descripcion = readText(parser, DESCRIPCION);

                    if (i.obtenerImagen(descripcion) != null) {

                        i.setUrlImagen(i.obtenerImagen(descripcion));

                    }else{

                        i.setImgPred(true);
                        Log.d("Metodo", "" + i.getImgPred());
                    }

                } else if (name.equals(CATEGORY)){

                    String category = readText(parser, CATEGORY);
                    categories.add(category);
                    i.setCategorias(categories);

                } else {
                        skip(parser);

                }

            }
            return i;
        }

        private String readText(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, tag);
            String value = "";
            if (parser.next() == XmlPullParser.TEXT) {
                value = parser.getText();
                parser.nextTag();
            }
            parser.require(XmlPullParser.END_TAG, ns, tag);
            return value;
        }

        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

        @Override
        protected ArrayList<Noticias> doInBackground(String... params) {

            InputStream is = null;

            try {

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                //Comienza la CONEXION
                is = connection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                //Instanciem un objecte XmlPullParser, i comencem a analitzar lâ€™xml
                XmlPullParser parser = factory.newPullParser();
                //Indiquem que no volem processar els namespaces
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                //Indiquem que lâ€™entrada de lâ€™XML la rebrem des dâ€™un InputStream (in)
                parser.setInput(is, null);
                //Iniciem el procÃ©s de parseig
                parser.nextTag();
                //Tornem la informaciÃ³ obtinguda del parseig. El mÃ¨tode readFeed() Ã©s
                // qui obtindrÃ  la informaciÃ³ dels tags que ens interesse, i la guardarÃ  en una
                //Estructura de dades adient.
                return readrss(parser);

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } catch (XmlPullParserException e) {
                e.printStackTrace();

            }finally {
                if (is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }

    /**
     * AsyncTask para carga de la imagen del Blog
     */

    class asyncImagen extends AsyncTask<String, Void, Bitmap>{

        RelativeLayout layout;

        public asyncImagen(RelativeLayout layout){

            this.layout = layout;

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

            if (getView() != null){

                ResizeImage image = new ResizeImage();
                //imgBlog.setImageDrawable(draw);

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Drawable d = new BitmapDrawable(getResources(), bitmap);
                layout.setBackground(image.resize(d, size.x, 150));

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
