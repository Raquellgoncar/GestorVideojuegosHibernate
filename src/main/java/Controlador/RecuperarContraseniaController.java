/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.Usuario;
import Modelo.util.EmailService;
import Modelo.util.PasswordService;
import java.util.UUID;

/**
 * Controlador encargado de la recuperación de contraseñas.
 * <p>
 * Permite generar una nueva contraseña para un usuario a partir de su
 * dirección de correo electrónico, actualizarla de forma segura en la
 * base de datos y enviarla por email.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class RecuperarContraseniaController {

    /**
     * DAO para el acceso a datos de usuarios.
     */
    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor del controlador.
     * <p>
     * Inicializa la implementación del DAO de usuarios necesaria para
     * acceder a la base de datos.
     * </p>
     */
    public RecuperarContraseniaController() {
        this.usuarioDAO = new UsuarioDAO_imp();
    }

    /**
     * Recupera la contraseña de un usuario a partir de su email.
     * <p>
     * Si el email existe en la base de datos, se genera una nueva contraseña
     * aleatoria, se almacena cifrada mediante hash y se envía al correo
     * electrónico del usuario.
     * </p>
     *
     * @param email dirección de correo electrónico del usuario
     * @return {@code true} si la recuperación se realiza correctamente,
     *         {@code false} si el email no existe
     */
    public boolean recuperarPassword(String email) {

        Usuario usuario = usuarioDAO.fetchByEmail(email);
        if (usuario == null) {
            return false;
        }

        String nuevaPassword = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        usuario.setPasswordHash(
                PasswordService.hashPassword(nuevaPassword)
        );
        usuarioDAO.update(usuario);

        EmailService.enviarNuevaPassword(
                usuario.getEmail(),
                nuevaPassword
        );

        return true;
    }
}
