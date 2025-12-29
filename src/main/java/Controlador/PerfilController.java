/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Usuario;
import Modelo.util.PasswordService;
import Vista.MenuPrincipalVista;
import Vista.PerfilVista;

/**
 *
 * @author Raquel
 */
public class PerfilController {

    private Usuario usuario;
    private PerfilVista vista;

    private UsuarioDAO usuarioDAO = new UsuarioDAO_imp();
    private VideojuegoDAO videojuegoDAO = new VideojuegoDAO_imp();

    public PerfilController(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setVista(PerfilVista vista) {
        this.vista = vista;
    }

    /* ===== DATOS ===== */

    public int obtenerTotalJuegos() {
        return videojuegoDAO.fetchAll().size();
    }

    public void actualizarNombre(String nuevoNombre) {
        usuario.setNombre(nuevoNombre);
        usuarioDAO.update(usuario);
    }

    public void actualizarPassword(String nuevaPassword) {
        String hash = PasswordService.hashPassword(nuevaPassword);
        usuario.setPasswordHash(hash);
        usuarioDAO.update(usuario);
    }

    /* ===== NAVEGACIÓN ===== */

    public void volverMenu() {
        new MenuPrincipalVista(usuario).setVisible(true);
        vista.dispose();
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

