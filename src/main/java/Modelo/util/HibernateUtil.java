/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.util;

import Modelo.Usuario;
import Modelo.Videojuego;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase utilitaria para la gestión de la {@code SessionFactory} de Hibernate.
 * <p>
 * Se encarga de crear y proporcionar una única instancia de
 * {@link SessionFactory} para toda la aplicación, utilizando el archivo
 * de configuración {@code hibernate.cfg.xml} y las entidades anotadas.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class HibernateUtil {

    /**
     * Factoría de sesiones de Hibernate.
     * <p>
     * Se inicializa una sola vez al cargar la clase y se reutiliza
     * durante toda la ejecución de la aplicación.
     * </p>
     */
    private static final SessionFactory sessionFactory;

    /**
     * Bloque estático de inicialización.
     * <p>
     * Crea la {@code SessionFactory} a partir del archivo de configuración
     * de Hibernate y registra las clases de entidad utilizadas en el
     * proyecto. La entidad {@code Favorito} ha sido eliminada del modelo
     * al integrarse como campo booleano en {@code Videojuego}.
     * </p>
     */
    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Usuario.class)
                    .addAnnotatedClass(Videojuego.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al crear SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Devuelve la {@code SessionFactory} de Hibernate.
     *
     * @return instancia única de {@code SessionFactory}
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}