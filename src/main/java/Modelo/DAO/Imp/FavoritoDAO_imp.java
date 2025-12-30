/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO.Imp;

import Modelo.Favorito;
import Modelo.DAO.FavoritoDAO;
import Modelo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * Implementación DAO para la gestión de favoritos mediante Hibernate.
 * <p>
 * Esta clase implementa la interfaz {@code FavoritoDAO} y proporciona
 * las operaciones necesarias para insertar, eliminar y consultar
 * favoritos en la base de datos utilizando sesiones Hibernate.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class FavoritoDAO_imp implements FavoritoDAO {

    /**
     * Inserta un nuevo favorito en la base de datos.
     * <p>
     * Abre una sesión Hibernate, inicia una transacción y guarda el
     * objeto {@code Favorito}. En caso de error, la transacción se revierte.
     * </p>
     *
     * @param f favorito a insertar
     */
    @Override
    public void insert(Favorito f) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.save(f);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Elimina un favorito a partir de su identificador.
     * <p>
     * Busca el favorito en la base de datos y, si existe,
     * lo elimina dentro de una transacción.
     * </p>
     *
     * @param id identificador del favorito
     */
    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Favorito f = s.get(Favorito.class, id);
            if (f != null) {
                s.delete(f);
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
     * Elimina un favorito a partir del usuario y el videojuego asociados.
     * <p>
     * Ejecuta una consulta HQL para borrar directamente el registro
     * que relaciona un usuario con un videojuego concreto.
     * </p>
     *
     * @param usuarioId identificador del usuario
     * @param videojuegoId identificador del videojuego
     */
    @Override
    public void deleteByUsuarioYVideojuego(int usuarioId, int videojuegoId) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();

            s.createQuery(
                    "DELETE FROM Favorito f WHERE f.usuarioId.id = :uid AND f.videojuegoId.id = :vid"
            )
                    .setParameter("uid", usuarioId)
                    .setParameter("vid", videojuegoId)
                    .executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un favorito a partir de su identificador.
     *
     * @param id identificador del favorito
     * @return favorito correspondiente al id o {@code null} si no existe
     */
    @Override
    public Favorito fetchOne(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Favorito.class, id);
        }
    }

    /**
     * Obtiene todos los favoritos registrados.
     *
     * @return lista de favoritos
     */
    @Override
    public List<Favorito> fetchAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM Favorito", Favorito.class).list();
        }
    }

    /**
     * Obtiene los favoritos asociados a un usuario concreto.
     *
     * @param usuarioId identificador del usuario
     * @return lista de favoritos del usuario
     */
    @Override
    public List<Favorito> fetchByUsuario(int usuarioId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Favorito f WHERE f.usuarioId.id = :id",
                    Favorito.class
            ).setParameter("id", usuarioId)
                    .list();
        }
    }
}
