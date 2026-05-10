/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Usuario;
import Modelo.Videojuego;
import Vista.ListadoVideojuegosVista;
import Vista.MenuPrincipalVista;
import java.util.List;

/**
 * Controlador encargado de gestionar el listado de videojuegos del usuario.
 * <p>
 * Permite obtener los videojuegos asociados al usuario autenticado y
 * gestiona la navegación entre la vista de listado y el menú principal.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class ListadoVideojuegosController {

    /**
     * Usuario autenticado que realiza las operaciones.
     */
    private Usuario usuario;

    /**
     * Vista asociada al listado de videojuegos.
     */
    private ListadoVideojuegosVista vista;

    /**
     * DAO para el acceso a datos de videojuegos.
     */
    private VideojuegoDAO videojuegoDAO;

    /**
     * Constructor del controlador.
     * <p>
     * Inicializa el usuario y crea la implementación del DAO de videojuegos
     * necesaria para acceder a la base de datos.
     * </p>
     *
     * @param usuario usuario autenticado
     */
    public ListadoVideojuegosController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
    }

    /**
     * Asocia la vista de listado de videojuegos al controlador.
     *
     * @param vista vista de listado
     */
    public void setVista(ListadoVideojuegosVista vista) {
        this.vista = vista;
    }

    /**
     * Obtiene la lista de videojuegos del usuario.
     *
     * @return lista de videojuegos asociados al usuario
     */
    public List<Videojuego> obtenerVideojuegos() {
        return videojuegoDAO.fetchByUsuario(usuario.getId());
    }

    /**
     * Obtiene la lista de videojuegos marcados como favoritos por el usuario.
     * <p>
     * Ahora se consulta directamente el campo booleano {@code favorito}
     * de la tabla videojuegos, sin necesidad de tabla separada ni Set de IDs.
     * En la vista basta con llamar a {@code videojuego.isFavorito()} para
     * saber si mostrar el indicador visual de favorito.
     * </p>
     *
     * @return lista de videojuegos favoritos del usuario
     */
    public List<Videojuego> obtenerFavoritos() {
        return videojuegoDAO.fetchFavoritosByUsuario(usuario.getId());
    }

    /**
     * Quita un videojuego de favoritos actualizando el campo en la base de datos.
     *
     * @param videojuego videojuego al que quitar el favorito
     */
    public void quitarFavorito(Videojuego videojuego) {
        videojuego.setFavorito(false);
        videojuegoDAO.update(videojuego);
    }

    /**
     * Vuelve al menú principal de la aplicación.
     * <p>
     * Abre la ventana del menú principal y cierra la vista actual.
     * </p>
     */
    public void volverMenu() {
        new MenuPrincipalVista(usuario).setVisible(true);
        vista.dispose();
    }
}