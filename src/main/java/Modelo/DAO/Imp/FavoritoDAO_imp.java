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
 *
 * @author Raquel
 */
public class FavoritoDAO_imp implements FavoritoDAO {

    @Override
    public void insert(Favorito f) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.save(f);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

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
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Favorito fetchOne(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Favorito.class, id);
        }
    }

    @Override
    public List<Favorito> fetchAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM Favorito", Favorito.class).list();
        }
    }

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
