package Modelo.DAO;

import Modelo.Plataforma;
import java.util.List;

/**
 * Interfaz DAO para la gestión de plataformas.
 * <p>
 * Define las operaciones de acceso a datos sobre la tabla {@code plataformas},
 * que actúa como catálogo de valores para los desplegables de la aplicación
 * y como fuente de slugs para las llamadas a la API de RAWG.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public interface PlataformaDAO {

    /**
     * Obtiene todas las plataformas ordenadas alfabéticamente.
     * <p>
     * Se usa para poblar los desplegables de inserción y actualización
     * de videojuegos.
     * </p>
     *
     * @return lista de todas las plataformas
     */
    List<Plataforma> fetchAll();

    /**
     * Obtiene una plataforma a partir de su identificador.
     *
     * @param id identificador de la plataforma
     * @return plataforma correspondiente o {@code null} si no existe
     */
    Plataforma fetchOne(int id);

    /**
     * Obtiene una plataforma a partir de su nombre exacto.
     * <p>
     * Se usa para recuperar el slug de RAWG cuando el usuario
     * selecciona una plataforma en el desplegable.
     * </p>
     *
     * @param nombre nombre de la plataforma (ej: "PlayStation 5")
     * @return plataforma correspondiente o {@code null} si no existe
     */
    Plataforma fetchByNombre(String nombre);

    /**
     * Obtiene directamente el slug de RAWG a partir del nombre de la plataforma.
     * <p>
     * Método de conveniencia para las llamadas a la API de RAWG.
     * Devuelve {@code null} si la plataforma no existe o no tiene slug.
     * </p>
     *
     * @param nombre nombre de la plataforma (ej: "Nintendo Switch")
     * @return slug de RAWG (ej: "nintendo-switch") o {@code null}
     */
    String fetchSlugByNombre(String nombre);

    /**
     * Obtiene solo los nombres de todas las plataformas ordenados alfabéticamente.
     * <p>
     * Útil para poblar un {@code JComboBox<String>} sin necesidad de
     * trabajar con objetos {@code Plataforma}.
     * </p>
     *
     * @return lista de nombres de plataformas
     */
    List<String> fetchNombres();
}
