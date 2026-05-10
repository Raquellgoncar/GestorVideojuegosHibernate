/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Usuario;
import Modelo.Videojuego;
import Vista.MenuPrincipalVista;
import Vista.MostrarFavoritosVista;
import java.util.List;

/**
 * Controlador encargado de gestionar los videojuegos marcados como favoritos.
 * <p>
 * Permite obtener los favoritos del usuario, quitar un favorito concreto
 * y gestionar la navegación entre la vista de favoritos y el menú principal.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class MostrarFavoritosController {

    /**
     * Usuario autenticado que utiliza la aplicación.
     */
    private Usuario usuario;

    /**
     * Vista asociada al listado de favoritos.
     */
    private MostrarFavoritosVista vista;

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
    public MostrarFavoritosController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
    }

    /**
     * Asocia la vista de favoritos al controlador.
     *
     * @param vista vista de favoritos
     */
    public void setVista(MostrarFavoritosVista vista) {
        this.vista = vista;
    }

    /**
     * Obtiene la lista de videojuegos marcados como favoritos por el usuario.
     * <p>
     * Filtra directamente por el campo booleano {@code favorito} de la
     * tabla videojuegos, sin necesidad de tabla separada.
     * </p>
     *
     * @return lista de videojuegos favoritos del usuario
     */
    public List<Videojuego> obtenerFavoritos() {
        return videojuegoDAO.fetchFavoritosByUsuario(usuario.getId());
    }

    /**
     * Quita un videojuego de la lista de favoritos.
     * <p>
     * Cambia el campo {@code favorito} a false y actualiza el registro
     * en la base de datos. Luego recarga el listado en la vista.
     * </p>
     *
     * @param videojuego videojuego al que quitar el favorito
     */
    public void quitarFavorito(Videojuego videojuego) {
        videojuego.setFavorito(false);
        videojuegoDAO.update(videojuego);
        vista.cargarFavoritos();
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