/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.Usuario;
import Modelo.util.PasswordService;
import java.util.Date;

/**
 * Controlador encargado de gestionar la autenticación de usuarios.
 * <p>
 * Se encarga de comprobar las credenciales introducidas por el usuario
 * y validar el acceso a la aplicación utilizando la capa DAO y los
 * servicios de seguridad para la verificación de contraseñas.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class LoginController {

    /**
     * DAO para el acceso a datos de usuarios.
     */
    private UsuarioDAO usuarioDAO;

    /**
     * Constructor del controlador.
     * <p>
     * Inicializa la implementación del DAO de usuarios necesaria para
     * acceder a la base de datos.
     * </p>
     */
    public LoginController() {
        usuarioDAO = new UsuarioDAO_imp();
    }

    /**
     * Autentica a un usuario en la aplicación.
     * <p>
     * Comprueba si el nombre de usuario existe y verifica la contraseña
     * introducida comparándola con el hash almacenado en la base de datos.
     * Si la autenticación es correcta, se actualiza la fecha de la última
     * conexión del usuario.
     * </p>
     *
     * @param username nombre de usuario introducido
     * @param password contraseña introducida en texto plano
     * @return objeto {@code Usuario} si las credenciales son correctas,
     *         {@code null} en caso contrario
     */
    public Usuario autenticar(String username, String password) {

        Usuario usuario = usuarioDAO.fetchByUsername(username);

        if (usuario == null) {
            return null;
        }

        if (!PasswordService.verifyPassword(password, usuario.getPasswordHash())) {
            return null;
        }

        // Login correcto: se actualiza la última conexión
        usuario.setUltimaConexion(new Date());
        usuarioDAO.update(usuario);

        return usuario;
    }
}
