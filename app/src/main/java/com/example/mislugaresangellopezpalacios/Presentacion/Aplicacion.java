package com.example.mislugaresangellopezpalacios.Presentacion;

import android.app.Application;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugares;
import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.GeoPunto;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.RepositorioLugares;
import com.google.android.gms.maps.GoogleMap;

/**
 * Clase para mantener el estado global de la aplicación.
 * Proporcionamos nuestra propia implementación creando una subclase (Application) y especificando el nombre completo.
 * La clase de aplicación se instancia antes que cualquier otra clase cuando se crea el proceso para su paquete.
 *
 * @author Angel Lopez Palacios
 * @version 1
 * @see android.app.Application
 */
public class Aplicacion extends Application {

    /**
     * La coleccion de Lugares almacenados en la BDD.
     */
    public LugaresBD lugares;

    /**
     * El adaptador para enlazar los datos en el RecyclerView.
     */
    public AdaptadorLugaresBD adaptador;
    public GoogleMap mapa;

    /**
     * La posicionActual del lugar, clase GeoPunto que almacena la longitud y la latitud para el posicionamiento.
     */
    public GeoPunto posicionActual = new GeoPunto(0.0, 0.0);

    /**
     * Inicializa los componentes de la actividad.
     * Recoge los los lugares de la BD y el adaptador
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onCreate() {
        super.onCreate();

        lugares = new LugaresBD(this);
        adaptador = new AdaptadorLugaresBD(lugares, lugares.extraeCursor());
    }

    /**
     * Obtiene los lugares del repositorio.
     * Esto se reemplaza cuando se introducen las BDD en la aplicación
     *
     * @return la lista de lugares
     * @author Angel Lopez Palacios
     * @version 1
     */
    public RepositorioLugares getLugares() {
        return lugares;
    }

    /**
     * Devuelve el adaptador de lugares que controla el RecyclerView.
     *
     * @return el adaptador de la aplicación con el que vinculamos los componentes
     * con el repositorio de datos
     * @author Angel Lopez Palacios
     * @version 1
     */
    public AdaptadorLugares getAdaptador() {
        return adaptador;
    }

}