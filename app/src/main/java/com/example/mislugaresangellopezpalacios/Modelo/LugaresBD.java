package com.example.mislugaresangellopezpalacios.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.example.mislugaresangellopezpalacios.Presentacion.Aplicacion;

/**
 * Clase que crea  la base de datos si no existe y que contiene
 * operaciones basicas con la base dedatos SQLite
 *
 * @author Angel Lopez Palacios
 * @version 1
 */
public class LugaresBD extends SQLiteOpenHelper
        implements RepositorioLugares {

    Context contexto;

    /**
     * Inilizacion de la clase
     *
     * @param contexto Contexto
     * @author Angel Lopez Palacios
     * @version 1
     */
    public LugaresBD(Context contexto) {
        super(contexto, "lugares", null, 1);
        this.contexto = contexto;
    }

    /**
     * Crea la base de datos si no esta creada ya.
     *
     * @param bd base datos
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE lugares (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "direccion TEXT, " +
                "longitud REAL, " +
                "latitud REAL, " +
                "tipo INTEGER, " +
                "foto TEXT, " +
                "telefono INTEGER, " +
                "url TEXT, " +
                "comentario TEXT, " +
                "fecha BIGINT, " +
                "valoracion REAL)");
        bd.execSQL("INSERT INTO lugares VALUES (null, " +
                "'Escuela Politécnica Superior de Gandía', " +
                "'C/ Paranimf, 1 46730 Gandia (SPAIN)', -0.166093, 38.995656, " +
                TipoLugar.EDUCACION.ordinal() + ", '', 962849300, " +
                "'http://www.epsg.upv.es', " +
                "'Uno de los mejores lugares para formarse.', " +
                System.currentTimeMillis() + ", 3.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null, 'Al de siempre', " +
                "'P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)', " +
                " -0.190642, 38.925857, " + TipoLugar.BAR.ordinal() + ", '', " +
                "636472405, '', " + "'No te pierdas el arroz en calabaza.', " +
                System.currentTimeMillis() + ", 3.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null, 'androidcurso.com', " +
                "'ciberespacio', 0.0, 0.0," + TipoLugar.EDUCACION.ordinal() + ", '', " +
                "962849300, 'http://androidcurso.com', " +
                "'Amplia tus conocimientos sobre Android.', " +
                System.currentTimeMillis() + ", 5.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null,'Barranco del Infierno'," +
                "'Vía Verde del río Serpis. Villalonga (Valencia)', -0.295058, " +
                "38.867180, " + TipoLugar.NATURALEZA.ordinal() + ", '', 0, " +
                "'http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-verde-del-" +
                "rio.html', 'Espectacular ruta para bici o andar', " +
                System.currentTimeMillis() + ", 4.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null, 'La Vital', " +
                "'Avda. La Vital,0 46701 Gandia (Valencia)',-0.1720092,38.9705949," +
                TipoLugar.COMPRAS.ordinal() + ", '', 962881070, " +
                "'http://www.lavital.es', 'El típico centro comercial', " +
                System.currentTimeMillis() + ", 2.0)");
    }

    /**
     * Actualizar base de datos a una nueva version
     *
     * @param db         base de datos
     * @param oldVersion Version vieja de la base de datos.
     * @param newVersion Version nueva de la base de datos.
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
    }

    /**
     * Recibe identificador y devuelve el lugar correspondiente a él.
     *
     * @param id identificador de lugar
     * @return
     * @version 1
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public Lugar elemento(int id) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM lugares WHERE _id = " + id, null);
        try {
            if (cursor.moveToNext())
                return extraeLugar(cursor);
            else
                throw new SQLException("Error al acceder al elemento _id = " + id);
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    /**
     * Sin funcionalidad.
     *
     * @param lugar Tipo Lugar
     * @author Angel Lopez Palacios
     * @version 1
     * @deprecated 1
     */
    @Override
    public void annade(Lugar lugar) {

    }

    /**
     * Crea un nuevo lugar vacio y devuelve su identificador correspondiente
     *
     * @return tipo int ,  identificador del nuevo lugar.
     * @version 1
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public int nuevo() {
        int _id = -1;
        Lugar lugar = new Lugar();
        getWritableDatabase().execSQL("INSERT INTO lugares (nombre, " +
                "direccion, longitud, latitud, tipo, foto, telefono, url, " +
                "comentario, fecha, valoracion) VALUES ('', '',  " +
                lugar.getPosicion().getLongitud() + "," +
                lugar.getPosicion().getLatitud() + ", " + lugar.getTipo().ordinal() +
                ", '', 0, '', '', " + lugar.getFecha() + ", 0)");
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT _id FROM lugares WHERE fecha = " + lugar.getFecha(), null);
        if (c.moveToNext()) _id = c.getInt(0);
        c.close();
        return _id;
    }

    /**
     * Borrar Lugar  al que corresponde el id pasado por parametro
     *
     * @param id int , correspondiente al identificador del lugar
     * @version 1
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void borrar(int id) {
        getWritableDatabase().execSQL("DELETE FROM lugares WHERE _id = " + id);
    }

    /**
     * Retorna el numero de lugares que tiene la base de datos.
     *
     * @return tamanyo tipo int.
     * @version 1
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public int tamanno() {
        return 0;
    }


    /**
     * Recibe el lugar que va actualizar y el id que lo identifica en la base de datos
     * para poder actualizar.
     *
     * @param id    identificador del lugar
     * @param lugar Lugar que recibe con los nuevos datos.
     * @version 1
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void actualiza(int id, Lugar lugar) {
        getWritableDatabase().execSQL("UPDATE lugares SET" +
                "   nombre = '" + lugar.getNombre() +
                "', direccion = '" + lugar.getDireccion() +
                "', longitud = " + lugar.getPosicion().getLongitud() +
                " , latitud = " + lugar.getPosicion().getLatitud() +
                " , tipo = " + lugar.getTipo().ordinal() +
                " , foto = '" + lugar.getFoto() +
                "', telefono = " + lugar.getTelefono() +
                " , url = '" + lugar.getUrl() +
                "', comentario = '" + lugar.getComentario() +
                "', fecha = " + lugar.getFecha() +
                " , valoracion = " + lugar.getValoracion() +
                " WHERE _id = " + id);
    }


    /**
     * Extrae lugar de un cursor
     *
     * @param cursor the cursor
     * @return Lugar
     * @version 1
     * @author Angel Lopez Palacios
     * @version 1
     */
    public static Lugar extraeLugar(Cursor cursor) {
        Lugar lugar = new Lugar();
        lugar.setNombre(cursor.getString(1));
        lugar.setDireccion(cursor.getString(2));
        lugar.setPosicion(new GeoPunto(cursor.getDouble(3),
                cursor.getDouble(4)));
        lugar.setTipo(TipoLugar.values()[cursor.getInt(5)]);
        lugar.setFoto(cursor.getString(6));
        lugar.setTelefono(cursor.getInt(7));
        lugar.setUrl(cursor.getString(8));
        lugar.setComentario(cursor.getString(9));
        lugar.setFecha(cursor.getLong(10));
        lugar.setValoracion(cursor.getFloat(11));
        return lugar;
    }

    /**
     * Extrae el cursor.
     *
     * @return Cursor
     * @version 1
     * @author Angel Lopez Palacios
     * @version 1
     */
    public Cursor extraeCursor() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(contexto);
        String consulta;
        switch (pref.getString("orden", "0")) {
            case "0":
                consulta = "SELECT * FROM lugares ";
                break;
            case "1":
                consulta = "SELECT * FROM lugares ORDER BY valoracion DESC";
                break;
            default:
                double lon = ((Aplicacion) contexto.getApplicationContext())
                        .posicionActual.getLongitud();
                double lat = ((Aplicacion) contexto.getApplicationContext())
                        .posicionActual.getLatitud();
                consulta = "SELECT * FROM lugares ORDER BY " +
                        "(" + lon + "-longitud)*(" + lon + "-longitud) + " +
                        "(" + lat + "-latitud )*(" + lat + "-latitud )";
                break;
        }
        consulta += " LIMIT " + pref.getString("maximo", "12");
        SQLiteDatabase bd = getReadableDatabase();
        return bd.rawQuery(consulta, null);
    }
}