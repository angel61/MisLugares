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

/**
 * Clase para controlar la actividad del formulario de edicion_lugar, sus elementos
 * y el control de eventos. En el oncreate recogemos el id lugar correspondiente para apuntar desde
 * un cursor a la coleccion de datos desde el RecycleView. Una vez relaccionados los datos
 * realizamos las actualizaciones correspondientes. Este formulario tiene doble funcionalidad
 * ya que lo utilizamos tanto para edición del lugar como para crear uno nuevo. Controlamos
 * esta posibilidad mediante el id -1 que nos indica que no hemos seleccionado un cursor
 * desde la actividad principal sino que hemos seleccionado la opción de crear uno nuevo
 *
 * @author Angel Lopez Palacios
 * @version 1
 * @see androidx.appcompat.app.AppCompatActivity
 */
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

    /**
     * Inicializa los componentes de la actividad. El argumento Bundle
     * contiene el estado ya guardado de la actividad.
     * Si la actividad nunca ha existido, el valor del objeto Bundle es nulo.
     * <p>
     * muestra la configuración básica de la actividad, como declarar
     * la interfaz de usuario (definida en un archivo XML de diseño),
     * definir las variables de miembro y configurar parte de la IU
     * </p>
     *
     * @param savedInstanceState objeto Bundle que contiene el estado de la actividad.
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);
        Bundle extras = getIntent().getExtras();
        // pos = extras.getInt("pos", -1) ;//0
        pos = extras.getInt("pos", 0);
        _id = extras.getInt("_id", -1);
        lugares = ((Aplicacion) getApplication()).lugares;
        adaptador = ((Aplicacion) getApplication()).adaptador;
        usoLugar = new CasosUsoLugar(this, lugares, adaptador);

        if (_id != -1) lugar = lugares.elemento(_id);
        else lugar = adaptador.lugarPosicion(pos);//lugares.elemento(pos);
        actualizaVistas();
    }

    /**
     * Método implementado para gestionar el recurso de menú (definido en XML)
     * hacia el Menu proporcionado en la devolución de llamada.
     * <p>
     * Cuando comienza la actividad, para mostrar los elementos de la barra de app.
     * </p>
     *
     * @param menu proporcionado en el XML para muestra los elementos de la barra.
     * @return boolean que devuelve true en el caso de que se haya podido cargar la barra correctamente.
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    /**
     * Gestionamos el MenuItem seleccionado por el usuario. Recogemos el id del menu (definido por el atributo android:id)
     * en el recurso del menú para realizar la accion correspondiente.
     *
     * @param item ID único del elemento de menú
     * @return boolean donde controlamos que se ha escogido una opción válida del menú.
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_cancelar:
                if (_id != -1) lugares.borrar(_id);
                finish();
                return true;
            case R.id.accion_guardar:
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());
                if (_id == -1) _id = adaptador.idPosicion(pos);
                usoLugar.guardar(_id, lugar);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Actualización de los componentes de la aplicación con las propiedades de la posición del lugar correspondiente.
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void actualizaVistas() {
        nombre = findViewById(R.id.nombree);
        nombre.setText(lugar.getNombre());
        direccion = findViewById(R.id.direccione);
        direccion.setText(lugar.getDireccion());
        findViewById(R.id.telefonoe).setVisibility(View.VISIBLE);
        telefono = findViewById(R.id.telefonoe);
        telefono.setText(Integer.toString(lugar.getTelefono()));
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

    /**
     * Evento lanzado cuando se sale de la actividad para que no se quede guardado un lugar cuando entramos desde {@link MainActivity}
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onBackPressed() {
        if (_id != -1) lugares.borrar(_id);
        finish();
    }
}
