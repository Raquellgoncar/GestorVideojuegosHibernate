/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.Favorito;
import java.util.List;

/**
 * Interfaz DAO para la gestión de favoritos.
 * <p>
 * Define las operaciones básicas de acceso a datos relacionadas con
 * los videojuegos marcados como favoritos por los usuarios.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public interface FavoritoDAO {

    /**
     * Inserta un nuevo favorito en la base de datos.
     *
     * @param f favorito a insertar
     */
    void insert(Favorito f);

    /**
     * Elimina un favorito a partir de su identificador.
     *
     * @param id identificador del favorito
     */
    void delete(int id);

    /**
     * Elimina un favorito a partir del usuario y el videojuego asociados.
     * <p>
     * Se utiliza cuando se desea eliminar la relación entre un usuario
     * y un videojuego concreto.
     * </p>
     *
     * @param usuarioId identificador del usuario
     * @param videojuegoId identificador del videojuego
     */
    void deleteByUsuarioYVideojuego(int usuarioId, int videojuegoId);

    /**
     * Obtiene un favorito a partir de su identificador.
     *
     * @param id identificador del favorito
     * @return favorito correspondiente al id
     */
    Favorito fetchOne(int id);

    /**
     * Obtiene todos los favoritos registrados.
     *
     * @return lista de todos los favoritos
     */
    List<Favorito> fetchAll();

    /**
     * Obtiene los favoritos asociados a un usuario concreto.
     *
     * @param usuarioId identificador del usuario
     * @return lista de favoritos del usuario
     */
    List<Favorito> fetchByUsuario(int usuarioId);
}

