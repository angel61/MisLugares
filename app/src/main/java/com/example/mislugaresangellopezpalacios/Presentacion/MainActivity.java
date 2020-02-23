package com.example.mislugaresangellopezpalacios.Presentacion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugares;
import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.R;
import com.example.mislugaresangellopezpalacios.casos_uso.CasosUsoLocalizacion;
import com.example.mislugaresangellopezpalacios.casos_uso.CasosUsoLugar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;


/**
 * Clase MainActivity que es la encargada de mostrar la pantalla principal
 *
 * @author Angel Lopez Palacios
 * @version 1
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private static CasosUsoLugar usoLugar;
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    public CasosUsoLocalizacion usoLocalizacion;
    static final int RESULTADO_PREFERENCIAS = 0;
    static final int RESULTADO_Activities = 1;
    private RippleDrawable rippleDrawable;

    /**
     * Método para inicializar el layout, los listener y llenar las demás clases
     *
     * @param savedInstanceState
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        adaptador = ((Aplicacion) getApplication()).adaptador;

        lugares = ((Aplicacion) getApplication()).lugares;
        usoLugar = new CasosUsoLugar(this, lugares, adaptador);


        preferencias();
        inicializarVistas();
        inicializarListeners();
        usoLocalizacion = new CasosUsoLocalizacion(this,
                SOLICITUD_PERMISO_LOCALIZACION);

    }

    /**
     * Método para inicializar la vista de el RecyclerView, la toolbar y del floating button
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void inicializarVistas() {

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    /**
     * Método para inicializar los listener, en este el del floating button para que cuando lo pulsemos llame al método de crear un nuevo Lugar,
     * tambien inicializa los listener para los elementos del RecyclerView
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    private void inicializarListeners() {

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                usoLugar.nuevo(((Aplicacion) getApplication())
                        .posicionActual);
                //finish();
            }
        });

        final GestureDetector GestureDetector =
                new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTapEvent(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });
        final GestureDetector gd2 =
                new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {

                        final View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null) {

                            final int pos = recyclerView.getChildAdapterPosition(child);
                            Context wrapper = new ContextThemeWrapper(MainActivity.this, R.style.popup_style);
                            PopupMenu popup = new PopupMenu(wrapper, child);
                            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.accion_editar:
                                            usoLugar.editar(pos, 15);
                                            return true;
                                        case R.id.accion_borrar:

                                            new AlertDialog.Builder(MainActivity.this)
                                                    .setTitle("Borrado de lugar")
                                                    .setMessage("¿Estas seguro que quieres eliminar este lugar?")
                                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {

                                                            int _id = adaptador.idPosicion(pos);
                                                            usoLugar.borrarSinFinish(_id);
                                                        }
                                                    })
                                                    .setNegativeButton("Cancelar", null)
                                                    .show();

                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });

                            popup.show();
                        }
                    }

                });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                    if (child != null) {


                        gd2.onTouchEvent(motionEvent);


                        if (GestureDetector.onTouchEvent(motionEvent)) {

                            rippleDrawable = (RippleDrawable) child.getBackground();
                            rippleDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                            rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});

                            int pos = recyclerView.getChildAdapterPosition(child);
                            usoLugar.mostrar(pos, 1);
                            // Toast.makeText(MainActivity.this,"Elemento seleccionado: "+ (pos+1) ,Toast.LENGTH_SHORT).show();

                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });
    }

    /**
     * Método para crear el menú superior con el menú establecido en el xml
     *
     * @param menu
     * @return true
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    /**
     * Método para definir las acciones de cada elemento del menú
     *
     * @param item
     * @return true
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, PreferenciasActivity.class);
            startActivityForResult(i, RESULTADO_PREFERENCIAS);

            return true;
        }
        if (id == R.id.acercaDe) {
            Intent i = new Intent(getBaseContext(), AcercaDeActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.menu_buscar) {
            lanzarVistaLugar(null);
            return true;
        }
        if (id == R.id.menu_mapa) {
            usoLugar.abrirMapa(((Aplicacion) getApplication()).posicionActual);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método para abrir un Diálogo para escribir el id del lugar que quieres visualizar
     *
     * @param view
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void lanzarVistaLugar(View view) {
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        new AlertDialog.Builder(this)
                .setTitle("Selección de lugar")
                .setMessage("indica su id:")
                .setView(entrada)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int id = Integer.parseInt(entrada.getText().toString());
                        usoLugar.mostrar(id, 1);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Método que se llama cuando una activity se completa, desde aquí se controla el evento de modificar la configuración en preferencias
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_Activities) {
        }
        if (requestCode == RESULTADO_PREFERENCIAS) {
            preferencias();
        }
    }

    /**
     * Método para iniciar la preferencias
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void preferencias() {
        Cursor aux = lugares.extraeCursor();
        adaptador.setCursor(aux);
        adaptador.notifyDataSetChanged();
    }


    /**
     * Método para controlar los permisos necesarios
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            usoLocalizacion.permisoConcedido();

    }

    /**
     * Método que activa la localización cuando vuelve a estar en primer plano
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    protected void onResume() {
        super.onResume();
        usoLocalizacion.activar();
    }

    /**
     * Método que desactiva la localización cuando se pausa la app
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    protected void onPause() {
        super.onPause();
        usoLocalizacion.desactivar();
    }

}
