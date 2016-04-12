package com.grupootp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.grupootp.Modelos.Centros;
import com.grupootp.Modelos.Noticias;
import com.grupootp.Modelos.Slide;
import com.grupootp.grupootp.R;

import java.util.ArrayList;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class CreateSQLite {

    //Parametros BD
    private static final String NOMBREDB = "dbgrupotp.db";
    private static final String TABLACENTRO = "centro";
    private static final String TABLASLIDE = "slide";
    private static final int VERSION = 8;

    //CREATE TABLE CENTRO
    private static final String CREARTABLACENTRO =
            "CREATE TABLE IF NOT EXISTS " + TABLACENTRO +
            " (idCentro integer primary key not null," +
            " centro varchar(55) not null," +
            " calle varchar(55) not null," +
            " codigoPostal varchar(5) not null," +
            " provincia varchar(55) not null," +
            " localidad varchar(55) not null," +
            " telefono varchar(9) not null," +
            " correo varchar(55) not null," +
            " latitud varchar(55) not null," +
            " longitud varchar(55) not null," +
            " imgCentro varchar(150)"+
            " );";


    //CREATE TABLE SLIDE
    private static final String CREARTABLASLIDE =
            "CREATE TABLE IF NOT EXISTS " + TABLASLIDE +
            " (idSlide integer primary key not null," +
            " url varchar(55) not null," +
            " texto varchar(55) not null," +
            " idImg integer not null"+
            " );";

    //DROP TABLE
    private static final String DROPTABLECENTRO = "DROP TABLE IF EXISTS " + TABLACENTRO + ";";
    private static final String DROPTABLESLIDE = "DROP TABLE IF EXISTS " + TABLASLIDE + ";";

    //CONTEXTO
    private final Context context;

    //OBJETO CLASE EMBEDIDA
    private DBClaseHelper dbc;

    //OBJETO SQLITE
    private SQLiteDatabase db;

    public CreateSQLite(Context context){

        this.context = context;
        dbc = new DBClaseHelper(context, NOMBREDB, null, VERSION);
    }

    public void abrirDB(){

        try {

            db = dbc.getReadableDatabase();

        }catch (SQLiteException sql){

            db = dbc.getReadableDatabase();

        }
    }

    //INSERTAR CAMPOS DE DATOS

    /**
     * Insertar Centros
     */
    public void insertarDatosCentro(int idCentro, String centro, String calle, String codigoPostal,
                              String provincia, String localidad, String telefono, String correo, String latitud,
                              String longitud, int imgCentro){

        Log.d("MARCADOR", "Esta en insertarDatos");
        ContentValues cv = new ContentValues();

        cv.put("idCentro", idCentro);
        cv.put("centro", centro);
        cv.put("calle", calle);
        cv.put("codigoPostal", codigoPostal);
        cv.put("provincia", provincia);
        cv.put("localidad", localidad);
        cv.put("telefono", telefono);
        cv.put("correo", correo);
        cv.put("latitud", latitud);
        cv.put("longitud", longitud);
        cv.put("imgCentro", imgCentro);

        db.insert(TABLACENTRO, null, cv);

    }

    //INSERCION DE DATOS
    public void insertarCentros(){

        insertarDatosCentro(0, "Grupo OTP Amposta", "C/Larache, 8", "43870",
                "Amposta", "Tarragona", "902120264", "amposta@grupotp.org", "40.711896", "0.583375", 0);

        insertarDatosCentro(1, "Grupo OTP Alcoy", "C/ Oliver, 7", "03802",
                "Alcoy", "Alicante", "965542199", "alcoy@grupotp.org", "38.6947795", "-0.4814145999999937", 0);

        insertarDatosCentro(2, "Grupo OTP Banyeres de Mariola", "C/ Villena, 18", "03450",
                "Banyeres de Mariola", "Alicante", "965103117", "banyeres@grupotp.org", "38.7183509", "-0.6602877999999919", 0);

        insertarDatosCentro(3, "Grupo OTP Barcelona", "Gran Via Carles III 64 - 66", "08028",
                "Barcelona", "Barcelona", "934523593", "barcelona@grupotp.org", "41.384198", "2.12785", 0);

        insertarDatosCentro(4, "Grupo OTP IBI", "C/Virgen de los Desamparados, 8", "03440",
                "Ibi", "Alicante", "965103117", "ibi@grupotp.org", "38.624514", "-0.568459", 0);

        insertarDatosCentro(5, "Grupo OTP Massanassa", "C/Font de la Cabilda 6 bajo", "46470",
                "Massanassa", "Valencia", "961278040", "massanassa@grupotp.org", "39.410873", "-0.395435", R.drawable.otp_massanassa);

        insertarDatosCentro(6, "Grupo OTP Lleida", "Avd. Prat de la Riba, 19 Entlo 1", "25006",
                "Lleida", "Lleida", "973235525", "lleida@grupotp.org", "41.620031", "0.621226", 0);

        insertarDatosCentro(7, "Grupo OTP Sort", "Avda. Diputación, 18", "25560",
                "Lleida", "Lleida", "973621096", "lleida@grupotp.org", "42.40982", "1.1287399999999934", 0);

        insertarDatosCentro(8, "Grupo OTP Torrejón de Ardoz", "Avda. de las Fronteras, 4", "28850",
                "Torrejón de Ardoz", "Madrid", "916784840", "torrejon@grupotp.org", "40.459776", "-3.4883519", 0);



    }

    public ArrayList<Centros> consultarCentros(){
        Log.d("MARCADOR", "Esta en la consulta");
        ArrayList<Centros> centrosOTP = new ArrayList<Centros>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLACENTRO + ";", null, null);

        //RECOGEMOS EL CURSOR
        if (cursor != null && cursor.moveToFirst()){
            do {

                Centros cent = new Centros();
                cent.setIdCentro(cursor.getInt(0));
                cent.setCentro(cursor.getString(1));
                cent.setCalle(cursor.getString(2));
                cent.setCodigoPostal(cursor.getString(3));
                cent.setProvincia(cursor.getString(4));
                cent.setLocalidad(cursor.getString(5));
                cent.setTelefono(cursor.getString(6));
                cent.setCorreo(cursor.getString(7));
                cent.setLatitud(cursor.getString(8));
                cent.setLongitud(cursor.getString(9));
                cent.setImgCentro(cursor.getInt(10));
                Log.d("CURSOR", "Dentro del cursor" + cursor.getString(1));
                centrosOTP.add(cent);

            }while (cursor.moveToNext());
        }else {

            Log.d("CURSOR", "No hay datos en el cursor");

        }

        return centrosOTP;

    }

    public Centros consultarCentro(int identificador){
        Log.d("MARCADOR", "Esta en la consulta");
        //ArrayList<Centros> centrosOTP = new ArrayList<Centros>();
        Centros cent = new Centros();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLACENTRO + " WHERE idCentro = "+ identificador +";", null, null);

        //RECOGEMOS EL CURSOR
        if (cursor != null && cursor.moveToFirst()){
            do {

                //Centros cent = new Centros();
                cent.setIdCentro(cursor.getInt(0));
                cent.setCentro(cursor.getString(1));
                cent.setCalle(cursor.getString(2));
                cent.setCodigoPostal(cursor.getString(3));
                cent.setProvincia(cursor.getString(4));
                cent.setLocalidad(cursor.getString(5));
                cent.setTelefono(cursor.getString(6));
                cent.setCorreo(cursor.getString(7));
                cent.setLatitud(cursor.getString(8));
                cent.setLongitud(cursor.getString(9));
                cent.setImgCentro(cursor.getInt(10));
                Log.d("CURSOR", "Dentro del cursor" + cursor.getString(1));
                //centrosOTP.add(cent);

            }while (cursor.moveToNext());
        }else {

            Log.d("CURSOR", "No hay datos en el cursor");

        }return cent;

    }

    public static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public ArrayList<Centros> consultarPorNombre(String cadena){
        Log.d("MARCADOR", "Esta en la consulta");
        //ArrayList<Centros> centrosOTP = new ArrayList<Centros>();
        ArrayList<Centros> centros = new ArrayList<Centros>();

        Cursor cursorNombre = db.rawQuery("SELECT * FROM " + TABLACENTRO + " WHERE centro LIKE '%" + cadena + "%';", null, null);

        //RECOGEMOS EL CURSOR QUE BUSCARA POR NOMBRE DEL CENTRO
        if (cursorNombre != null && cursorNombre.moveToFirst()){
            do {

                Centros cent = new Centros();
                cent.setIdCentro(cursorNombre.getInt(0));
                cent.setCentro(cursorNombre.getString(1));
                cent.setCalle(cursorNombre.getString(2));
                cent.setCodigoPostal(cursorNombre.getString(3));
                cent.setProvincia(cursorNombre.getString(4));
                cent.setLocalidad(cursorNombre.getString(5));
                cent.setTelefono(cursorNombre.getString(6));
                cent.setCorreo(cursorNombre.getString(7));
                cent.setLatitud(cursorNombre.getString(8));
                cent.setLongitud(cursorNombre.getString(9));
                cent.setImgCentro(cursorNombre.getInt(10));
                centros.add(cent);

            }while (cursorNombre.moveToNext());

        }else {

            Log.d("CURSOR", "No hay datos en el cursor");

        }

        boolean validarCodPostal = false;

        try {
            Integer.parseInt(cadena);
            validarCodPostal = true;
        } catch (NumberFormatException nfe){
            validarCodPostal = false;
        }

        if (validarCodPostal){

            Cursor cursorCodigoPostal = db.rawQuery("SELECT * FROM " + TABLACENTRO + " WHERE codigoPostal = "+ cadena +";", null, null);

            //RECOGEMOS EL CURSOR QUE BUSCARA POR NOMBRE DEL CENTRO
            if (cursorCodigoPostal != null && cursorCodigoPostal.moveToFirst()){
                do {

                    Centros cent = new Centros();
                    cent.setIdCentro(cursorCodigoPostal.getInt(0));
                    cent.setCentro(cursorCodigoPostal.getString(1));
                    cent.setCalle(cursorCodigoPostal.getString(2));
                    cent.setCodigoPostal(cursorCodigoPostal.getString(3));
                    cent.setProvincia(cursorCodigoPostal.getString(4));
                    cent.setLocalidad(cursorCodigoPostal.getString(5));
                    cent.setTelefono(cursorCodigoPostal.getString(6));
                    cent.setCorreo(cursorCodigoPostal.getString(7));
                    cent.setLatitud(cursorCodigoPostal.getString(8));
                    cent.setLongitud(cursorCodigoPostal.getString(9));
                    cent.setImgCentro(cursorCodigoPostal.getInt(10));
                    centros.add(cent);

                }while (cursorCodigoPostal.moveToNext());

            }else {

                Log.d("CURSOR", "No hay datos en el cursor");

            }
        }

        Cursor cursorProvincia = db.rawQuery("SELECT * FROM " + TABLACENTRO + " WHERE provincia LIKE '%" + cadena + "%';", null, null);

        //RECOGEMOS EL CURSOR QUE BUSCARA POR NOMBRE DEL CENTRO
        if (cursorProvincia != null && cursorProvincia.moveToFirst()){
            do {

                Centros cent = new Centros();
                cent.setIdCentro(cursorProvincia.getInt(0));
                cent.setCentro(cursorProvincia.getString(1));
                cent.setCalle(cursorProvincia.getString(2));
                cent.setCodigoPostal(cursorProvincia.getString(3));
                cent.setProvincia(cursorProvincia.getString(4));
                cent.setLocalidad(cursorProvincia.getString(5));
                cent.setTelefono(cursorProvincia.getString(6));
                cent.setCorreo(cursorProvincia.getString(7));
                cent.setLatitud(cursorProvincia.getString(8));
                cent.setLongitud(cursorProvincia.getString(9));
                cent.setImgCentro(cursorProvincia.getInt(10));

                boolean esta = false;

                for (int i = 0; i<centros.size(); i++){

                    if (centros.get(i).getIdCentro() == cent.getIdCentro()){
                        esta = true;
                        continue;
                    }
                }

                if (!esta){
                    centros.add(cent);
                }

            }while (cursorProvincia.moveToNext());

        }else {

            Log.d("CURSOR", "No hay datos en el cursor");

        }

        Cursor cursorLocalidad = db.rawQuery("SELECT * FROM " + TABLACENTRO + " WHERE localidad LIKE '%" + cadena + "%';", null, null);

        //RECOGEMOS EL CURSOR QUE BUSCARA POR NOMBRE DEL CENTRO
        if (cursorLocalidad != null && cursorLocalidad.moveToFirst()){
            do {

                Centros cent = new Centros();
                cent.setIdCentro(cursorLocalidad.getInt(0));
                cent.setCentro(cursorLocalidad.getString(1));
                cent.setCalle(cursorLocalidad.getString(2));
                cent.setCodigoPostal(cursorLocalidad.getString(3));
                cent.setProvincia(cursorLocalidad.getString(4));
                cent.setLocalidad(cursorLocalidad.getString(5));
                cent.setTelefono(cursorLocalidad.getString(6));
                cent.setCorreo(cursorLocalidad.getString(7));
                cent.setLatitud(cursorLocalidad.getString(8));
                cent.setLongitud(cursorLocalidad.getString(9));
                cent.setImgCentro(cursorLocalidad.getInt(10));

                boolean esta = false;

                for (int i = 0; i<centros.size(); i++){

                    if (centros.get(i).getIdCentro() == cent.getIdCentro()){
                        esta = true;
                        continue;
                    }
                }

                if (!esta){
                    centros.add(cent);
                }


            }while (cursorProvincia.moveToNext());

        }else {

            Log.d("CURSOR", "No hay datos en el cursor");

        }

        return centros;
    }

    /**
     * SUBCLASE EMBEDIDA
     */

    public static class DBClaseHelper extends SQLiteOpenHelper{


        public DBClaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){

            super(context, name, factory, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREARTABLACENTRO);
            db.execSQL(CREARTABLASLIDE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(DROPTABLECENTRO);
            db.execSQL(DROPTABLESLIDE);
            onCreate(db);

        }
    }

}
