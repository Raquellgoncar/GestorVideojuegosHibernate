/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.Usuario;

/**
 *
 * @author Raquel
 */
public class GestorVideojuegosHibernate {
    
     public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO_imp();

        Usuario u = usuarioDAO.fetchByUsername("raquel");

        if (u != null) {
            System.out.println("Usuario encontrado: " + u.getUsername());
        } else {
            System.out.println("Usuario no existe");
        }
    }
}
