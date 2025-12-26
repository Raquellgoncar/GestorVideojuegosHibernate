/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.Usuario;
import java.util.List;

/**
 *
 * @author Raquel
 */
public interface UsuarioDAO {

    void insert(Usuario u);

    void update(Usuario u);

    void delete(int id);

    Usuario fetchOne(int id);

    Usuario fetchByUsername(String username);
    
    Usuario fetchByEmail(String email);

    List<Usuario> fetchAll();

}
