/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.Usuario;
import Modelo.util.PasswordService;

/**
 *
 * @author Raquel
 */
public class RegistroController {

    private UsuarioDAO usuarioDAO;

    public RegistroController() {
        this.usuarioDAO = new UsuarioDAO_imp();
    }

    //Metodo para registrar metiendo los datos necesarios
    public void registrar(
            String username,
            String nombre,
            String email,
            String password
    ) throws IllegalArgumentException {

        if (usuarioDAO.fetchByUsername(username) != null) {
            throw new IllegalArgumentException("USERNAME_EXISTE");
        }

        if (usuarioDAO.fetchByEmail(email) != null) {
            throw new IllegalArgumentException("EMAIL_EXISTE");
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPasswordHash(
                PasswordService.hashPassword(password)
        );

        usuarioDAO.insert(u);
    }
}