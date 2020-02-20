package com.example.mislugaresangellopezpalacios.Modelo;

/**
 * Interfaz RepositorioLugares
 *
 * @author Angel Lopez Palacios
 * @version 1
 */
public interface RepositorioLugares {

    /**
     * Recibido el identificador de un Lugar devuelve el lugar corrspondiente
     *
     * @param id identificador de lugar
     * @return Tipo Lugar
     * @author Angel Lopez Palacios
     * @version 1
     */
    Lugar elemento(int id);

    /**
     * Añade un lugar
     *
     * @param lugar Tipo Lugar
     * @author Angel Lopez Palacios
     * @version 1
     */
    void annade(Lugar lugar);

    /**
     * Crea un nuevo elemento.
     *
     * @return devuelve el identificador del elemento.
     * @author Angel Lopez Palacios
     * @version 1
     */
    int nuevo();

    /**
     * Borrar el elemento cuyo identificador se ha pasado por parametro.
     *
     * @param id identificador del elemento a borrar.
     * @author Angel Lopez Palacios
     * @version 1
     */
    void borrar(int id);

    /**
     * Tamaño de elementos
     *
     * @return Numero de elementos.
     * @author Angel Lopez Palacios
     * @version 1
     */
    int tamanno();

    /**
     * Actualiza el elemento al que le correspone la id lugar
     *
     * @param id    identificador del elemento.
     * @param lugar Lugar con datos modificados.
     * @author Angel Lopez Palacios
     * @version 1
     */
    void actualiza(int id, Lugar lugar);
}