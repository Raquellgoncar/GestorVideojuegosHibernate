package Controlador;

import Modelo.Usuario;
import Vista.ActualizarVideojuegoVista;
import Vista.EliminarVideojuegoVista;
import Vista.MenuPrincipalVista;
import Vista.ModificarVideojuegosVista;
import Vista.SeleccionarModoInsertarDialog;

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
 * @version 2.0
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
     * Muestra el diálogo de selección de modo de inserción.
     * <p>
     * El usuario elige entre buscar en el catálogo de RAWG o añadir
     * el videojuego manualmente. La vista actual no se cierra hasta
     * que el usuario confirma su elección en el diálogo.
     * </p>
     */
    public void insertar() {
        new SeleccionarModoInsertarDialog(vista, usuario).setVisible(true);
    }

    /**
     * Muestra la vista para actualizar un videojuego existente.
     *
     * @param vista vista de modificación
     */
    public void actualizar() {
        new ActualizarVideojuegoVista(usuario, vista).setVisible(true);
    }

    /**
     * Muestra la vista para eliminar un videojuego.
     */
    public void eliminar() {
        new EliminarVideojuegoVista(usuario, vista).setVisible(true);
    }

    /**
     * Vuelve al menú principal de la aplicación.
     */
    public void volverMenu() {
        new MenuPrincipalVista(usuario).setVisible(true);
        vista.dispose();
    }
}
