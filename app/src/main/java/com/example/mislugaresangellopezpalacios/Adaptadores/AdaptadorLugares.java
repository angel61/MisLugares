package com.example.mislugaresangellopezpalacios.Adaptadores;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mislugaresangellopezpalacios.Modelo.GeoPunto;
import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.Modelo.RepositorioLugares;
import com.example.mislugaresangellopezpalacios.Presentacion.Aplicacion;
import com.example.mislugaresangellopezpalacios.R;


/**
 * Esta clase pretende rellenar el ReciclweView
 *
 * @author Angel Lopez Palacios
 * @version 15/02/2020
 */

public class AdaptadorLugares extends RecyclerView.Adapter<AdaptadorLugares.ViewHolder> {
    protected View.OnClickListener onClickListener;



    protected RepositorioLugares lugares;         // Lista de lugares a mostrar

    public AdaptadorLugares(RepositorioLugares lugares) {
        this.lugares = lugares;
    }


    /**
     * Crea un ViewHolder, con los tipos de elementos a modificar
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, direccion;
        public ImageView foto;
        public RatingBar valoracion;
        public TextView distancia;

        /**
         * Constructor del ViewHolder
         *
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            direccion = itemView.findViewById(R.id.direccion);
            foto = itemView.findViewById(R.id.foto);
            valoracion = itemView.findViewById(R.id.valoracion);
            distancia = itemView.findViewById(R.id.distancia);
        }

        /**
         * Este metodo personaliza un ViewHolder a partir de un lugar
         *
         * @param lugar
         */
        public void personaliza(Lugar lugar) {
            nombre.setText(lugar.getNombre());
            direccion.setText(lugar.getDireccion());
            int id = R.drawable.otros;
            switch (lugar.getTipo()) {
                case RESTAURANTE:
                    id = R.drawable.restaurante;
                    break;
                case BAR:
                    id = R.drawable.bar;
                    break;
                case COPAS:
                    id = R.drawable.copas;
                    break;
                case ESPECTACULO:
                    id = R.drawable.espectaculos;
                    break;
                case HOTEL:
                    id = R.drawable.hotel;
                    break;
                case COMPRAS:
                    id = R.drawable.compras;
                    break;
                case EDUCACION:
                    id = R.drawable.educacion;
                    break;
                case DEPORTE:
                    id = R.drawable.deporte;
                    break;
                case NATURALEZA:
                    id = R.drawable.naturaleza;
                    break;
                case GASOLINERA:
                    id = R.drawable.gasolinera;
                    break;
            }
            foto.setImageResource(id);
            foto.setScaleType(ImageView.ScaleType.FIT_END);
            valoracion.setRating(lugar.getValoracion());

            GeoPunto pos = ((Aplicacion) itemView.getContext().getApplicationContext())
                    .posicionActual;
            if (
                    lugar.getPosicion().equals(GeoPunto.SIN_POSICION)) {
                distancia.setText("... Km");
            } else {
                int d = (int) pos.distancia(lugar.getPosicion());
                if (d < 2000) distancia.setText(d + " m");
                else distancia.setText(d / 1000 + " Km");
            }
        }
    }

    /**
     * Crea el ViewHolder con la vista de un elemento sin personalizar
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// Inflamos la vista desde el xml
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elementolista, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    /**
     * Usa como base el ViewHolder y lo personalizamos
     *
     * @param holder
     * @param posicion
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Lugar lugar = lugares.elemento(posicion);
        holder.personaliza(lugar);
    }

    /**
     * Indicamos el número de elementos de la lista
     *
     * @return numero de elementos
     */
    @Override
    public int getItemCount() {
        return lugares.tamanno();
    }


    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }







}
