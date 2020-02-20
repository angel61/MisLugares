package com.example.mislugaresangellopezpalacios.Modelo;

import java.util.Objects;

/**
 * Clase utilizada para manejar datos de localizacion como latitud y longitud
 *
 * @author Angel Lopez Palacios
 * @version 1
 */
public class GeoPunto {

    private double longitud, latitud;

    /**
     *Geopunto con ambas variables "latitud" y "longitud" en 0.0
     */
    static public GeoPunto SIN_POSICION = new GeoPunto(0.0, 0.0);

    /**
     * Constructor de la clase que inicializa la latitud y la longitud del GeoPunto
     *
     * @param longitud
     * @param latitud
     * @author Angel Lopez Palacios
     * @version 1
     */
    public GeoPunto(double longitud, double latitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    /**
     * Metodo toString que devuelve el GeoPunto
     *
     * @return String
     * @author Angel Lopez Palacios
     * @version 1
     */
    public String toString() {
        return new String("longitud:" + longitud + ", latitud:" + latitud);
    }

    /**
     * Método que calcula la distancia entre dos {@link GeoPunto}
     *
     * @param punto
     * @return double
     * @author Angel Lopez Palacios
     * @version 1
     */
    public double distancia(GeoPunto punto) {
        final double RADIO_TIERRA = 6371000; // en metros
        double dLat = Math.toRadians(latitud - punto.latitud);
        double dLon = Math.toRadians(longitud - punto.longitud);
        double lat1 = Math.toRadians(punto.latitud);
        double lat2 = Math.toRadians(latitud);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                        Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * RADIO_TIERRA;
    }

    /**
     * Getter que devuelve la longitud del GeoPunto
     *
     * @return double
     * @author Angel Lopez Palacios
     * @version 1
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * Setter para poner la longitud del GeoPunto
     *
     * @param longitud
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /**
     * Getter que devuelve la latitud del GeoPunto
     *
     * @return double
     * @author Angel Lopez Palacios
     * @version 1
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * Setter para poner la latitud del GeoPunto
     *
     * @param latitud
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /**
     * Getter que devuelve un GeoPunto Sin posición
     *
     * @return SIN_POSICION
     * @author Angel Lopez Palacios
     * @version 1
     */
    public static GeoPunto getSinPosicion() {
        return SIN_POSICION;
    }

    /**
     * Setter para poner un GeoPunto vacio
     *
     * @param sinPosicion
     * @author Angel Lopez Palacios
     * @version 1
     */
    public static void setSinPosicion(GeoPunto sinPosicion) {
        SIN_POSICION = sinPosicion;
    }

    /**
     * Metodo equals para determinar si dos {@link GeoPunto} son iguales
     *
     * @param o
     * @return boolean
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPunto geoPunto = (GeoPunto) o;
        return Double.compare(geoPunto.longitud, longitud) == 0 &&
                Double.compare(geoPunto.latitud, latitud) == 0;
    }

    /**
     * Metodo que hashCode que determina si sirve para comparar objetos de una forma más rápida en estructuras Hash
     *
     * @return int
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public int hashCode() {
        return Objects.hash(longitud, latitud);
    }
}
