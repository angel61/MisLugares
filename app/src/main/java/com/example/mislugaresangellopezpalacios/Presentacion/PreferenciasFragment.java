package com.example.mislugaresangellopezpalacios.Presentacion;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.mislugaresangellopezpalacios.R;

/**
 * Crea un fragment que contiene una ventana con las opciones de preferencias definidas en un recurso XML
 *
 * @see PreferenciasActivity
 * @author Angel Lopez Palacios
 * @version 1
 */
public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
