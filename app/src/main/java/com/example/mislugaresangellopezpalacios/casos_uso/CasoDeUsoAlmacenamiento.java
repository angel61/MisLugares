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
 * Esta clase se utiliza para perdir y comprobar permisos de lectura y escritura de imagenes
 *
 * @author Angel Lopez Palacios
 * @version 15/02/2020
 */
public class CasoDeUsoAlmacenamiento implements OnRequestPermissionsResultCallback {


    private static final String TAG = "MisLugares2019";
    private Activity actividad;
    private int codigoPermiso;

    /**
     * Constructor de {@link CasoDeUsoAlmacenamiento}
     *
     * @param actividad
     * @param codigoPermiso
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
     * Este metodo solicita permisos en una actividad
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
     * Este metodo solicita permisos en un Fragment
     *
     * @param permiso
     * @param justificacion
     * @param requestCode
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
     * Comprueba si hay permisos de lectura
     *
     * @return boolean
     */
    public boolean hayPermisoAlmacenamiento() {
        return (checkSelfPermission(
                actividad, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Comprueba si hay permisos de escritura
     *
     * @return boolean
     */
    public boolean hayPermisoAlmacenamientoEscritura() {
        return (checkSelfPermission(
                actividad, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Evento que comprueba si se concedieron permisos y muestra un mensaje si se concedieron
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
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
