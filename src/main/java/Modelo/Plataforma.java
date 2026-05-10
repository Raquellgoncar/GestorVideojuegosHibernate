package Modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidad que representa una plataforma de videojuegos.
 * <p>
 * Mapeada a la tabla {@code plataformas}. Actúa como catálogo de valores
 * para el desplegable de registro y actualización de videojuegos.
 * El campo {@code rawgSlug} es el identificador que usa la API de RAWG
 * para filtrar juegos por plataforma.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
@Entity
@Table(name = "plataformas")
@NamedQueries({
    @NamedQuery(
        name = "Plataforma.findAll",
        query = "SELECT p FROM Plataforma p ORDER BY p.nombre ASC"
    ),
    @NamedQuery(
        name = "Plataforma.findById",
        query = "SELECT p FROM Plataforma p WHERE p.id = :id"
    ),
    @NamedQuery(
        name = "Plataforma.findByNombre",
        query = "SELECT p FROM Plataforma p WHERE p.nombre = :nombre"
    ),
    @NamedQuery(
        name = "Plataforma.findBySlug",
        query = "SELECT p FROM Plataforma p WHERE p.rawgSlug = :rawgSlug"
    )
})
public class Plataforma implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador único de la plataforma. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    /**
     * Nombre visible de la plataforma (ej: "PlayStation 5").
     * Es el valor que se muestra en el desplegable.
     */
    @Basic(optional = false)
    @Column(name = "nombre", unique = true)
    private String nombre;

    /**
     * Identificador de la plataforma en la API de RAWG.
     * Se usa para construir las llamadas al catálogo externo.
     * Ejemplo: "playstation5", "pc", "nintendo-switch".
     * Puede ser null si la plataforma no existe en RAWG.
     */
    @Column(name = "rawg_slug")
    private String rawgSlug;

    /** Constructor vacío requerido por JPA. */
    public Plataforma() {
    }

    /**
     * Constructor con identificador.
     *
     * @param id identificador de la plataforma
     */
    public Plataforma(Integer id) {
        this.id = id;
    }

    /**
     * Constructor completo.
     *
     * @param id       identificador de la plataforma
     * @param nombre   nombre visible de la plataforma
     * @param rawgSlug slug de la plataforma en RAWG
     */
    public Plataforma(Integer id, String nombre, String rawgSlug) {
        this.id = id;
        this.nombre = nombre;
        this.rawgSlug = rawgSlug;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRawgSlug() { return rawgSlug; }
    public void setRawgSlug(String rawgSlug) { this.rawgSlug = rawgSlug; }

    // -------------------------------------------------------------------------
    // equals, hashCode, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Plataforma)) return false;
        Plataforma other = (Plataforma) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Devuelve el nombre de la plataforma.
     * Se usa directamente como texto en los JComboBox.
     *
     * @return nombre de la plataforma
     */
    @Override
    public String toString() {
        return nombre;
    }
}
