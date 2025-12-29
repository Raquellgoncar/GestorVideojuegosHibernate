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
 *
 * @author Raquel
 */
public class RecuperarContraseniaController {

    private final UsuarioDAO usuarioDAO;

    public RecuperarContraseniaController() {
        this.usuarioDAO = new UsuarioDAO_imp();
    }

    //Metodo para meter el email y que se envíe un correo con una contraseña nueva
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
