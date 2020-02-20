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
 * Clase donde se realiza las operaciones habituales de los lugares
 * hacia la base de datos SQLite(Guardado , borrado y Modificado) o
 * eventos que se realizan en las vistas de la aplicacion (Mostrar lugares ,
 * Mostrar Mapa , Llamada,visualizar web y etc)
 *
 * @author Angel Lopez Palacios
 * @version 1
 */
public class CasosUsoLugar {
    private Activity actividad;
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;

    /**
     * Constructor de clase que recibe el contestodo de la actividad que lo usa , un Repositorio de Lugares
     * y el Adaptador de la Base e datos SQLite, inicializando las variables de esta clase.
     *
     * @param actividad
     * @param lugares
     * @param adaptador
     * @version 1
     * @author Angel Lopez Palacios
     * @see {@link LugaresBD}
     * @see {@link AdaptadorLugaresBD}
     */
    public CasosUsoLugar(Activity actividad, LugaresBD lugares,
                         AdaptadorLugaresBD adaptador) {
        this.actividad = actividad;
        this.lugares = lugares;
        this.adaptador = adaptador;
    }


    /**
     * Recibe la posicion del lugar y envia un intent con esa posicion a
     * VistaLugarActivity para que se inicie la actividad y muestre ese lugar.
     *
     * @param pos
     * @version 1
     * @author Angel Lopez Palacios
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
     * Metodo que recibe un ID del Lugar que se quiere editar y llamara a la actividad
     * EdicionLugarActivity para mostrar los campos modificables del lugar y sus datos ,
     * siempre y cuando el lugar no sea nuevo pues si no se mostraran vacios para que el
     * usuario los rellene.
     *
     * @param pos             Corresponde al Identificador del lugar de la base de datos.
     * @param codidoSolicitud Para actualizar la vista de lugar a traves del onActivityResult de la actividad.
     * @version 1
     * @author Angel Lopez Palacios
     */
    public void editar(int pos, int codidoSolicitud) {
        Intent i = new Intent(actividad, EdicionLugarActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codidoSolicitud);
    }


    /**
     * Metodo que recibe el id de un lugar y llama al metodo Lugares.borrar()
     * para eliminarlo de la base de datos, despues extrae el cursor de lugaresBD para
     * que el RecicleeView actualice los datos en el MainActivity.
     *
     * @param id del lugar en la base de datos.
     * @version 1
     * @author Angel Lopez Palacios
     * @see LugaresBD#borrar(int)
     */
    public void borrar(final int id) {
        lugares.borrar(id);
        adaptador.setCursor(lugares.extraeCursor());
        adaptador.notifyDataSetChanged();
        actividad.finish();
    }

    /**
     * Método exactamente igual a {@link #borrar(int)} exceptuando que no finaliza la  actividad
     *
     * @param id
     * @version 1
     * @author Angel Lopez Palacios
     * @see #borrar(int)
     */
    public void borrarSinFinish(final int id) {
        lugares.borrar(id);
        adaptador.setCursor(lugares.extraeCursor());
        adaptador.notifyDataSetChanged();
    }

    /**
     * Metodo recibe un Lugar y un ID , que guardara los cambios que se hayan realizado
     * en Lugar al que corresponda al ID que ha recibido.
     *
     * @param id         identificador del lugar en la base datos SQLite
     * @param nuevoLugar Lugar con datos modificados que se van a guardar.
     * @version 1
     * @author Angel Lopez Palacios
     * @see LugaresBD#actualiza(int, Lugar)
     */
    public void guardar(int id, Lugar nuevoLugar) {
        lugares.actualiza(id, nuevoLugar);
        adaptador.setCursor(lugares.extraeCursor());
        adaptador.notifyDataSetChanged();
    }


    // INTENCIONES


    /**
     * Compartir el lugar a traves de la aplicacion que el usuario eliga a traves de un intent.
     *
     * @param lugar
     * @version 1
     * @author Angel Lopez Palacios
     */
    public void compartir(Lugar lugar) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,
                lugar.getNombre() + " - " + lugar.getUrl());
        actividad.startActivity(i);
    }


    /**
     * Metodo que recibira el lugar y atraves de un intent abrira el dial de llamadas
     * para llamar al telefono de contacto del lugar.
     *
     * @param lugar
     * @version 1
     * @author Angel Lopez Palacios
     */
    public void llamarTelefono(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + lugar.getTelefono())));
    }

    /**
     * Metodo que Abrira la web del lugar en el navegador del telefono.
     *
     * @param lugar
     * @version 1
     * @author Angel Lopez Palacios
     */
    public void verPgWeb(Lugar lugar) {
        try {
            actividad.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(lugar.getUrl())));
        } catch (Exception ex) {
        }
    }

    /**
     * Mostrara la ubicacion del lugar en google maps.
     *
     * @param lugar
     * @version 1
     * @author Angel Lopez Palacios
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
     * Metodo que abre la galeria del telefono para seleccionar una imagen y la URI es enviada
     * a traves de un intent
     *
     * @param view              View Vista
     * @param RESULTADO_GALERIA int Resultado de la operacion
     * @version 1
     * @author Angel Lopez Palacios
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
     *
     * @param pos
     * @param uri
     * @param imageView
     * @version 1
     * @author Angel Lopez Palacios
     * @see {@link Lugar#setFoto(String)}
     * @see {@link #visualizarFoto(Lugar, ImageView)}
     * @see {@link #actualizaPosLugar(int, Lugar)}
     */
    public void ponerFoto(int pos, String uri, ImageView imageView) {
        Lugar lugar = adaptador.lugarPosicion(pos);
        lugar.setFoto(uri);
        visualizarFoto(lugar, imageView);
        actualizaPosLugar(pos, lugar);
    }


    /**
     * Verifica que la URI que acabamos de asignar no está vacía para usarla en el ImageView
     *
     * @param lugar
     * @param imageView
     * @version 1
     * @author Angel Lopez Palacios
     */
    public void visualizarFoto(Lugar lugar, ImageView imageView) {
        if (lugar.getFoto() != null && !lugar.getFoto().isEmpty()) {
            imageView.setImageURI(Uri.parse(lugar.getFoto()));
        } else {
            imageView.setImageBitmap(null);
        }
    }


    /**
     * Abre la aplicacion de la camara del movil para tomar una foto , cuando
     * la foto ha sido tomada devuelve la URI de  la imagen.
     *
     * @param codidoSolicitud
     * @return Uri de la imagen
     * @version 1
     * @author Angel Lopez Palacios
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
     * Método que almacena el lugar recibido en la posicion recibida
     *
     * @param pos
     * @param lugar
     * @version 1
     * @author Angel Lopez Palacios
     * @see {@link AdaptadorLugaresBD#idPosicion(int)}
     * @see {@link #guardar(int, Lugar)}
     */
    public void actualizaPosLugar(int pos, Lugar lugar) {
        int id = adaptador.idPosicion(pos);
        guardar(id, lugar);  //
    }

    /**
     * Metodo que creara un nuevo lugar  y abrira EdicionLugar para que insertemos los datos iniciales
     *
     * @version 1
     * @author Angel Lopez Palacios
     * @see {@link LugaresBD#nuevo()}
     * @see {@link LugaresBD#elemento(int)}
     * @see {@link Lugar#setPosicion(GeoPunto)}
     * @see {@link LugaresBD#actualiza(int, Lugar)}
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

