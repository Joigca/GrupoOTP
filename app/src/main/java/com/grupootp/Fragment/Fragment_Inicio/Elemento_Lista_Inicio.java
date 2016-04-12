package com.grupootp.Fragment.Fragment_Inicio;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Elemento_Lista_Inicio {

    private String imgFeed;
    private String tituloFeed;
    private String descripcionFeed;
    private String linkFeed;

    //Contructor VACIO
    public Elemento_Lista_Inicio() {
    }

    public Elemento_Lista_Inicio(String imgFeed, String tituloFeed, String descripcionFeed, String linkFeed) {
        this.imgFeed = imgFeed;
        this.tituloFeed = tituloFeed;
        this.descripcionFeed = descripcionFeed;
        this.linkFeed = linkFeed;
    }

    public String getImgFeed() {
        return imgFeed;
    }

    public void setImgFeed(String imgFeed) {
        this.imgFeed = imgFeed;
    }

    public String getTituloFeed() {
        return tituloFeed;
    }

    public void setTituloFeed(String tituloFeed) {
        this.tituloFeed = tituloFeed;
    }

    public String getDescripcionFeed() {
        return descripcionFeed;
    }

    public void setDescripcionFeed(String descripcionFeed) {
        this.descripcionFeed = descripcionFeed;
    }

    public String getLinkFeed() {
        return linkFeed;
    }

    public void setLinkFeed(String linkFeed) {
        this.linkFeed = linkFeed;
    }
}
