package com.grupootp.Modelos;

import java.util.ArrayList;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Noticias {

    private String urlImagen;
    private String titulo;
    private String descripcion;
    private String desc;
    private String link;
    private ArrayList<String> categorias;
    private boolean imgPred;

    public Noticias() {
        /**
         * CONSTRUCTOR VACIO
         */
    }

    //Constructor COMPLETO
    public Noticias(String urlImagen, String titulo, String descripcion) {
        this.urlImagen = urlImagen;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setContenido(String contenido){
        setDescripcion(contenido);

        String descrip = obtenerDescripcion(contenido);
        setDesc(descrip);

    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean getImgPred() {
        return imgPred;
    }

    public void setImgPred(boolean imgPred) {
        this.imgPred = imgPred;
    }

    public ArrayList<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<String> categorias) {
        this.categorias = categorias;
    }

    public String obtenerImagen(String contenido){

        String res = "";
        String ini = " src=";
        String fin = " class=";

        int inicio = contenido.indexOf(ini);
        int finalizacion = contenido.indexOf(fin);

        if (inicio < 0 || finalizacion < 0){
            return null;
        }else {

            res =contenido.substring(inicio + ini.length()+1, finalizacion-1);

            return res;

        }
    }

    public String obtenerDescripcion(String contenido){
        String res = "";

        String ini = "</div>\n<p>";
        String fin = "</p>\n<p>";

        int inicio = contenido.indexOf(ini);
        int finalizacion = contenido.indexOf(fin);

        res = contenido.substring(inicio + ini.length(), finalizacion);

        inicio = 0;
        finalizacion = 0;

        return res;
    }

}