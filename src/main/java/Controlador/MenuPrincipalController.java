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
 * Controlador encargado de gestionar el menú principal de la aplicación.
 * <p>
 * Se encarga de la navegación entre las distintas vistas disponibles
 * desde el menú principal, sin contener lógica de negocio ni acceso
 * directo a la base de datos.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class MenuPrincipalController {

    /**
     * Usuario autenticado que utiliza la aplicación.
     */
    private Usuario usuario;

    /**
     * Vista asociada al menú principal.
     */
    private MenuPrincipalVista vista;

    /**
     * Constructor del controlador.
     *
     * @param usuario usuario autenticado
     */
    public MenuPrincipalController(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Asocia la vista del menú principal al controlador.
     *
     * @param vista vista del menú principal
     */
    public void setVista(MenuPrincipalVista vista) {
        this.vista = vista;
    }

    /**
     * Muestra la vista con el listado de videojuegos del usuario.
     * <p>
     * Abre la ventana de listado y cierra la vista actual.
     * </p>
     */
    public void mostrarListado() {
        new ListadoVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }

    /**
     * Muestra la vista de modificación de videojuegos.
     * <p>
     * Permite al usuario insertar, actualizar o eliminar videojuegos.
     * </p>
     */
    public void modificarVideojuegos() {
        new ModificarVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }

    /**
     * Muestra la vista con los videojuegos marcados como favoritos.
     */
    public void mostrarFavoritos() {
        new MostrarFavoritosVista(usuario).setVisible(true);
        vista.dispose();
    }

    /**
     * Muestra la vista del perfil del usuario.
     */
    public void mostrarPerfil() {
        new PerfilVista(usuario).setVisible(true);
        vista.dispose();
    }

    /**
     * Cierra la sesión actual del usuario.
     * <p>
     * Redirige a la ventana de inicio de sesión y cierra la vista actual.
     * </p>
     */
    public void cerrarSesion() {
        new LoginVista().setVisible(true);
        vista.dispose();
    }
}
