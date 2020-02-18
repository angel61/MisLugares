package com.example.mislugaresangellopezpalacios.Modelo;

/**
 * Clase utilizada para manejar los datos de un Lugar
 *
 * @author Angel Lopez Palacios
 * @version 15/02/2020
 */
public class Lugar {
    private String nombre;
    private String direccion;
    private GeoPunto posicion;
    private String foto;
    private int telefono;
    private String url;
    private String comentario;
    private long fecha;
    private float valoracion;
    private TipoLugar tipo;

    /**
     * Constructor que inicializa los valores de la clase Lugar
     *
     * @param nombre
     * @param direccion
     * @param longitud
     * @param latitud
     * @param tipo
     * @param telefono
     * @param url
     * @param comentario
     * @param valoracion
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public Lugar(String nombre, String direccion, double longitud,
                 double latitud, TipoLugar tipo, int telefono,
                 String url, String comentario, int valoracion) {
        this.tipo = tipo;
        fecha = System.currentTimeMillis();
        posicion = new GeoPunto(longitud, latitud);
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.url = url;
        this.comentario = comentario;
        this.valoracion = valoracion;
    }

    /**
     * Constructor vacio de la clase Lugar
     * Inicializa {@link #fecha}, {@link #posicion} y {@link #tipo}
     *
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public Lugar() {
        fecha = System.currentTimeMillis();
        posicion = GeoPunto.SIN_POSICION;
        tipo = TipoLugar.OTROS;
    }

    /**
     * Getter que devuelve el tipo del lugar
     *
     * @return {@link TipoLugar}
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public TipoLugar getTipo() {
        return tipo;
    }

    /**
     * Setter para poner el tipo del lugar
     *
     * @param tipo
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setTipo(TipoLugar tipo) {
        this.tipo = tipo;
    }

    /**
     * Getter que devuelve el nombre del lugar
     *
     * @return String
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter para poner el nombre del lugar
     *
     * @param nombre
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter que devuelve la dirección del lugar
     *
     * @return String
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Setter para poner la dirección del lugar
     *
     * @param direccion
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    /**
     * Getter que devuelve la posición del lugar
     *
     * @return {@link GeoPunto}
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public GeoPunto getPosicion() {
        return posicion;
    }

    /**
     * Setter para poner la posición del lugar
     *
     * @param posicion
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setPosicion(GeoPunto posicion) {
        this.posicion = posicion;
    }


    /**
     * Getter que devuelve la foto del lugar
     *
     * @return String
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public String getFoto() {
        return foto;
    }

    /**
     * Setter para poner la foto del lugar
     *
     * @param foto
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setFoto(String foto) {
        this.foto = foto;
    }


    /**
     * Getter que devuelve el telefono del lugar
     *
     * @return int
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public int getTelefono() {
        return telefono;
    }

    /**
     * Setter para poner el telefono del lugar
     *
     * @param telefono
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }


    /**
     * Getter que devuelve la url del lugar
     *
     * @return String
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter para poner una url al lugar
     *
     * @param url
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setUrl(String url) {
        this.url = url;
    }


    /**
     * Getter que devuelve el comentario sobre el lugar
     *
     * @return String
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Setter para poner un comentario al lugar
     *
     * @param comentario
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    /**
     * Getter que devuelve la fecha del lugar en milisegundos
     *
     * @return long
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public long getFecha() {
        return fecha;
    }

    /**
     * Setter para poner la fecha al lugar
     *
     * @param fecha
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setFecha(long fecha) {
        this.fecha = fecha;
    }


    /**
     * Getter que devuelve la valoración del lugar
     *
     * @return String
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public float getValoracion() {
        return valoracion;
    }

    /**
     * Setter para poner la valoración al lugar
     *
     * @param valoracion
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    /**
     * toString generado por defecto
     *
     * @return String
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    @Override
    public String toString() {
        return "Lugar{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", posicion=" + posicion +
                ", foto='" + foto + '\'' +
                ", telefono=" + telefono +
                ", url='" + url + '\'' +
                ", comentario='" + comentario + '\'' +
                ", fecha=" + fecha +
                ", valoracion=" + valoracion +
                ", tipo=" + tipo +
                '}';
    }
}
