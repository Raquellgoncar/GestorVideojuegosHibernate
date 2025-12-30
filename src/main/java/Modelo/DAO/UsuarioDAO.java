/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.Usuario;
import java.util.List;

/**
 * Interfaz DAO para la gestión de usuarios.
 * <p>
 * Define las operaciones básicas de acceso a datos relacionadas con
 * los usuarios de la aplicación, incluyendo inserción, actualización,
 * eliminación y consultas.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public interface UsuarioDAO {

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param u usuario a insertar
     */
    void insert(Usuario u);

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param u usuario con los datos actualizados
     */
    void update(Usuario u);

    /**
     * Elimina un usuario a partir de su identificador.
     *
     * @param id identificador del usuario
     */
    void delete(int id);

    /**
     * Obtiene un usuario a partir de su identificador.
     *
     * @param id identificador del usuario
     * @return usuario correspondiente al id
     */
    Usuario fetchOne(int id);

    /**
     * Obtiene un usuario a partir de su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return usuario correspondiente al username
     */
    Usuario fetchByUsername(String username);

    /**
     * Obtiene un usuario a partir de su correo electrónico.
     *
     * @param email correo electrónico
     * @return usuario correspondiente al email
     */
    Usuario fetchByEmail(String email);

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios
     */
    List<Usuario> fetchAll();
}
