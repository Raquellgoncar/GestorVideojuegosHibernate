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
 *
 * @author Raquel
 */
public class MostrarFavoritosController {

    private Usuario usuario;
    private MostrarFavoritosVista vista;
    private FavoritoDAO_imp favoritoDAO;

    public MostrarFavoritosController(Usuario usuario) {
        this.usuario = usuario;
        this.favoritoDAO = new FavoritoDAO_imp();
    }

    public void setVista(MostrarFavoritosVista vista) {
        this.vista = vista;
    }

    public List<Favorito> obtenerFavoritos() {
        return favoritoDAO.fetchByUsuario(usuario.getId());
    }

    public void quitarFavorito(Favorito favorito) {
        favoritoDAO.delete(favorito.getId());
        vista.cargarFavoritos();
    }

    public void volverMenu() {
        new MenuPrincipalVista(usuario).setVisible(true);
        vista.dispose();
    }
}
  
