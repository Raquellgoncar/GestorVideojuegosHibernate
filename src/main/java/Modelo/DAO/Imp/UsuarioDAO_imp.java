/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO.Imp;

import Modelo.DAO.UsuarioDAO;

import Modelo.Usuario;
import Modelo.DAO.UsuarioDAO;
import Modelo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * Implementación DAO para la gestión de usuarios mediante Hibernate.
 * <p>
 * Esta clase implementa la interfaz {@code UsuarioDAO} y proporciona
 * las operaciones necesarias para insertar, actualizar, eliminar y
 * consultar usuarios en la base de datos utilizando Hibernate.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class UsuarioDAO_imp implements UsuarioDAO {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * <p>
     * Abre una sesión Hibernate, inicia una transacción y guarda
     * el objeto {@code Usuario}. En caso de error, la transacción
     * se revierte.
     * </p>
     *
     * @param u usuario a insertar
     */
    @Override
    public void insert(Usuario u) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.save(u);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     * <p>
     * Guarda los cambios del usuario en la base de datos dentro
     * de una transacción Hibernate.
     * </p>
     *
     * @param u usuario con los datos actualizados
     */
    @Override
    public void update(Usuario u) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.update(u);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Elimina un usuario a partir de su identificador.
     * <p>
     * Busca el usuario en la base de datos y, si existe,
     * lo elimina dentro de una transacción.
     * </p>
     *
     * @param id identificador del usuario
     */
    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Usuario u = s.get(Usuario.class, id);
            if (u != null) {
                s.delete(u);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un usuario a partir de su identificador.
     *
     * @param id identificador del usuario
     * @return usuario correspondiente al id o {@code null} si no existe
     */
    @Override
    public Usuario fetchOne(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Usuario.class, id);
        }
    }

    /**
     * Obtiene un usuario a partir de su nombre de usuario.
     * <p>
     * Ejecuta una consulta HQL para localizar un usuario único
     * mediante su username.
     * </p>
     *
     * @param username nombre de usuario
     * @return usuario correspondiente o {@code null} si no existe
     */
    @Override
    public Usuario fetchByUsername(String username) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Usuario u WHERE u.username = :username",
                    Usuario.class
            ).setParameter("username", username)
                    .uniqueResult();
        }
    }

    /**
     * Obtiene un usuario a partir de su correo electrónico.
     * <p>
     * Se utiliza principalmente en procesos de registro y
     * recuperación de contraseña.
     * </p>
     *
     * @param email correo electrónico del usuario
     * @return usuario correspondiente al email o {@code null} si no existe
     */
    @Override
    public Usuario fetchByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuario usuario = session
                .createQuery("FROM Usuario WHERE email = :email", Usuario.class)
                .setParameter("email", email)
                .uniqueResult();
        session.close();
        return usuario;
    }

    /**
     * Obtiene todos los usuarios registrados en la base de datos.
     *
     * @return lista de usuarios
     */
    @Override
    public List<Usuario> fetchAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM Usuario", Usuario.class).list();
        }
    }
}
