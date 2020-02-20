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
import android.widget.Toast;

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
 * @version 1
 */
public class CasosUsoLocalizacion implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "MisLugares";
    private Activity actividad;
    private int codigoPermiso;
    private LocationManager manejadorLoc;
    private Location mejorLoc;
    private GeoPunto posicionActual;
    private AdaptadorLugares adaptador;
    private static final long DOS_MINUTOS = 2 * 60 * 1000;


    /**
     * Constructor para solicitar los permisos de localizacion y a la vez, inicializar la posición actual y la mejor posición conocida
     *
     * @param actividad
     * @param codigoPermiso
     * @author Angel Lopez Palacios
     * @version 1
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
     * Método que si devuelve true es que la app tiene permisos de localizacion concedidos
     *
     * @return true o false
     * @author Angel Lopez Palacios
     * @version 1
     */
    public boolean hayPermisoLocalizacion() {
        return (ActivityCompat.checkSelfPermission(
                actividad, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Método que guarda la ultima localización conocida del terminal
     *
     * @author Angel Lopez Palacios
     * @version 1
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
     * Método que genera un dialogo para pedir que se acepten los permisos de localización de la app
     *
     * @param permiso
     * @param justificacion
     * @param requestCode
     * @author Angel Lopez Palacios
     * @version 1
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
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void permisoConcedido() {
        ultimaLocalizazion();
        activarProveedores();
        adaptador.notifyDataSetChanged();
    }

    /**
     * Hace que la clase sea informada con cada actualización del proveedor de localización
     * Si no tiene permisos los solicita
     *
     * @author Angel Lopez Palacios
     * @version 1
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
     * Método que actualiza la localización si el terminal se ha movido
     *
     * @param location
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Nueva localización: " + location);
        actualizaMejorLocaliz(location);
        adaptador.notifyDataSetChanged();
    }


    /**
     * Método que activa los proveedores si estos estan deshabilitados
     *
     * @param proveedor
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onProviderDisabled(String proveedor) {
        Log.d(TAG, "Se deshabilita: " + proveedor);
        activarProveedores();
    }

    /**
     * Método que activa los proveedores para que no dejen de estas habilitados
     *
     * @param proveedor
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onProviderEnabled(String proveedor) {
        Log.d(TAG, "Se habilita: " + proveedor);
        activarProveedores();
    }


    /**
     * Método que comprueba que la ubicación del terminal ha cambiado
     *
     * @param proveedor
     * @param estado
     * @param extras
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        Log.d(TAG, "Cambia estado: " + proveedor);
        activarProveedores();
    }


    /**
     * Método que actualiza a la mejor localización del terminal cada 2 minutos
     *
     * @param localiz
     * @author Angel Lopez Palacios
     * @version 1
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
     * Método que activa los permisos de localización y proveedores
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void activar() {
        if (hayPermisoLocalizacion()) activarProveedores();
    }


    /**
     * Método que borra las localizaciones para el ahorro de datos
     *
     * @author Angel Lopez Palacios
     * @version 1
     */
    public void desactivar() {
        if (hayPermisoLocalizacion()) manejadorLoc.removeUpdates(this);
    }

    /**
     * Devuelve el resultado de pedir los permisos
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @author Angel Lopez Palacios
     * @version 1
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == codigoPermiso
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this.actividad, "Permiso de localización concedido", Toast.LENGTH_LONG).show();
    }
}