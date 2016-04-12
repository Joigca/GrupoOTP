package com.grupootp.Modelos;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Slide {

    private int idSlide;
    private String url;
    private String texto;
    private int idImg;

    public Slide() {
        //CONSTRUCTOR VACIO
    }

    public Slide(int idSlide, String url, String texto, int idImg) {

        this.idSlide = idSlide;
        this.url = url;
        this.texto = texto;
        this.idImg = idImg;

    }

    public int getIdSlide() {
        return idSlide;
    }

    public void setIdSlide(int idSlide) {
        this.idSlide = idSlide;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdImg() {
        return idImg;
    }

    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
