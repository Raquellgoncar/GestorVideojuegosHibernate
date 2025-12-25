/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.util;

import com.password4j.Password;
import com.password4j.Hash;

/**
 *
 * @author Raquel
 */
public class PasswordService {
    
     // Genera el hash para guardar en BD
    public static String hashPassword(String plainPassword) {
        Hash hash = Password.hash(plainPassword)
                .withBcrypt();
        return hash.getResult();
    }

    // Verifica contraseña en login
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        return Password.check(plainPassword, storedHash)
                .withBcrypt();
    }
    
}
