package com.example.mislugaresangellopezpalacios.Presentacion;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.GeoPunto;
import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.R;
import com.example.mislugaresangellopezpalacios.casos_uso.CasosUsoLocalizacion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * FragmentActivity sobre el que vamos a cargar nuestro mapa de la API de Google Maps
 *
 * @author Angel Lopez Palacios
 * @version 15/02/2020
 */
public class MapaActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mapa;
    private AdaptadorLugaresBD adaptador;
    private GeoPunto ge;

    /**
     * Este metodo inicializa el layout, el adaptador y el mapa
     *
     * @param savedInstanceState
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        adaptador = ((Aplicacion) getApplication()).adaptador;
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        ge=((Aplicacion) this.getApplication()).posicionActual;
    }

    /**
     * Este método se va a llamar cuando el mapa este listo para usarse y Obtengamos un objeto GoogleMap no nulo
     *
     * @param googleMap
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            mapa.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style));

        } catch (Resources.NotFoundException e) {
        }

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(ge.getLatitud(), ge.getLongitud()), 12));
        }else
        if (adaptador.getItemCount() > 0) {
            GeoPunto p = adaptador.lugarPosicion(0).getPosicion();
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(p.getLatitud(), p.getLongitud()), 12));
        }
        for (int n = 0; n < adaptador.getItemCount(); n++) {
            Lugar lugar = adaptador.lugarPosicion(n);
            GeoPunto p = lugar.getPosicion();
            if (p != null && p.getLatitud() != 0) {

                Bitmap iGrande = BitmapFactory.decodeResource(
                        getResources(), lugar.getTipo().getRecurso());
                Bitmap icono = Bitmap.createScaledBitmap(iGrande,
                        iGrande.getWidth() / 7, iGrande.getHeight() / 7, false);
                mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(p.getLatitud(), p.getLongitud()))
                        .title(lugar.getNombre()).snippet(lugar.getDireccion())
                        .icon(BitmapDescriptorFactory.fromBitmap(icono)));
            }
        }
        mapa.setOnInfoWindowClickListener(this);
    }

    /**
     * Evento que lanza una actividad {@link VistaLugarActivity} cuando se pulsa algun lugar marcado en el mapa
     *
     * @param marker
     * @author Angel Lopez Palacios
     * @version 15/02/2020
     */
    @Override
    public void onInfoWindowClick(Marker marker) {

        for (int id = 0; id < adaptador.getItemCount(); id++) {
            if (adaptador.lugarPosicion(id).getNombre()
                    .equals(marker.getTitle())) {
                Intent intent = new Intent(this, VistaLugarActivity.class);
                intent.putExtra("pos", id);
                startActivity(intent);
                break;
            }
        }
    }
}