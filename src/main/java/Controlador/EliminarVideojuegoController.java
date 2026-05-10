/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Usuario;
import Modelo.Videojuego;
import Vista.EliminarVideojuegoVista;
import Vista.ModificarVideojuegosVista;
import java.util.List;

/**
 * Controlador encargado de gestionar la eliminación de videojuegos.
 * <p>
 * Permite obtener los videojuegos del usuario y eliminar un videojuego
 * de la base de datos.
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
     * Constructor del controlador.
     * <p>
     * Inicializa el usuario y crea la implementación del DAO necesario
     * para acceder a la base de datos.
     * </p>
     *
     * @param usuario usuario autenticado
     */
    public EliminarVideojuegoController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
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
     * <p>
     * Ahora se obtienen directamente desde la tabla videojuegos
     * filtrando por el campo booleano {@code favorito}.
     * </p>
     *
     * @return lista de videojuegos favoritos del usuario
     */
    public List<Videojuego> obtenerFavoritos() {
        return videojuegoDAO.fetchFavoritosByUsuario(usuario.getId());
    }

    /**
     * Elimina un videojuego de la base de datos.
     * <p>
     * Al tener la FK con ON DELETE CASCADE y no existir ya tabla de favoritos
     * separada, simplemente se elimina el videojuego directamente.
     * </p>
     *
     * @param idVideojuego identificador del videojuego a eliminar
     */
    public void eliminarVideojuego(int idVideojuego) {
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