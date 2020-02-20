package com.example.mislugaresangellopezpalacios.casos_uso;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static androidx.core.app.ActivityCompat.*;


/**
 * Clase para los distintos permisos de almacenamiento como pueden ser de escritura como de lecutra
 *
 * @author Angel Lopez Palacios
 * @version 1.6
 */
public class CasoDeUsoAlmacenamiento implements OnRequestPermissionsResultCallback {


    private static final String TAG = "MisLugares2019";
    private Activity actividad;
    private int codigoPermiso;


    /**
     * Constructor para solicitar permisos de lectura y escritura
     *
     * @param actividad
     * @param codigoPermiso
     * @author Angel Lopez Palacios
     * @version 1.6
     */
    public CasoDeUsoAlmacenamiento(Activity actividad, int codigoPermiso) {
        this.actividad = actividad;
        this.codigoPermiso = codigoPermiso;
        solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "Necesita permisos de almacenamiento para añadir fotografías", 1);
        solicitarPermiso(Manifest.permission.READ_EXTERNAL_STORAGE,
                "Necesita permisos de almacenamiento para añadir fotografías", 1);


    }

    /**
     * Método que genera un dialogo para pedir que se acepten los permisos de almacenamiento de la app
     *
     * @param permiso
     * @param justificacion
     * @param requestCode
     * @author Angel Lopez Palacios
     * @version 1.6
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
     * Solicitar permiso fragment.
     *
     * @param permiso
     * @param justificacion
     * @param requestCode
     * @param fragment
     * @author Angel Lopez Palacios
     * @version 1.6
     */
    public void solicitarPermisoFragment(final String permiso, String justificacion,
                                         final int requestCode, final Fragment fragment) {
        if (fragment.shouldShowRequestPermissionRationale(permiso)) {
            new AlertDialog.Builder(fragment.getActivity())
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            fragment.requestPermissions(
                                    new String[]{permiso}, requestCode);
                        }
                    })
                    .show();
        } else {
            fragment.requestPermissions(new String[]{permiso}, requestCode);
        }
    }


    /**
     * Método que si devuelve true significa que la app tiene permisos de lectura concedidos
     *
     * @return true o false
     * @author Angel Lopez Palacios
     * @version 1.6
     */
    public boolean hayPermisoAlmacenamiento() {
        return (checkSelfPermission(
                actividad, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Método que si devuelve true significa que la app tiene permisos de escritura concedidos
     *
     * @return true o false
     * @author Angel Lopez Palacios
     * @version 1.6
     */
    public boolean hayPermisoAlmacenamientoEscritura() {
        return (checkSelfPermission(
                actividad, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Devuelve el resultado de pedir los permisos
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @author Angel Lopez Palacios
     * @version 1.6
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == codigoPermiso
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this.actividad, "Permiso de lectura concedido", Toast.LENGTH_LONG).show();
    }
}
