/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Usuario;
import Vista.ActualizarVideojuegoVista;
import Vista.EliminarVideojuegoVista;
import Vista.InsertarVideojuegoVista;
import Vista.MenuPrincipalVista;
import Vista.ModificarVideojuegosVista;

/**
 * Controlador encargado de gestionar la navegación dentro del módulo
 * de modificación de videojuegos.
 * <p>
 * Permite acceder a las distintas vistas relacionadas con la inserción,
 * actualización y eliminación de videojuegos, así como volver al menú
 * principal de la aplicación.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class ModificarVideojuegosController {

    /**
     * Usuario autenticado que utiliza la aplicación.
     */
    private Usuario usuario;

    /**
     * Vista asociada a la modificación de videojuegos.
     */
    private ModificarVideojuegosVista vista;

    /**
     * Constructor del controlador.
     *
     * @param usuario usuario autenticado
     */
    public ModificarVideojuegosController(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Asocia la vista de modificación de videojuegos al controlador.
     *
     * @param vista vista de modificación
     */
    public void setVista(ModificarVideojuegosVista vista) {
        this.vista = vista;
    }

    /**
     * Muestra la vista para insertar un nuevo videojuego.
     * <p>
     * Abre la ventana de inserción y cierra la vista actual.
     * </p>
     */
    public void insertar() {
        new InsertarVideojuegoVista(usuario).setVisible(true);
        vista.dispose();
    }

    /**
     * Muestra la vista para actualizar un videojuego existente.
     * <p>
     * Abre la ventana de actualización y cierra la vista actual.
     * </p>
     */
    public void actualizar() {
        new ActualizarVideojuegoVista(usuario).setVisible(true);
        vista.dispose();
    }

    /**
     * Muestra la vista para eliminar un videojuego.
     * <p>
     * Abre la ventana de eliminación y cierra la vista actual.
     * </p>
     */
    public void eliminar() {
        new EliminarVideojuegoVista(usuario).setVisible(true);
        vista.dispose();
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

