package com.example.mislugaresangellopezpalacios.Adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mislugaresangellopezpalacios.Modelo.GeoPunto;
import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.Presentacion.Aplicacion;
import com.example.mislugaresangellopezpalacios.R;
import com.example.mislugaresangellopezpalacios.casos_uso.CasosUsoLugar;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


/**
 * Clase para adaptar la información de los lugares para poder mostrarla en el InfoWindow
 *
 * @author Angel Lopez Palacios
 * @version 1
 * @see GoogleMap.InfoWindowAdapter
 * @see com.example.mislugaresangellopezpalacios.Presentacion.MapaActivity
 */
public class AdaptadorInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    /**
     * Constructor de la clase
     * @param context contexto del mapa
     */
    public AdaptadorInfoWindow(Activity context){
        this.context = context;
    }

    /**
     * Método que modfica la apariencia y el texto del InfoWindow
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(Marker marker) {
        View view; //= context.getLayoutInflater().inflate(R.layout.customwindow, null);
        view =LayoutInflater.from(context).inflate(R.layout.customwindow, null);


        TextView nombre = view.findViewById(R.id.nombrew);
        TextView direccion = view.findViewById(R.id.direccionw);
        RatingBar valoracion = view.findViewById(R.id.valoracionw);
        TextView distancia = view.findViewById(R.id.distanciaw);

        AdaptadorLugaresBD adaptador=((Aplicacion)context.getApplication()).adaptador;
        int pos=adaptador.posicionId((int) marker.getTag());
        Lugar lugar=adaptador.lugarPosicion(pos);

        nombre.setText(lugar.getNombre());
        direccion.setText(lugar.getDireccion());

        valoracion.setRating(lugar.getValoracion());

        GeoPunto punto = ((Aplicacion) context.getApplicationContext())
                .posicionActual;
        if (
                lugar.getPosicion().equals(GeoPunto.SIN_POSICION)) {
            distancia.setText("... Km");
        } else {
            int d = (int) punto.distancia(lugar.getPosicion());
            if (d < 2000) distancia.setText(d + " m");
            else distancia.setText(d / 1000 + " Km");
        }

        return view;
    }

    /**
     * Método que modfica el texto del InfoWindow
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }
}
