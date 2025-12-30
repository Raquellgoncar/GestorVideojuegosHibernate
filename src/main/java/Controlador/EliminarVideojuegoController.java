/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.FavoritoDAO;
import Modelo.DAO.Imp.FavoritoDAO_imp;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Favorito;
import Modelo.Usuario;
import Modelo.Videojuego;
import Vista.EliminarVideojuegoVista;
import Vista.ModificarVideojuegosVista;
import java.util.List;

/**
 * Controlador encargado de gestionar la eliminación de videojuegos.
 * <p>
 * Permite obtener los videojuegos y favoritos del usuario, así como
 * eliminar un videojuego asegurando previamente la eliminación de sus
 * referencias en la tabla de favoritos.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class EliminarVideojuegoController {

    /**
     * Usuario autenticado que realiza las operaciones.
     */
    private Usuario usuario;

    /**
     * Vista asociada a la eliminación de videojuegos.
     */
    private EliminarVideojuegoVista vista;

    /**
     * DAO para el acceso a datos de videojuegos.
     */
    private VideojuegoDAO videojuegoDAO;

    /**
     * DAO para el acceso a datos de favoritos.
     */
    private FavoritoDAO favoritoDAO;

    /**
     * Constructor del controlador.
     * <p>
     * Inicializa el usuario y crea las implementaciones de los DAO necesarios
     * para acceder a la base de datos.
     * </p>
     *
     * @param usuario usuario autenticado
     */
    public EliminarVideojuegoController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
        this.favoritoDAO = new FavoritoDAO_imp();
    }

    /**
     * Asocia la vista de eliminación de videojuegos al controlador.
     *
     * @param vista vista de eliminación
     */
    public void setVista(EliminarVideojuegoVista vista) {
        this.vista = vista;
    }

    /**
     * Obtiene la lista de videojuegos del usuario.
     *
     * @return lista de videojuegos del usuario
     */
    public List<Videojuego> obtenerVideojuegos() {
        return videojuegoDAO.fetchByUsuario(usuario.getId());
    }

    /**
     * Obtiene la lista de videojuegos marcados como favoritos por el usuario.
     *
     * @return lista de favoritos del usuario
     */
    public List<Favorito> obtenerFavoritos() {
        return favoritoDAO.fetchByUsuario(usuario.getId());
    }

    /**
     * Elimina un videojuego de la base de datos.
     * <p>
     * Antes de eliminar el videojuego, se eliminan todas las referencias
     * asociadas a dicho videojuego en la tabla de favoritos para mantener
     * la integridad de los datos.
     * </p>
     *
     * @param idVideojuego identificador del videojuego a eliminar
     */
    public void eliminarVideojuego(int idVideojuego) {

        favoritoDAO.fetchByUsuario(usuario.getId()).stream()
                .filter(f -> f.getVideojuegoId().getId().equals(idVideojuego))
                .forEach(f -> favoritoDAO.delete(f.getId()));

        videojuegoDAO.delete(idVideojuego);
    }

    /**
     * Vuelve a la vista de modificación de videojuegos.
     * <p>
     * Abre la ventana de modificación y cierra la vista actual.
     * </p>
     */
    public void volverModificar() {
        new ModificarVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }
}
