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
 * Clase para adaptar la información de los lugares para poder mostrarla en el RecycleView
 *
 * @author Angel Lopez Palacios
 * @version 1
 * @see androidx.recyclerview.widget.RecyclerView.Adapter
 */
public class AdaptadorLugares extends RecyclerView.Adapter<AdaptadorLugares.ViewHolder> {
    protected View.OnClickListener onClickListener;


    protected RepositorioLugares lugares;         // Lista de lugares a mostrar

    /**
     * Constructor de la clase
     *
     * @param lugares
     * @author Angel Lopez Palacios
     * @version 1
     */
    public AdaptadorLugares(RepositorioLugares lugares) {
        this.lugares = lugares;
    }


    /**
     * Instancia de elementos a mostrar en el RecycleView
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, direccion;
        public ImageView foto;
        public RatingBar valoracion;
        public TextView distancia;


        /**
         * Método que permite visualizar los elementos seleccionados en la vista RecycleView
         *
         * @param itemView
         * @author Angel Lopez Palacios
         * @version 1
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
         * Método que permite seleccionar el tipo de icono que va a tener nuestro lugar,
         * ponerle una valoración al lugar, obtener la distancia a la que se encuentra de tu posición
         *
         * @param lugar
         * @author Angel Lopez Palacios
         * @version 1
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
     * Crea un ViewHolder e inicializa los campos siguiendo el diseño de elementolista.xml
     *
     * @param parent
     * @param viewType
     * @return ViewHolder
     * @author Angel Lopez Palacios
     * @version 1
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
     * Método que actualiza los ViewHolder a partir de la posicion del elemento
     *
     * @param holder
     * @param posicion
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Lugar lugar = lugares.elemento(posicion);
        holder.personaliza(lugar);
    }

    /**
     * Devuelve el número total de elementos en el conjunto de datos
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public int getItemCount() {
        return lugares.tamanno();
    }

    /**
     * Añade un OncliItemClickListener a la clase
     *
     * @param onClickListener
     * @author Angel Lopez Palacios
     * @version 1
     * @deprecated 1
     */
    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


}
