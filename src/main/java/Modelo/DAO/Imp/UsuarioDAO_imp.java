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
 *
 * @author Raquel
 */
public class UsuarioDAO_imp implements UsuarioDAO {

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

    @Override
    public Usuario fetchOne(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Usuario.class, id);
        }
    }

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

    @Override
    public List<Usuario> fetchAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM Usuario", Usuario.class).list();
        }
    }
}
