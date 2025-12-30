/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.util;

import com.password4j.Password;
import com.password4j.Hash;

/**
 * Servicio encargado de la gestión segura de contraseñas.
 * <p>
 * Proporciona métodos para generar hashes de contraseñas y verificar
 * contraseñas introducidas por el usuario utilizando el algoritmo bcrypt.
 * En ningún caso se almacenan contraseñas en texto plano.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class PasswordService {

    /**
     * Genera el hash de una contraseña para su almacenamiento en la base de datos.
     * <p>
     * La contraseña en texto plano se cifra utilizando el algoritmo bcrypt,
     * generando un hash seguro que se almacena en la base de datos.
     * </p>
     *
     * @param plainPassword contraseña en texto plano
     * @return hash de la contraseña
     */
    public static String hashPassword(String plainPassword) {
        Hash hash = Password.hash(plainPassword)
                .withBcrypt();
        return hash.getResult();
    }

    /**
     * Verifica una contraseña introducida por el usuario.
     * <p>
     * Comprueba si la contraseña en texto plano coincide con el hash
     * almacenado en la base de datos utilizando bcrypt.
     * </p>
     *
     * @param plainPassword contraseña introducida en texto plano
     * @param storedHash hash almacenado en la base de datos
     * @return {@code true} si la contraseña es correcta,
     *         {@code false} en caso contrario
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        return Password.check(plainPassword, storedHash)
                .withBcrypt();
    }
}
