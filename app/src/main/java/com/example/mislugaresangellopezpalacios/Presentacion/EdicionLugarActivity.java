package com.example.mislugaresangellopezpalacios.Presentacion;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.TipoLugar;
import com.example.mislugaresangellopezpalacios.R;
import com.example.mislugaresangellopezpalacios.casos_uso.CasosUsoLugar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EdicionLugarActivity extends AppCompatActivity {
    public LugaresBD lugares;
    private CasosUsoLugar usoLugar;
    private int pos;
    private Lugar lugar;

    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;

    private AdaptadorLugaresBD adaptador;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private int _id;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);
        Bundle extras = getIntent().getExtras();
       // pos = extras.getInt("pos", -1) ;//0
        pos = extras.getInt("pos", 0);
        _id = extras.getInt("_id",-1);
        lugares = ((Aplicacion) getApplication()).lugares;
        adaptador = ((Aplicacion) getApplication()).adaptador;
        usoLugar = new CasosUsoLugar(this, lugares,adaptador);

        if (_id!=-1) lugar = lugares.elemento(_id);
        else     lugar = adaptador.lugarPosicion (pos);//lugares.elemento(pos);
        actualizaVistas();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_cancelar:
                if (_id!=-1) lugares.borrar(_id);
                finish();
                return true;
            case R.id.accion_guardar:
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());
                if (_id==-1) _id = adaptador.idPosicion(pos);
                usoLugar.guardar(_id, lugar);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizaVistas() {
        nombre = findViewById(R.id.nombree);
        nombre.setText(lugar.getNombre());
         direccion = findViewById(R.id.direccione);
        direccion.setText(lugar.getDireccion());
     //   if (lugar.getTelefono() == 0) {
//            findViewById(R.id.telefono).setVisibility(View.GONE);
     //   } else {
            findViewById(R.id.telefonoe).setVisibility(View.VISIBLE);
             telefono = findViewById(R.id.telefonoe);
            telefono.setText(Integer.toString(lugar.getTelefono()));
       // }
        /*TextView telefono = findViewById(R.id.telefono);
        telefono.setText(Integer.toString(lugar.getTelefono()));*/
         url = findViewById(R.id.urle);
        url.setText(lugar.getUrl());
         comentario = findViewById(R.id.comentarioe);
        comentario.setText(lugar.getComentario());

         tipo = findViewById(R.id.tipoSpinner);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipo().ordinal());




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
    }
    @Override
    public void onBackPressed() {
        if (_id!=-1) lugares.borrar(_id);
        finish();
    }
}
