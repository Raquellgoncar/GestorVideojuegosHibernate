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
 *
 * @author Raquel
 */
public class VideojuegoDAO_imp implements VideojuegoDAO {

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

    @Override
    public Videojuego fetchOne(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Videojuego.class, id);
        }
    }

    @Override
    public List<Videojuego> fetchAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM Videojuego", Videojuego.class).list();
        }
    }

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

}
