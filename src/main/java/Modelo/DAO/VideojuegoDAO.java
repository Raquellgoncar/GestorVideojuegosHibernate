/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.Videojuego;
import java.util.List;

/**
 * Interfaz DAO para la gestión de videojuegos.
 * <p>
 * Define las operaciones básicas de acceso a datos relacionadas con los
 * videojuegos registrados en la aplicación.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public interface VideojuegoDAO {

    /**
     * Inserta un nuevo videojuego en la base de datos.
     *
     * @param v videojuego a insertar
     */
    void insert(Videojuego v);

    /**
     * Actualiza los datos de un videojuego existente.
     *
     * @param v videojuego con los datos actualizados
     */
    void update(Videojuego v);

    /**
     * Elimina un videojuego a partir de su identificador.
     *
     * @param id identificador del videojuego
     */
    void delete(int id);

    /**
     * Obtiene un videojuego a partir de su identificador.
     *
     * @param id identificador del videojuego
     * @return videojuego correspondiente al id
     */
    Videojuego fetchOne(int id);

    /**
     * Obtiene todos los videojuegos registrados.
     *
     * @return lista de videojuegos
     */
    List<Videojuego> fetchAll();

    /**
     * Obtiene los videojuegos asociados a un usuario concreto.
     *
     * @param idUsuario identificador del usuario
     * @return lista de videojuegos del usuario
     */
    List<Videojuego> fetchByUsuario(int idUsuario);

    /**
     * Obtiene el número total de videojuegos registrados por un usuario
     * concreto.
     *
     * @param usuarioId identificador del usuario
     * @return número total de videojuegos del usuario
     */
    int countByUsuario(int usuarioId);
}
