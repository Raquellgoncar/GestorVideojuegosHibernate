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
 *
 * @author Raquel
 */
public class LoginController {

    private UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO_imp();
    }

    // Método para comprobar que los datos insertados pertenecen a alguien y están bien
    public Usuario autenticar(String username, String password) {

        Usuario usuario = usuarioDAO.fetchByUsername(username);

        if (usuario == null) {
            return null;
        }

        if (!PasswordService.verifyPassword(password, usuario.getPasswordHash())) {
            return null;
        }

        // LOGIN CORRECTO → GUARDAR ÚLTIMA CONEXIÓN
        usuario.setUltimaConexion(new Date());
        usuarioDAO.update(usuario);  

        return usuario;
    }
}
