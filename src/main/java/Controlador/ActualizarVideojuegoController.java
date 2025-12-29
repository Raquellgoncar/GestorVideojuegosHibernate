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
 *
 * @author Raquel
 */
public class ActualizarVideojuegoController {

    private Usuario usuario;
    private ActualizarVideojuegoVista vista;

    private VideojuegoDAO videojuegoDAO;
    private FavoritoDAO favoritoDAO;

    public ActualizarVideojuegoController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
        this.favoritoDAO = new FavoritoDAO_imp();
    }

    public void setVista(ActualizarVideojuegoVista vista) {
        this.vista = vista;
    }

    public List<Videojuego> obtenerVideojuegosUsuario() {
        return videojuegoDAO.fetchByUsuario(usuario.getId());
    }

    public List<Favorito> obtenerFavoritosUsuario() {
        return favoritoDAO.fetchByUsuario(usuario.getId());
    }

    public Videojuego obtenerVideojuegoPorId(int id) {
        return videojuegoDAO.fetchOne(id);
    }

    public void volverModificar() {
        new ModificarVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
