/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO.Imp;

import Modelo.DAO.VideojuegoDAO;
import Modelo.Videojuego;
import Modelo.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Implementación DAO para la gestión de videojuegos mediante Hibernate.
 * <p>
 * Esta clase implementa la interfaz {@code VideojuegoDAO} y proporciona las
 * operaciones necesarias para insertar, actualizar, eliminar y consultar
 * videojuegos en la base de datos utilizando Hibernate.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class VideojuegoDAO_imp implements VideojuegoDAO {

    /**
     * Inserta un nuevo videojuego en la base de datos.
     * <p>
     * Abre una sesión Hibernate, inicia una transacción y guarda el objeto
     * {@code Videojuego}. En caso de error, la transacción se revierte.
     * </p>
     *
     * @param v videojuego a insertar
     */
    @Override
    public void insert(Videojuego v) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.save(v);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Actualiza los datos de un videojuego existente.
     * <p>
     * Guarda los cambios del videojuego en la base de datos dentro de una
     * transacción Hibernate.
     * </p>
     *
     * @param v videojuego con los datos actualizados
     */
    @Override
    public void update(Videojuego v) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.update(v);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Elimina un videojuego a partir de su identificador.
     * <p>
     * Busca el videojuego en la base de datos y, si existe, lo elimina dentro
     * de una transacción.
     * </p>
     *
     * @param id identificador del videojuego
     */
    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Videojuego v = s.get(Videojuego.class, id);
            if (v != null) {
                s.delete(v);
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
     * Obtiene un videojuego a partir de su identificador.
     *
     * @param id identificador del videojuego
     * @return videojuego correspondiente al id o {@code null} si no existe
     */
    @Override
    public Videojuego fetchOne(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Videojuego.class, id);
        }
    }

    /**
     * Obtiene todos los videojuegos registrados.
     *
     * @return lista de videojuegos
     */
    @Override
    public List<Videojuego> fetchAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM Videojuego", Videojuego.class).list();
        }
    }

    /**
     * Obtiene los videojuegos asociados a un usuario concreto.
     * <p>
     * Ejecuta una consulta HQL filtrando por el identificador del usuario
     * propietario del videojuego.
     * </p>
     *
     * @param idUsuario identificador del usuario
     * @return lista de videojuegos del usuario
     */
    @Override
    public List<Videojuego> fetchByUsuario(int idUsuario) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Videojuego> lista = session.createQuery(
                "FROM Videojuego v WHERE v.usuario.id = :idUsuario",
                Videojuego.class
        )
                .setParameter("idUsuario", idUsuario)
                .getResultList();

        session.close();
        return lista;
    }

    /**
     * Obtiene el número total de videojuegos registrados por un usuario
     * concreto.
     * <p>
     * Ejecuta una consulta HQL utilizando la función COUNT para contabilizar
     * únicamente los videojuegos asociados al usuario indicado.
     * </p>
     *
     * @param usuarioId identificador del usuario
     * @return número total de videojuegos registrados por el usuario
     */
    @Override
    public int countByUsuario(int usuarioId) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Long total = session.createQuery(
                "SELECT COUNT(v) FROM Videojuego v WHERE v.usuario.id = :id",
                Long.class
        )
                .setParameter("id", usuarioId)
                .uniqueResult();

        session.close();

        return total != null ? total.intValue() : 0;
    }

}
