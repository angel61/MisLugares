package com.example.mislugaresangellopezpalacios.Adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mislugaresangellopezpalacios.R;
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

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);

        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());

        return view;
    }

    /**
     * Método que modfica el texto del InfoWindow
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        View view; //= context.getLayoutInflater().inflate(R.layout.customwindow, null);
        view =LayoutInflater.from(context).inflate(R.layout.customwindow, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);

        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());

        return null;
    }
}
