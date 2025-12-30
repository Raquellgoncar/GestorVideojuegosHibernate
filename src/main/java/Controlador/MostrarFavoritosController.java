/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.FavoritoDAO_imp;
import Modelo.Favorito;
import Modelo.Usuario;
import Vista.MenuPrincipalVista;
import Vista.MostrarFavoritosVista;
import java.util.List;

/**
 * Controlador encargado de gestionar los videojuegos marcados como favoritos.
 * <p>
 * Permite obtener los favoritos del usuario, eliminar un favorito concreto
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
     * DAO para el acceso a datos de favoritos.
     */
    private FavoritoDAO_imp favoritoDAO;

    /**
     * Constructor del controlador.
     * <p>
     * Inicializa el usuario y crea la implementación del DAO de favoritos
     * necesaria para acceder a la base de datos.
     * </p>
     *
     * @param usuario usuario autenticado
     */
    public MostrarFavoritosController(Usuario usuario) {
        this.usuario =usuario;
        this.favoritoDAO = new FavoritoDAO_imp();
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
     *
     * @return lista de favoritos del usuario
     */
    public List<Favorito> obtenerFavoritos() {
        return favoritoDAO.fetchByUsuario(usuario.getId());
    }

    /**
     * Elimina un videojuego de la lista de favoritos.
     * <p>
     * Borra el favorito de la base de datos y recarga el listado en la vista
     * para reflejar los cambios realizados.
     * </p>
     *
     * @param favorito favorito a eliminar
     */
    public void quitarFavorito(Favorito favorito) {
        favoritoDAO.delete(favorito.getId());
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

