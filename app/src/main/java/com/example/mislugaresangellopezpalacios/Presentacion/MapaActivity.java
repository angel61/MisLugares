package com.example.mislugaresangellopezpalacios.Presentacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.GeoPunto;
import com.example.mislugaresangellopezpalacios.Modelo.Lugar;
import com.example.mislugaresangellopezpalacios.Modelo.LugaresBD;
import com.example.mislugaresangellopezpalacios.Modelo.TipoLugar;
import com.example.mislugaresangellopezpalacios.R;
import com.example.mislugaresangellopezpalacios.casos_uso.CasosUsoLocalizacion;
import com.example.mislugaresangellopezpalacios.casos_uso.CasosUsoLugar;
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
 * @version 1
 */
public class MapaActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener,  GoogleMap.OnMarkerDragListener{

    private GoogleMap mapa;
    private AdaptadorLugaresBD adaptador;
    private GeoPunto ge;
    private int idDrag;
    private LugaresBD lugares;
    private static CasosUsoLugar usoLugar;

    /**
     * Este metodo inicializa el layout, el adaptador y el mapa
     *
     * @param savedInstanceState
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        adaptador = ((Aplicacion) getApplication()).adaptador;
        lugares = ((Aplicacion) getApplication()).lugares;
        usoLugar = new CasosUsoLugar(this, lugares, adaptador);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        ge = (GeoPunto) extras.get("posicion");//((Aplicacion) this.getApplication()).posicionActual;
    }

    /**
     * Este método se va a llamar cuando el mapa este listo para usarse y Obtengamos un objeto GoogleMap no nulo
     *
     * @param googleMap
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        ((Aplicacion) this.getApplication()).mapa=mapa;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.setOnMapLongClickListener(this);
        mapa.setOnMarkerDragListener(this);
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
        } else if (adaptador.getItemCount() > 0) {
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
                int id=adaptador.idPosicion(n);
                mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(p.getLatitud(), p.getLongitud()))
                        .title(lugar.getNombre()).snippet(lugar.getDireccion())
                        .icon(BitmapDescriptorFactory.fromBitmap(icono)).draggable(true)).setTag(id);
            }
        }
        mapa.setOnInfoWindowClickListener(this);

        ((Aplicacion) this.getApplication()).mapa=mapa;
    }

    /**
     * Evento que lanza una actividad {@link VistaLugarActivity} cuando se pulsa algun lugar marcado en el mapa
     *
     * @param marker
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTag() != null) {
            Intent intent = new Intent(this, VistaLugarActivity.class);
            intent.putExtra("pos", adaptador.posicionId((int) marker.getTag()));
            startActivity(intent);
        } else {
            GeoPunto posicion=new GeoPunto(marker.getPosition().longitude, marker.getPosition().latitude);
            usoLugar.nuevo(posicion);
        }
    }

   @Override
    public void onMapLongClick(LatLng latLng) {
        Bitmap iGrande = BitmapFactory.decodeResource(
                getResources(), TipoLugar.OTROS.getRecurso());
        Bitmap icono = Bitmap.createScaledBitmap(iGrande,
                iGrande.getWidth() / 7, iGrande.getHeight() / 7, false);
        final MarkerOptions marker= new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .title("Nuevo Lugar").snippet("¡Algun lugar nuevo!")
                .icon(BitmapDescriptorFactory.fromBitmap(icono));
        final GeoPunto posicion=new GeoPunto(marker.getPosition().longitude, marker.getPosition().latitude);

        new AlertDialog.Builder(this)
                .setTitle("Crear nuevo lugar")
                .setMessage("¿Quieres crear un lugar en esta posición?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mapa.addMarker(marker).setTag(null);
                        usoLugar.nuevo(posicion);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8) {
                            finish();
                            startActivity(getIntent());
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        GeoPunto posicion=new GeoPunto(marker.getPosition().longitude, marker.getPosition().latitude);
        Lugar lugar=adaptador.lugarPosicion(adaptador.posicionId((Integer) marker.getTag()));
        System.out.println(posicion.distancia(lugar.getPosicion()));
        lugar.setPosicion(posicion);
        usoLugar.guardar((Integer) marker.getTag(),lugar);
        System.out.println("actu");

    }
    @Override
    public void onBackPressed() {
        ((Aplicacion) getApplication()).mapa=null;
        this.finish();
    }
}