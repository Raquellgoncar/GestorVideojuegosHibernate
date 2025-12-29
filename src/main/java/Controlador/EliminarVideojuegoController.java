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
 *
 * @author Raquel
 */
public class EliminarVideojuegoController {

    private Usuario usuario;
    private EliminarVideojuegoVista vista;

    private VideojuegoDAO videojuegoDAO;
    private FavoritoDAO favoritoDAO;

    public EliminarVideojuegoController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
        this.favoritoDAO = new FavoritoDAO_imp();
    }

    public void setVista(EliminarVideojuegoVista vista) {
        this.vista = vista;
    }

    public List<Videojuego> obtenerVideojuegos() {
        return videojuegoDAO.fetchByUsuario(usuario.getId());
    }

    public List<Favorito> obtenerFavoritos() {
        return favoritoDAO.fetchByUsuario(usuario.getId());
    }

    public void eliminarVideojuego(int idVideojuego) {

        favoritoDAO.fetchByUsuario(usuario.getId()).stream()
                .filter(f -> f.getVideojuegoId().getId().equals(idVideojuego))
                .forEach(f -> favoritoDAO.delete(f.getId()));

        videojuegoDAO.delete(idVideojuego);
    }

    public void volverModificar() {
        new ModificarVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }
}

