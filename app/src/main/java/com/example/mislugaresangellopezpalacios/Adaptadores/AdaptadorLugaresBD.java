package com.example.mislugaresangellopezpalacios.Adaptadores;

import android.database.Cursor;

import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.RepositorioLugares;

public class AdaptadorLugaresBD extends AdaptadorLugares {

    protected Cursor cursor;

    public AdaptadorLugaresBD(RepositorioLugares
                                      lugares, Cursor cursor) {
        super(lugares);
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Lugar lugarPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return LugaresBD.extraeLugar(cursor);
    }

    public int idPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return cursor.getInt(0);
    }

    public int posicionId(int id) {
        int pos = 0;
        int b=-1;
        cursor.moveToPosition(pos);
        while (pos!= getItemCount()){
            if(id==cursor.getInt(0)){b=pos;break;}else{
            pos++;
            cursor.moveToPosition(pos);}
        }
        return b;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Lugar lugar = lugarPosicion(posicion);
        holder.personaliza(lugar);
        holder.itemView.setTag(new Integer(posicion));
    }

    @Override public int getItemCount() {
        return cursor.getCount();
    }
}