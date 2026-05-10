package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidad que representa un videojuego registrado por un usuario.
 * <p>
 * Mapeada a la tabla {@code videojuegos}. El campo {@code favorito} sustituye
 * a la antigua tabla separada de favoritos, simplificando el modelo de datos.
 * Los campos {@code genero} e {@code imagenUrl} se rellenan automáticamente
 * cuando el juego se añade desde la API de RAWG.
 * </p>
 *
 * @author Raquel
 * @version 2.0
 */
@Entity
@Table(name = "videojuegos")
@NamedQueries({
    @NamedQuery(
        name = "Videojuego.findAll",
        query = "SELECT v FROM Videojuego v"
    ),
    @NamedQuery(
        name = "Videojuego.findById",
        query = "SELECT v FROM Videojuego v WHERE v.id = :id"
    ),
    @NamedQuery(
        name = "Videojuego.findByTitulo",
        query = "SELECT v FROM Videojuego v WHERE v.titulo = :titulo"
    ),
    @NamedQuery(
        name = "Videojuego.findByPlataforma",
        query = "SELECT v FROM Videojuego v WHERE v.plataforma = :plataforma"
    ),
    @NamedQuery(
        name = "Videojuego.findByAnio",
        query = "SELECT v FROM Videojuego v WHERE v.anio = :anio"
    ),
    @NamedQuery(
        name = "Videojuego.findByValoracion",
        query = "SELECT v FROM Videojuego v WHERE v.valoracion = :valoracion"
    ),
    @NamedQuery(
        name = "Videojuego.findFavoritosByUsuario",
        query = "SELECT v FROM Videojuego v WHERE v.usuario.id = :idUsuario AND v.favorito = true"
    ),
    @NamedQuery(
        name = "Videojuego.findByGeneroAndUsuario",
        query = "SELECT v FROM Videojuego v WHERE v.usuario.id = :idUsuario AND v.genero = :genero"
    )
})
public class Videojuego implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    /** Identificador único del videojuego. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    /** Título del videojuego. */
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;

    /**
     * Nombre de la plataforma en la que se juega.
     * Puede ser un valor elegido del catálogo o introducido manualmente.
     */
    @Basic(optional = false)
    @Column(name = "plataforma")
    private String plataforma;

    /** Año de lanzamiento del videojuego. */
    @Column(name = "anio")
    private String anio;

    /** Valoración personal del usuario (1-10). */
    @Column(name = "valoracion")
    private BigDecimal valoracion;

    /**
     * Indica si el videojuego está marcado como favorito por el usuario.
     * Sustituye a la antigua tabla separada {@code favoritos}.
     */
    @Basic(optional = false)
    @Column(name = "favorito", nullable = false)
    private boolean favorito = false;

    /**
     * Género del videojuego.
     * Se rellena automáticamente desde la API de RAWG cuando el usuario
     * elige el juego del catálogo. Puede quedar vacío en registros manuales.
     */
    @Column(name = "genero")
    private String genero;

    /**
     * URL de la imagen/portada del juego.
     * Se rellena automáticamente desde la API de RAWG cuando el usuario
     * elige el juego del catálogo. Puede quedar vacío en registros manuales.
     */
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    /** Anotaciones personales privadas del usuario sobre el videojuego. */
    @Column(name = "anotacion", columnDefinition = "TEXT")
    private String anotacion;

    /** Usuario propietario del videojuego. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Constructor vacío requerido por JPA. */
    public Videojuego() {
    }

    /**
     * Constructor con identificador.
     *
     * @param id identificador del videojuego
     */
    public Videojuego(Integer id) {
        this.id = id;
    }

    /**
     * Constructor con los campos básicos obligatorios.
     *
     * @param id         identificador del videojuego
     * @param titulo     título del videojuego
     * @param plataforma plataforma del videojuego
     */
    public Videojuego(Integer id, String titulo, String plataforma) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public String getAnio() { return anio; }
    public void setAnio(String anio) { this.anio = anio; }

    public String getFechaMostrada() {
        return formatearFechaMostrada(anio);
    }

    private String formatearFechaMostrada(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) return "-";

        String valor = fecha.trim();

        if (valor.matches("\\d{2}/\\d{2}/\\d{4}")) {
            try {
                return LocalDate.parse(valor, FORMATO_FECHA).format(FORMATO_FECHA);
            } catch (DateTimeParseException ignored) {
                return "-";
            }
        }

        return "-";
    }

    public BigDecimal getValoracion() { return valoracion; }
    public void setValoracion(BigDecimal valoracion) { this.valoracion = valoracion; }

    public String getValoracionMostrada() {
        return valoracion != null ? valoracion.stripTrailingZeros().toPlainString() : "-";
    }

    /**
     * Indica si el videojuego está marcado como favorito.
     *
     * @return {@code true} si es favorito, {@code false} en caso contrario
     */
    public boolean isFavorito() { return favorito; }

    /**
     * Marca o desmarca el videojuego como favorito.
     *
     * @param favorito {@code true} para marcarlo, {@code false} para desmarcarlo
     */
    public void setFavorito(boolean favorito) { this.favorito = favorito; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getAnotacion() { return anotacion; }
    public void setAnotacion(String anotacion) { this.anotacion = anotacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    // -------------------------------------------------------------------------
    // equals, hashCode, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Videojuego)) return false;
        Videojuego other = (Videojuego) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return titulo + " | " + plataforma + " | " + getFechaMostrada() + " | " + getValoracionMostrada()
                + (favorito ? " ★" : "");
    }
}
