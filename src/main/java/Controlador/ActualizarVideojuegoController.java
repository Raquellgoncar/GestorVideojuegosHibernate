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
import Vista.ActualizarVideojuegoVista;
import Vista.ModificarVideojuegosVista;
import java.util.List;

/**
 * Controlador encargado de gestionar la lógica relacionada con la
 * actualización de videojuegos del usuario.
 * <p>
 * Actúa como intermediario entre la vista de actualización de videojuegos
 * y la capa de acceso a datos (DAO), permitiendo obtener videojuegos,
 * favoritos y gestionar la navegación entre vistas.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class ActualizarVideojuegoController {

    /**
     * Usuario autenticado que realiza las operaciones.
     */
    private Usuario usuario;

    /**
     * Vista asociada a la actualización de videojuegos.
     */
    private ActualizarVideojuegoVista vista;

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
    public ActualizarVideojuegoController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
        this.favoritoDAO = new FavoritoDAO_imp();
    }

    /**
     * Asocia la vista de actualización de videojuegos al controlador.
     *
     * @param vista vista de actualización
     */
    public void setVista(ActualizarVideojuegoVista vista) {
        this.vista = vista;
    }

    /**
     * Obtiene la lista de videojuegos pertenecientes al usuario.
     *
     * @return lista de videojuegos del usuario
     */
    public List<Videojuego> obtenerVideojuegosUsuario() {
        return videojuegoDAO.fetchByUsuario(usuario.getId());
    }

    /**
     * Obtiene la lista de videojuegos marcados como favoritos por el usuario.
     *
     * @return lista de favoritos del usuario
     */
    public List<Favorito> obtenerFavoritosUsuario() {
        return favoritoDAO.fetchByUsuario(usuario.getId());
    }

    /**
     * Obtiene un videojuego concreto a partir de su identificador.
     *
     * @param id identificador del videojuego
     * @return videojuego correspondiente al id
     */
    public Videojuego obtenerVideojuegoPorId(int id) {
        return videojuegoDAO.fetchOne(id);
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

    /**
     * Devuelve el usuario asociado al controlador.
     *
     * @return usuario actual
     */
    public Usuario getUsuario() {
        return usuario;
    }
}