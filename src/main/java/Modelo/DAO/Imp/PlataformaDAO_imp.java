package Modelo.DAO.Imp;

import Modelo.DAO.PlataformaDAO;
import Modelo.Plataforma;
import Modelo.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 * Implementación DAO para la gestión de plataformas mediante Hibernate.
 * <p>
 * La tabla {@code plataformas} es un catálogo de solo lectura desde
 * la aplicación: los datos se insertan una sola vez mediante el script
 * SQL de creación de la base de datos y no se modifican en tiempo de ejecución.
 * Por eso esta implementación solo ofrece métodos de consulta.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class PlataformaDAO_imp implements PlataformaDAO {

    /**
     * Obtiene todas las plataformas ordenadas alfabéticamente.
     *
     * @return lista de plataformas
     */
    @Override
    public List<Plataforma> fetchAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Plataforma p ORDER BY p.nombre ASC",
                    Plataforma.class
            ).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene una plataforma a partir de su identificador.
     *
     * @param id identificador de la plataforma
     * @return plataforma o {@code null} si no existe
     */
    @Override
    public Plataforma fetchOne(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Plataforma.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una plataforma a partir de su nombre exacto.
     *
     * @param nombre nombre de la plataforma
     * @return plataforma o {@code null} si no existe
     */
    @Override
    public Plataforma fetchByNombre(String nombre) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            List<Plataforma> resultado = s.createQuery(
                    "FROM Plataforma p WHERE p.nombre = :nombre",
                    Plataforma.class
            )
                    .setParameter("nombre", nombre)
                    .getResultList();

            return resultado.isEmpty() ? null : resultado.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene directamente el slug de RAWG a partir del nombre de la plataforma.
     * <p>
     * Devuelve {@code null} si la plataforma no existe o no tiene slug asignado.
     * </p>
     *
     * @param nombre nombre de la plataforma
     * @return slug de RAWG o {@code null}
     */
    @Override
    public String fetchSlugByNombre(String nombre) {
        Plataforma p = fetchByNombre(nombre);
        if (p == null) return null;
        return p.getRawgSlug();
    }

    /**
     * Obtiene solo los nombres de todas las plataformas ordenados alfabéticamente.
     * <p>
     * Útil para poblar directamente un {@code JComboBox<String>}.
     * </p>
     *
     * @return lista de nombres de plataformas
     */
    @Override
    public List<String> fetchNombres() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "SELECT p.nombre FROM Plataforma p ORDER BY p.nombre ASC",
                    String.class
            ).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
