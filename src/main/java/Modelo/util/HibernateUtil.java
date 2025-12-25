/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.util;

import Modelo.Usuario;
import Modelo.Videojuego;
import Modelo.Favorito;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Raquel
 */
public class HibernateUtil {
    
     private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml") // coge el XML de resources
                    .addAnnotatedClass(Usuario.class)
                    .addAnnotatedClass(Videojuego.class)
                    .addAnnotatedClass(Favorito.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al crear SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
