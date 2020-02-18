package com.example.mislugaresangellopezpalacios.Presentacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad que simplemente muestra {@link PreferenciasFragment}
 *
 * @author Angel Lopez Palacios
 * @version 15/02/2020
 */
public class PreferenciasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }
}
