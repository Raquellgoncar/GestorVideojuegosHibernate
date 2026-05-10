package Modelo.DAO.Imp;

import Modelo.DAO.VideojuegoDAO;
import Modelo.Videojuego;
import Modelo.util.HibernateUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Implementación DAO para la gestión de videojuegos mediante Hibernate.
 * <p>
 * Los favoritos ya no tienen tabla propia: se gestionan a través del campo
 * booleano {@code favorito} de la entidad {@code Videojuego}.
 * </p>
 *
 * @author Raquel
 * @version 2.0
 */
public class VideojuegoDAO_imp implements VideojuegoDAO {

    /**
     * Inserta un nuevo videojuego en la base de datos.
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
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Actualiza los datos de un videojuego existente.
     * <p>
     * Este método también persiste el cambio del campo {@code favorito},
     * por lo que marcar/desmarcar favorito simplemente requiere modificar
     * ese campo y llamar a {@code update}.
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
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Elimina un videojuego a partir de su identificador.
     *
     * @param id identificador del videojuego
     */
    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Videojuego v = s.get(Videojuego.class, id);
            if (v != null) s.delete(v);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un videojuego a partir de su identificador.
     *
     * @param id identificador del videojuego
     * @return videojuego o {@code null} si no existe
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
            return s.createQuery("FROM Videojuego v ORDER BY v.id DESC", Videojuego.class).list();
        }
    }

    /**
     * Obtiene los videojuegos asociados a un usuario concreto.
     *
     * @param idUsuario identificador del usuario
     * @return lista de videojuegos del usuario
     */
    @Override
    public List<Videojuego> fetchByUsuario(int idUsuario) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Videojuego v WHERE v.usuario.id = :idUsuario ORDER BY v.id DESC",
                    Videojuego.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        }
    }

    /**
     * Obtiene el número total de videojuegos registrados por un usuario.
     *
     * @param usuarioId identificador del usuario
     * @return número total de videojuegos del usuario
     */
    @Override
    public int countByUsuario(int usuarioId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Long total = s.createQuery(
                    "SELECT COUNT(v) FROM Videojuego v WHERE v.usuario.id = :id",
                    Long.class)
                    .setParameter("id", usuarioId)
                    .uniqueResult();
            return total != null ? total.intValue() : 0;
        }
    }

    /**
     * Obtiene los videojuegos marcados como favoritos por un usuario.
     * <p>
     * Filtra por el campo booleano {@code favorito} directamente en la tabla
     * {@code videojuegos}, sin necesidad de join con tabla externa.
     * </p>
     *
     * @param idUsuario identificador del usuario
     * @return lista de videojuegos favoritos del usuario
     */
    @Override
    public List<Videojuego> fetchFavoritosByUsuario(int idUsuario) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Videojuego v WHERE v.usuario.id = :idUsuario AND v.favorito = true ORDER BY v.id DESC",
                    Videojuego.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        }
    }

    /**
     * Obtiene los videojuegos de un usuario filtrados por género.
     *
     * @param idUsuario identificador del usuario
     * @param genero    género a filtrar
     * @return lista de videojuegos del usuario con ese género
     */
    @Override
    public List<Videojuego> fetchByGeneroAndUsuario(int idUsuario, String genero) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Videojuego v WHERE v.usuario.id = :idUsuario AND v.genero = :genero ORDER BY v.id DESC",
                    Videojuego.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("genero", genero)
                    .getResultList();
        }
    }

    /**
     * Obtiene el género más frecuente en la biblioteca de un usuario.
     * <p>
     * Devuelve {@code null} si no hay videojuegos con género registrado.
     * Se usa en el perfil para generar recomendaciones personalizadas.
     * </p>
     *
     * @param idUsuario identificador del usuario
     * @return género más jugado o {@code null}
     */
    @Override
    public String fetchGeneroMasJugado(int idUsuario) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> resultados = s.createQuery(
                    "SELECT v.genero, COUNT(v) as total FROM Videojuego v "
                    + "WHERE v.usuario.id = :idUsuario AND v.genero IS NOT NULL "
                    + "GROUP BY v.genero ORDER BY total DESC",
                    Object[].class)
                    .setParameter("idUsuario", idUsuario)
                    .setMaxResults(1)
                    .getResultList();

            if (!resultados.isEmpty()) {
                return (String) resultados.get(0)[0];
            }
            return null;
        }
    }

    /**
     * Obtiene el número de videojuegos registrados por plataforma para un usuario.
     * <p>
     * La consulta agrupa los videojuegos por el campo {@code plataforma}.
     * Si una plataforma no tiene videojuegos, no aparece en el gráfico porque
     * no existe ningún registro que contar.
     * </p>
     *
     * @param idUsuario identificador del usuario
     * @return mapa ordenado con plataforma y total de videojuegos
     */
    @Override
    public Map<String, Long> countPlataformasByUsuario(int idUsuario) {
        Map<String, Long> conteoPlataformas = new LinkedHashMap<>();

        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> resultados = s.createQuery(
                    "SELECT v.plataforma, COUNT(v) FROM Videojuego v "
                    + "WHERE v.usuario.id = :idUsuario "
                    + "AND v.plataforma IS NOT NULL "
                    + "GROUP BY v.plataforma "
                    + "ORDER BY COUNT(v) DESC",
                    Object[].class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();

            for (Object[] fila : resultados) {
                String plataforma = (String) fila[0];
                Long total = (Long) fila[1];

                if (plataforma != null && !plataforma.trim().isEmpty()) {
                    conteoPlataformas.put(plataforma, total);
                }
            }
        }

        return conteoPlataformas;
    }

}
