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
 * Controlador encargado de gestionar el perfil del usuario.
 * <p>
 * Permite consultar datos relacionados con el usuario, actualizar su
 * información personal (nombre y contraseña) y gestionar la navegación entre la
 * vista de perfil y el menú principal.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class PerfilController {

    /**
     * Usuario autenticado que utiliza la aplicación.
     */
    private Usuario usuario;

    /**
     * Vista asociada al perfil del usuario.
     */
    private PerfilVista vista;

    /**
     * DAO para el acceso a datos de usuarios.
     */
    private UsuarioDAO usuarioDAO = new UsuarioDAO_imp();

    /**
     * DAO para el acceso a datos de videojuegos.
     */
    private VideojuegoDAO videojuegoDAO = new VideojuegoDAO_imp();

    /**
     * Constructor del controlador.
     *
     * @param usuario usuario autenticado
     */
    public PerfilController(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Asocia la vista de perfil al controlador.
     *
     * @param vista vista de perfil
     */
    public void setVista(PerfilVista vista) {
        this.vista = vista;
    }

    /* ===== DATOS ===== */
    /**
     * Obtiene el número total de videojuegos registrados.
     *
     * @return total de videojuegos
     */
    public int obtenerTotalJuegos() {
        return videojuegoDAO.countByUsuario(usuario.getId());
    }

    /**
     * Actualiza el nombre del usuario.
     * <p>
     * Modifica el nombre del usuario actual y guarda los cambios en la base de
     * datos.
     * </p>
     *
     * @param nuevoNombre nuevo nombre del usuario
     */
    public void actualizarNombre(String nuevoNombre) {
        usuario.setNombre(nuevoNombre);
        usuarioDAO.update(usuario);
    }

    /**
     * Actualiza la contraseña del usuario.
     * <p>
     * La nueva contraseña se cifra mediante un hash antes de almacenarse en la
     * base de datos por motivos de seguridad.
     * </p>
     *
     * @param nuevaPassword nueva contraseña en texto plano
     */
    public void actualizarPassword(String nuevaPassword) {
        String hash = PasswordService.hashPassword(nuevaPassword);
        usuario.setPasswordHash(hash);
        usuarioDAO.update(usuario);
    }

    /* ===== NAVEGACIÓN ===== */
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

    /**
     * Devuelve el usuario asociado al controlador.
     *
     * @return usuario actual
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
