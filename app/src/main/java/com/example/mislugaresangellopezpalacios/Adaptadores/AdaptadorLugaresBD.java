package com.example.mislugaresangellopezpalacios.Adaptadores;

import android.database.Cursor;

import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.RepositorioLugares;

/**
 * Clase para adaptar la base de datos a nuestra app
 * para que se guarden tanto los cambios que realicemos en los lugares
 * como cuando creamos un nuevo lugar
 *
 * @author Angel Lopez Palacios
 * @version 1
 */
public class AdaptadorLugaresBD extends AdaptadorLugares {

    protected Cursor cursor;

    /**
     * Constructor para inicializar el cursor de la clase
     *
     * @param lugares
     * @param cursor
     * @author Angel Lopez Palacios
     * @version 1
     */
    public AdaptadorLugaresBD(RepositorioLugares
                                      lugares, Cursor cursor) {
        super(lugares);
        this.cursor = cursor;
    }


    /**
     * Devuelve el cursor
     *
     * @return lugar
     * @author Angel Lopez Palacios
     * @version 1
     */
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * Establece un cursor
     *
     * @param cursor
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }


    /**
     * Devuelve el lugar a partir de la posición
     *
     * @param posicion
     * @return lugar
     * @author Angel Lopez Palacios
     * @version 1
     */
    public Lugar lugarPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return LugaresBD.extraeLugar(cursor);
    }

    /**
     * Devuelve el ID del lugar dependiendo de la posición que ocupe este en la base de datos
     *
     * @param posicion
     * @return id del lugar
     * @author Angel Lopez Palacios
     * @version 1
     */
    public int idPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return cursor.getInt(0);
    }


    /**
     * Método que muestra la posición a partir del ID del lugar
     *
     * @param id
     * @return posición del lugar
     * @author Angel Lopez Palacios
     * @version 1
     */
    public int posicionId(int id) {
        int pos = 0;
        int b = -1;
        cursor.moveToPosition(pos);
        while (pos != getItemCount()) {
            if (id == cursor.getInt(0)) {
                b = pos;
                break;
            } else {
                pos++;
                cursor.moveToPosition(pos);
            }
        }
        return b;
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
        Lugar lugar = lugarPosicion(posicion);
        holder.personaliza(lugar);
        holder.itemView.setTag(new Integer(posicion));
    }

    /**
     * Devuelve el número total de elementos en el conjunto de datos
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}