package com.example.mislugaresangellopezpalacios.casos_uso;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.mislugaresangellopezpalacios.Adaptadores.AdaptadorLugares;
import com.example.mislugaresangellopezpalacios.Modelo.GeoPunto;
import com.example.mislugaresangellopezpalacios.Presentacion.Aplicacion;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;


/**
 * Esta clase utiliza la información en dos formatos
 * Location esta variable es usada para disponer de la fecha de obtención o proveedor que nos la ha dado
 * GeoPunto que es el formato usado en el resto de la aplicación
 *
 * @author Angel Lopez Palacios
 * @version 15/02/2020
 */
public class CasosUsoLocalizacion implements LocationListener {
    private static final String TAG = "MisLugares";
    private Activity actividad;
    private int codigoPermiso;
    private LocationManager manejadorLoc;
    private Location mejorLoc;
    private GeoPunto posicionActual;
    private AdaptadorLugares adaptador;
    private static final long DOS_MINUTOS = 2 * 60 * 1000;

    /**
     * Constructor de {@link CasosUsoLocalizacion}
     *
     * @param actividad
     * @param codigoPermiso
     */
    public CasosUsoLocalizacion(Activity actividad, int codigoPermiso) {
        this.actividad = actividad;
        this.codigoPermiso = codigoPermiso;
        manejadorLoc = (LocationManager) actividad.getSystemService(LOCATION_SERVICE);
        posicionActual = ((Aplicacion) actividad.getApplication())
                .posicionActual;
        adaptador = ((Aplicacion) actividad.getApplication()).adaptador;
        ultimaLocalizazion();

        solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                "Necesita permisos de localización para acceder a su ubicación", 1);
    }

    /**
     * Este método que comprueba si el usuario ha concedido permisos o no
     *
     * @return
     */
    public boolean hayPermisoLocalizacion() {
        return (ActivityCompat.checkSelfPermission(
                actividad, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Este método busca la última localización disponible
     */
    @SuppressLint("MissingPermission")
    public void ultimaLocalizazion() {
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER));
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER));
            } else {
                solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                        "Sin el permiso localización no puedo mostrar la distancia" +
                                " a los lugares.", codigoPermiso/*, actividad*/);
            }
        }
    }

    /**
     * Este metodo solicita permisos de localizacion si es que la aplicacion aun no los tiene
     *
     * @param permiso
     * @param justificacion
     * @param requestCode
     */
    public void solicitarPermiso(final String permiso, String justificacion,
                                 final int requestCode) {
        if (shouldShowRequestPermissionRationale(actividad, permiso)) {
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }
                    })
                    .show();
        } else {
            requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    /**
     * Este método es llamado cuando el permiso es concedido
     * Su funcion es recoger la ultima localización disponible,
     * activa los eventos de localizacion y refresca el RecyclerView
     */
    public void permisoConcedido() {
        ultimaLocalizazion();
        activarProveedores();
        adaptador.notifyDataSetChanged();
    }

    /**
     * Hace que la clase sea informada con cada actualización del proveedor de localización
     * Si no tiene permisos los solicita
     */
    @SuppressLint("MissingPermission")
    private void activarProveedores() {
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manejadorLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        20 * 1000, 5, (LocationListener) this);
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                manejadorLoc.requestLocationUpdates(LocationManager
                        .NETWORK_PROVIDER, 10 * 1000, 10, (LocationListener) this);
            }
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso localización no puedo mostrar la distancia" +
                            " a los lugares.", codigoPermiso/*, actividad*/);
        }
    }


    /**
     * Actualiza la posición cuando está cambia
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Nueva localización: " + location);
        actualizaMejorLocaliz(location);
        adaptador.notifyDataSetChanged();
    }

    /**
     * Desactiva los proveedores
     *
     * @param proveedor
     */
    @Override
    public void onProviderDisabled(String proveedor) {
        Log.d(TAG, "Se deshabilita: " + proveedor);
        activarProveedores();
    }

    /**
     * Activa los proveedores
     *
     * @param proveedor
     */
    @Override
    public void onProviderEnabled(String proveedor) {
        Log.d(TAG, "Se habilita: " + proveedor);
        activarProveedores();
    }


    /**
     * Evento que se lanza cuando cambia el estado
     *
     * @param proveedor
     * @param estado
     * @param extras
     */
    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        Log.d(TAG, "Cambia estado: " + proveedor);
        activarProveedores();
    }


    /**
     * Este metodo actualiza {@link #mejorLoc} y {@link Aplicacion#posicionActual} cuando se cumplen ciertos requisitos
     *
     * @param localiz
     */
    private void actualizaMejorLocaliz(Location localiz) {
        if (localiz != null && (mejorLoc == null
                || localiz.getAccuracy() < 2 * mejorLoc.getAccuracy()
                || localiz.getTime() - mejorLoc.getTime() > DOS_MINUTOS)) {
            Log.d(TAG, "Nueva mejor localización");
            mejorLoc = localiz;
            ((Aplicacion) actividad.getApplication()).posicionActual.setLatitud(
                    localiz.getLatitude());
            ((Aplicacion) actividad.getApplication()).posicionActual.setLongitud(
                    localiz.getLongitude());
        }
    }

    /**
     * Activa los listeners
     */
    public void activar() {
        if (hayPermisoLocalizacion()) activarProveedores();
    }

    /**
     * Desactiva los listeners
     */
    public void desactivar() {
        if (hayPermisoLocalizacion()) manejadorLoc.removeUpdates(this);
    }

}