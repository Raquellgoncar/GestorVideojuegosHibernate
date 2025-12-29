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
 *
 * @author Raquel
 */
public class ListadoVideojuegosController {

    private Usuario usuario;
    private ListadoVideojuegosVista vista;
    private VideojuegoDAO videojuegoDAO;

    public ListadoVideojuegosController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
    }

    public void setVista(ListadoVideojuegosVista vista) {
        this.vista = vista;
    }

    public List<Videojuego> obtenerVideojuegos() {
        return videojuegoDAO.fetchByUsuario(usuario.getId());
    }

    public void volverMenu() {
        new MenuPrincipalVista(usuario).setVisible(true);
        vista.dispose();
    }
}
