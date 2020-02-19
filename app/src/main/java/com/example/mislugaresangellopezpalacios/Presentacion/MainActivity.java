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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private Button bAcercaDe;
    private Button bSalir;
    private Button bPreferencias;
    //private RepositorioLugares lugares;
    private RecyclerView recyclerView;
    //public AdaptadorLugares adaptador;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        adaptador = ((Aplicacion) getApplication()).adaptador;

        lugares = ((Aplicacion) getApplication()).lugares;
        usoLugar = new CasosUsoLugar(this, lugares,adaptador);




        preferencias();
        inicializarVistas();
        inicializarListeners();
        usoLocalizacion = new CasosUsoLocalizacion(this,
                SOLICITUD_PERMISO_LOCALIZACION);

    }
    public void inicializarVistas(){

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bAcercaDe = findViewById(R.id.button03);

        //bSalir = findViewById(R.id.button04);

        //bPreferencias = findViewById(R.id.button02);

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }
    private void inicializarListeners(){

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                usoLugar.nuevo();
                //finish();
            }
        });

        final GestureDetector GestureDetector =
                new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override public boolean onDoubleTapEvent(MotionEvent e) {
                        return false;
                    }
                    @Override public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });
        final GestureDetector gd2 =
                new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override  public void onLongPress(MotionEvent e) {

                        final View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if(child!=null){

                            final int pos = recyclerView.getChildAdapterPosition(child);
                        PopupMenu popup = new PopupMenu(MainActivity.this, child);
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

                        popup.show();}
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
                    if(child != null ){


                    gd2.onTouchEvent(motionEvent);



                        if (GestureDetector.onTouchEvent(motionEvent)) {

                            rippleDrawable = (RippleDrawable) child.getBackground();
                            rippleDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                            rippleDrawable.setState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled });

                            int pos = recyclerView.getChildAdapterPosition(child);
                            usoLugar.mostrar(pos,1);
                            // Toast.makeText(MainActivity.this,"Elemento seleccionado: "+ (pos+1) ,Toast.LENGTH_SHORT).show();

                            return true;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            /*
            Intent i = new Intent(getBaseContext(), busca_id.class);
            startActivity(i);*/
           lanzarVistaLugar(null);
            return true;
        }
        if (id==R.id.menu_mapa) {
            Intent intent = new Intent(this, MapaActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void salir(View view){
        finish();
    }
   /* public static void lanzarVistaLugar(View view, int num){
        usoLugar.mostrar(num);
    }*/
   public void lanzarVistaLugar(View view){
       final EditText entrada = new EditText(this);
       entrada.setText("0");
       new AlertDialog.Builder(this)
               .setTitle("Selección de lugar")
               .setMessage("indica su id:")
               .setView(entrada)
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton) {
                       int id = Integer.parseInt (entrada.getText().toString());
                       usoLugar.mostrar(id,1);
                   }})
               .setNegativeButton("Cancelar", null)
               .show();
   }

    @Override protected void onActivityResult(int requestCode, int resultCode,
                                              Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_Activities) {
        }
        if (requestCode == RESULTADO_PREFERENCIAS) {
            preferencias();
        }
    }
    public void preferencias(){
        Cursor aux=lugares.extraeCursor();
        adaptador.setCursor(aux);
        adaptador.notifyDataSetChanged();
    }
    @Override public void onRequestPermissionsResult(int requestCode,
                                                     String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            usoLocalizacion.permisoConcedido();

    }

    @Override protected void onResume() {
        super.onResume();
        usoLocalizacion.activar();
    }

    @Override protected void onPause() {
        super.onPause();
        usoLocalizacion.desactivar();
    }

}
