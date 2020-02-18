package com.example.mislugaresangellopezpalacios.Presentacion;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.R;
import com.example.mislugaresangellopezpalacios.casos_uso.CasoDeUsoAlmacenamiento;
import com.example.mislugaresangellopezpalacios.casos_uso.CasosUsoLugar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class VistaLugarActivity extends AppCompatActivity {
    public LugaresBD lugares;
    private CasosUsoLugar usoLugar;
    private int pos;
    private Lugar lugar;
    final static int RESULTADO_EDITAR = 1;

    private Toolbar toolbar;
    private LinearLayout lmapa;
    private LinearLayout lweb;
    private LinearLayout ltelefono;
    private FloatingActionButton fab;
    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;
    private ImageView imageView;
    private AdaptadorLugaresBD adaptador;
    private ImageView gal;
    private CasoDeUsoAlmacenamiento usoAlmacenamiento;
    private static final int MY_READ_REQUEST_CODE = 2;
    private Uri uriUltimaFoto;
    private Bundle extras;

    public int _id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_lugar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        // pos =adaptador.
        lugares = ((Aplicacion) getApplication()).lugares;
        adaptador = ((Aplicacion) getApplication()).adaptador;
        _id = adaptador.idPosicion(pos);
        lugar = adaptador.lugarPosicion(pos);//lugares.elemento(pos);
        usoLugar = new CasosUsoLugar(this, lugares, adaptador);
        actualizaVistas();
        inicializarListeners();


        usoAlmacenamiento = new CasoDeUsoAlmacenamiento(this, MY_READ_REQUEST_CODE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vista_lugar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_compartir:
                usoLugar.compartir(lugar);
                return true;
            case R.id.accion_llegar:
                usoLugar.verMapa(lugar);
                return true;
            case R.id.accion_editar:
                usoLugar.editar(pos, RESULTADO_EDITAR);
                return true;
            case R.id.accion_borrar:

                new AlertDialog.Builder(this)
                        .setTitle("Borrado de lugar")
                        .setMessage("¿Estas seguro que quieres eliminar este lugar?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                int _id = adaptador.idPosicion(pos);
                                usoLugar.borrar(_id);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_EDITAR) {
            lugar = lugares.elemento(_id);
            //pos = adaptador.posicionId(_id);
            actualizaVistas();
        } else if (requestCode == RESULTADO_GALERIA) {
            if (resultCode == Activity.RESULT_OK) {
                usoLugar.ponerFoto(pos, data.getDataString(), imageView);
            } else {
                Toast.makeText(this, "Foto no cargada", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULTADO_FOTO) {
            if (resultCode == Activity.RESULT_OK && uriUltimaFoto != null) {
                lugar.setFoto(uriUltimaFoto.toString());
                usoLugar.ponerFoto(pos, lugar.getFoto(), imageView);
            } else {
                Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void actualizaVistas() {
        TextView nombre = findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        ImageView logo_tipo = findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());
        TextView tipo = findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());
        TextView direccion = findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());
        if (lugar.getTelefono() == 0) {
            findViewById(R.id.telefono).setVisibility(View.GONE);
        } else {
            findViewById(R.id.telefono).setVisibility(View.VISIBLE);
            TextView telefono = findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
        }
        /*TextView telefono = findViewById(R.id.telefono);
        telefono.setText(Integer.toString(lugar.getTelefono()));*/
        TextView url = findViewById(R.id.url);
        url.setText(lugar.getUrl());
        TextView comentario = findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());
        TextView fecha = findViewById(R.id.fecha);
        fecha.setText(DateFormat.getDateInstance().format(
                new Date(lugar.getFecha())));
        TextView hora = findViewById(R.id.icono_hora);
        hora.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())));
        RatingBar valoracion = findViewById(R.id.valoracion);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float valor, boolean fromUser) {
                        lugar.setValoracion(valor);

                        usoLugar.actualizaPosLugar(pos, lugar);
                        pos = adaptador.posicionId(_id);

                        //pos = extras.getInt("pos", 0);
                    }
                });

        imageView = (ImageView) findViewById(R.id.foto);
        imageView.setClipToOutline(true);
        gal = (ImageView) findViewById(R.id.galeria);
        usoLugar.visualizarFoto(lugar, imageView);


    }


    private void inicializarListeners() {

        lmapa = findViewById(R.id.LinearMapa);
        lweb = findViewById(R.id.LinearWeb);
        ltelefono = findViewById(R.id.LinearTelefono);
        ImageView galeria = (ImageView) findViewById(R.id.galeria);
        ImageView camara = (ImageView) findViewById(R.id.camara);
        ImageView eliminarFoto = (ImageView) findViewById(R.id.eliminarfoto);


        lmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.verMapa(lugar);
            }
        });

        lweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.verPgWeb(lugar);
            }
        });
        ltelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.llamarTelefono(lugar);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.ponerDeGaleria(v, RESULTADO_GALERIA);
            }
        });
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.ponerDeGaleria(v, RESULTADO_GALERIA);
            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (usoAlmacenamiento.hayPermisoAlmacenamientoEscritura()) {

                    uriUltimaFoto = usoLugar.tomarFoto(RESULTADO_FOTO);

                } else {

                    Toast.makeText(getBaseContext(), "No hay permisos de almacenamiento, no se puede tomar la foto", Toast.LENGTH_LONG).show();
                }

            }
        });
        eliminarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.ponerFoto(pos, "", imageView);
            }
        });


        LinearLayout lFecha = findViewById(R.id.fechaVisita);
        LinearLayout lhora = findViewById(R.id.horaVisita);

        lFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarFecha();
            }
        });

        lhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarHora();
            }
        });

    }

    private void cambiarHora() {

        final Calendar horaSelect = Calendar.getInstance();
        horaSelect.setTimeInMillis(lugar.getFecha());
        horaSelect.set(Calendar.SECOND, 0);
        TimePickerDialog timePickerDialog = new TimePickerDialog(VistaLugarActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                horaSelect.set(Calendar.HOUR_OF_DAY, hourOfDay);
                horaSelect.set(Calendar.MINUTE, minutes);
                lugar.setFecha(horaSelect.getTimeInMillis());

                TextView hora = findViewById(R.id.icono_hora);
                hora.setText(DateFormat.getTimeInstance().format(
                        new Date(lugar.getFecha())));
                usoLugar.actualizaPosLugar(pos, lugar);
                pos = adaptador.posicionId(_id);
            }
        }, horaSelect.get(Calendar.HOUR_OF_DAY), horaSelect.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void cambiarFecha() {

        final Calendar fechaSelect = Calendar.getInstance();
        fechaSelect.setTimeInMillis(lugar.getFecha());

        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                fechaSelect.set(Calendar.YEAR, year);
                fechaSelect.set(Calendar.MONTH, month);
                fechaSelect.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                lugar.setFecha(fechaSelect.getTimeInMillis());

                TextView fecha = findViewById(R.id.fecha);
                fecha.setText(DateFormat.getDateInstance().format(
                        new Date(lugar.getFecha())));

                usoLugar.actualizaPosLugar(pos, lugar);
                pos = adaptador.posicionId(_id);

            }
        }, fechaSelect.get(Calendar.YEAR), fechaSelect.get(Calendar.MONTH), fechaSelect.get(Calendar.DAY_OF_MONTH));
        recogerFecha.show();
    }
    /*@Override public void onActivityCreated(Bundle state) {
     /*   if (extras != null) pos = extras.getInt("pos", 0);
        else                pos = 0;
        _id = adaptador.idPosicion(pos);}*/

}