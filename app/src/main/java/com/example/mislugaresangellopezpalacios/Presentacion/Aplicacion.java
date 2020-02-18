package com.example.mislugaresangellopezpalacios.Presentacion;

import android.app.Application;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugares;
import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.GeoPunto;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.RepositorioLugares;

public class Aplicacion extends Application {
    public LugaresBD lugares;
    public AdaptadorLugaresBD adaptador;
    public GeoPunto posicionActual = new GeoPunto(0.0, 0.0);
    //  public RepositorioLugares lugares = new LugaresLista();
    //public AdaptadorLugares adaptador = new AdaptadorLugares(lugares);
    @Override public void onCreate() {
        super.onCreate();

        lugares = new LugaresBD(this);
        adaptador= new AdaptadorLugaresBD(lugares, lugares.extraeCursor());
    }

    /*public Lugar getLugares() {
        return lugares;
    }*/
    public RepositorioLugares getLugares() {
        return lugares;
    }
    public AdaptadorLugares getAdaptador() {return adaptador;}

}