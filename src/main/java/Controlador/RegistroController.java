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
 * Controlador encargado del registro de nuevos usuarios.
 * <p>
 * Se encarga de validar los datos introducidos durante el registro,
 * comprobar que no existan usuarios duplicados y almacenar el nuevo
 * usuario en la base de datos con la contraseña cifrada.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class RegistroController {

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
    public RegistroController() {
        this.usuarioDAO = new UsuarioDAO_imp();
    }

    /**
     * Registra un nuevo usuario en la aplicación.
     * <p>
     * Comprueba que el nombre de usuario y el correo electrónico no estén
     * ya registrados. Si alguno existe, se lanza una excepción indicando
     * el motivo del error. La contraseña se almacena cifrada mediante hash.
     * </p>
     *
     * @param username nombre de usuario
     * @param nombre nombre real del usuario
     * @param email correo electrónico del usuario
     * @param password contraseña en texto plano
     * @throws IllegalArgumentException si el nombre de usuario o el email
     *         ya existen en la base de datos
     */
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
