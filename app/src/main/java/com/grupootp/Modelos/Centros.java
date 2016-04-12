package com.grupootp.Modelos;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Centros {

    private int idCentro;
    private String centro;
    private String calle;
    private String codigoPostal;
    private String provincia;
    private String localidad;
    private String telefono;
    private String correo;
    private String latitud;
    private String longitud;
    private int imgCentro;

    public Centros(){}

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getImgCentro() {
        return imgCentro;
    }

    public void setImgCentro(int imgCentro) {
        this.imgCentro = imgCentro;
    }
}
