package com.example.mislugaresangellopezpalacios.casos_uso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.GeoPunto;
import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.Presentacion.Aplicacion;
import com.example.mislugaresangellopezpalacios.Presentacion.EdicionLugarActivity;
import com.example.mislugaresangellopezpalacios.Presentacion.VistaLugarActivity;

import java.io.File;
import java.io.IOException;

/**
 * En esta clase se ejecutan distintos casos de uso referentes a un lugar
 *
 * @author Angel Lopez Palacios
 * @version 15/02/2020
 */
public class CasosUsoLugar {
    private Activity actividad;
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;

    public CasosUsoLugar(Activity actividad, LugaresBD lugares,
                         AdaptadorLugaresBD adaptador) {
        this.actividad = actividad;
        this.lugares = lugares;
        this.adaptador = adaptador;
    }


    /**
     * Arrancará una actividad mostrando la información del lugar  según su posición en el RecyclerView
     *
     * @param pos
     * @param codidoSolicitud
     */
    public void mostrar(int pos, int codidoSolicitud) {
        if (lugares.extraeCursor().getCount() > 0) {
            Intent i = new Intent(actividad, VistaLugarActivity.class);
            i.putExtra("pos", pos);
            actividad.startActivityForResult(i, codidoSolicitud);
        } else {
        }
    }

    /**
     * Arrancará una actividad mostrando la intefaz de edición según su posición en el RecyclerView
     *
     * @param pos
     * @param codidoSolicitud
     */
    public void editar(int pos, int codidoSolicitud) {
        Intent i = new Intent(actividad, EdicionLugarActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codidoSolicitud);
    }

    /**
     * Borra el lugar según el id indicado
     *
     * @param id
     */
    public void borrar(final int id) {
        lugares.borrar(id);
        adaptador.setCursor(lugares.extraeCursor());
        adaptador.notifyDataSetChanged();
        actividad.finish();
    }
    public void borrarSinFinish(final int id) {
        lugares.borrar(id);
        adaptador.setCursor(lugares.extraeCursor());
        adaptador.notifyDataSetChanged();
    }

    /**
     * Guarda la informacion de un lugar editado o creado
     * Llama al metodo {@link LugaresBD#actualiza(int, Lugar)}
     *
     * @param id
     * @param nuevoLugar
     */
    public void guardar(int id, Lugar nuevoLugar) {
        lugares.actualiza(id, nuevoLugar);
        adaptador.setCursor(lugares.extraeCursor());
        adaptador.notifyDataSetChanged();
    }


    // INTENCIONES

    /**
     * Este método crea una intención con el proposito de compartir el nombre del lugar y la URL
     *
     * @param lugar
     */
    public void compartir(Lugar lugar) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,
                lugar.getNombre() + " - " + lugar.getUrl());
        actividad.startActivity(i);
    }

    /**
     * Este método crea una inteción con el proposito de marcar el número de teléfono del lugar
     *
     * @param lugar
     */
    public void llamarTelefono(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + lugar.getTelefono())));
    }

    /**
     * Este método crea una inteción con el proposito de abrir en el navegador la página web del lugar
     *
     * @param lugar
     */
    public void verPgWeb(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(lugar.getUrl())));
    }

    /**
     * Este método crea una inteción con el proposito de abrir en Google Maps la ubicación del lugar
     *
     * @param lugar
     */
    public final void verMapa(Lugar lugar) {
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        Uri uri = lugar.getPosicion() != GeoPunto.SIN_POSICION
                ? Uri.parse("geo:" + lat + ',' + lon)
                : Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        actividad.startActivity(new Intent("android.intent.action.VIEW", uri));
    }


    /**
     * Este método crea una intención indicando que queremos seleccionar contenido
     *
     * @param view
     * @param RESULTADO_GALERIA
     */
    public void ponerDeGaleria(View view, int RESULTADO_GALERIA) {
        String action;
        if (android.os.Build.VERSION.SDK_INT >= 19) { // API 19 - Kitkat
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent(action,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        actividad.startActivityForResult(intent, RESULTADO_GALERIA);
    }


    /**
     * Este método comienza obteniendo el lugar que corresponde al id para modificar la URI de su foto
     * Llama a los siguientes metodos: {@link Lugar#setFoto(String)}
     * {@link #visualizarFoto(Lugar, ImageView)}
     * {@link #actualizaPosLugar(int, Lugar)}
     *
     * @param pos
     * @param uri
     * @param imageView
     */
    public void ponerFoto(int pos, String uri, ImageView imageView) {
        Lugar lugar = adaptador.lugarPosicion(pos);
        lugar.setFoto(uri);
        visualizarFoto(lugar, imageView);
        actualizaPosLugar(pos, lugar);
    }


    /**
     * Verifica que la URI que acabamos de asignar no está vacía
     *
     * @param lugar
     * @param imageView
     */
    public void visualizarFoto(Lugar lugar, ImageView imageView) {
        if (lugar.getFoto() != null && !lugar.getFoto().isEmpty()) {
            imageView.setImageURI(Uri.parse(lugar.getFoto()));
        } else {
            imageView.setImageBitmap(null);
        }
    }


    /**
     * Crea  una intención indicando que queremos capturar una imagen desde el dispositivo
     *
     * @param codidoSolicitud
     * @return Uri. Retorna la ultima foto de realizada
     */
    public Uri tomarFoto(int codidoSolicitud) {
        try {
            Uri uriUltimaFoto;
            File file = File.createTempFile(
                    "img_" + (System.currentTimeMillis() / 1000), ".jpg",
                    actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            if (Build.VERSION.SDK_INT >= 24) {
                uriUltimaFoto = FileProvider.getUriForFile(
                        actividad, "com.example.mislugaresangellopezpalacios.fileprovider", file);

            } else {
                uriUltimaFoto = Uri.fromFile(file);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriUltimaFoto);
            actividad.startActivityForResult(intent, codidoSolicitud);
            return uriUltimaFoto;
        } catch (IOException ex) {
            Toast.makeText(actividad, "Error al crear fichero de imagen",
                    Toast.LENGTH_LONG).show();
            return null;
        }
    }


    /**
     * Este método almacena el lugar recibido en la posicion recibida
     * Llama a los metodos {@link AdaptadorLugaresBD#idPosicion(int)} y {@link #guardar(int, Lugar)}
     *
     * @param pos
     * @param lugar
     */
    public void actualizaPosLugar(int pos, Lugar lugar) {
        int id = adaptador.idPosicion(pos);
        guardar(id, lugar);  //
    }

    /**
     * Este método crea un nuevo lugar con su id y localización
     * Llama a {@link LugaresBD#nuevo()}, {@link LugaresBD#elemento(int)},
     * {@link Lugar#setPosicion(GeoPunto)} y {@link LugaresBD#actualiza(int, Lugar)}
     */
    public void nuevo() {
        int id = lugares.nuevo();
        GeoPunto posicion = ((Aplicacion) actividad.getApplication())
                .posicionActual;
        if (!posicion.equals(GeoPunto.SIN_POSICION)) {
            Lugar lugar = lugares.elemento(id);
            lugar.setPosicion(posicion);
            lugares.actualiza(id, lugar);
        }
        Intent i = new Intent(actividad, EdicionLugarActivity.class);
        i.putExtra("_id", id);
        actividad.startActivity(i);
    }
}

