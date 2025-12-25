/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.gestorvideojuegoshibernate;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.Usuario;
import Modelo.util.PasswordService;

/**
 *
 * @author Raquel
 */
public class GestorVideojuegosHibernate {

    public static void main(String[] args) {
        UsuarioDAO uDao = new UsuarioDAO_imp();

// REGISTRO (prueba)
        Usuario u = new Usuario();
        u.setUsername("raquel");
        u.setEmail("raquel@email.com");

        String hash = PasswordService.hashPassword("1234");
        u.setPasswordHash(hash);

        uDao.insert(u);

// LOGIN (prueba)
        Usuario uLogin = uDao.fetchByUsername("raquel");

        if (uLogin != null
                && PasswordService.verifyPassword("1234", uLogin.getPasswordHash())) {
            System.out.println("Login correcto");
        } else {
            System.out.println("Login incorrecto");
        }
    }
}
