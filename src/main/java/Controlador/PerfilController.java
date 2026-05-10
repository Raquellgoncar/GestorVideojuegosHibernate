package Controlador;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Usuario;
import Modelo.util.PasswordService;
import Vista.MenuPrincipalVista;
import Vista.PerfilVista;
import java.util.Map;

/**
 * Controlador encargado de gestionar el perfil del usuario.
 * <p>
 * Permite consultar datos relacionados con el usuario, actualizar su
 * información personal (nombre y contraseña), obtener estadísticas
 * de la biblioteca y gestionar la navegación entre la vista de perfil
 * y el menú principal.
 * </p>
 *
 * @author Raquel
 * @version 2.0
 */
public class PerfilController {

    /** Usuario autenticado que utiliza la aplicación. */
    private Usuario usuario;

    /** Vista asociada al perfil del usuario. */
    private PerfilVista vista;

    /** DAO para el acceso a datos de usuarios. */
    private UsuarioDAO usuarioDAO = new UsuarioDAO_imp();

    /** DAO para el acceso a datos de videojuegos. */
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
     * Obtiene el número total de videojuegos registrados por el usuario.
     *
     * @return total de videojuegos
     */
    public int obtenerTotalJuegos() {
        return videojuegoDAO.countByUsuario(usuario.getId());
    }

    /**
     * Obtiene el conteo de videojuegos agrupados por plataforma
     * para generar el gráfico del perfil.
     *
     * @return mapa con plataforma y número de videojuegos registrados
     */
    public Map<String, Long> obtenerConteoPlataformas() {
        return videojuegoDAO.countPlataformasByUsuario(usuario.getId());
    }

    /**
     * Obtiene la plataforma en la que el usuario tiene más juegos registrados.
     * <p>
     * Se usa como filtro para las recomendaciones de RAWG por plataforma.
     * Devuelve {@code null} si el usuario no tiene videojuegos registrados.
     * </p>
     *
     * @return nombre de la plataforma más jugada o {@code null}
     */
    public String obtenerPlataformaMasJugada() {
        Map<String, Long> conteo = videojuegoDAO.countPlataformasByUsuario(usuario.getId());
        if (conteo == null || conteo.isEmpty()) return null;
        return conteo.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Obtiene el género más frecuente en la biblioteca del usuario.
     * <p>
     * Se usa como filtro para las recomendaciones de RAWG por género.
     * Devuelve {@code null} si el usuario no tiene videojuegos con género.
     * </p>
     *
     * @return nombre del género más jugado o {@code null}
     */
    public String obtenerGeneroMasJugado() {
        return videojuegoDAO.fetchGeneroMasJugado(usuario.getId());
    }

    /* ===== ACTUALIZACIÓN ===== */

    /**
     * Actualiza el nombre del usuario.
     *
     * @param nuevoNombre nuevo nombre del usuario
     */
    public void actualizarNombre(String nuevoNombre) {
        usuario.setNombre(nuevoNombre);
        usuarioDAO.update(usuario);
    }

    /**
     * Actualiza la contraseña del usuario cifrándola antes de guardarla.
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
