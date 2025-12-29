/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Usuario;
import Vista.ListadoVideojuegosVista;
import Vista.LoginVista;
import Vista.MenuPrincipalVista;
import Vista.ModificarVideojuegosVista;
import Vista.MostrarFavoritosVista;
import Vista.PerfilVista;

/**
 *
 * @author Raquel
 */
public class MenuPrincipalController {

    private Usuario usuario;
    private MenuPrincipalVista vista;

    public MenuPrincipalController(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setVista(MenuPrincipalVista vista) {
        this.vista = vista;
    }

    public void mostrarListado() {
        new ListadoVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }

    public void modificarVideojuegos() {
        new ModificarVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }

    public void mostrarFavoritos() {
        new MostrarFavoritosVista(usuario).setVisible(true);
        vista.dispose();
    }

    public void mostrarPerfil() {
        new PerfilVista(usuario).setVisible(true);
        vista.dispose();
    }

    public void cerrarSesion() {
        new LoginVista().setVisible(true);
        vista.dispose();
    }
}