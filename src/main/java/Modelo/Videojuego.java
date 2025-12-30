/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entidad que representa un videojuego registrado por un usuario.
 * <p>
 * Esta clase está mapeada a la tabla {@code videojuegos} de la base de datos y
 * almacena la información básica de cada videojuego, así como su relación
 * con el usuario propietario y los posibles favoritos asociados.
 * </p>
 *
 * @author Raquel
 * @version 1.0
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
    )
})
public class Videojuego implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del videojuego.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    /**
     * Título del videojuego.
     */
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;

    /**
     * Plataforma en la que se juega el videojuego.
     */
    @Basic(optional = false)
    @Column(name = "plataforma")
    private String plataforma;

    /**
     * Año de lanzamiento del videojuego.
     */
    @Column(name = "anio")
    private Integer anio;

    /**
     * Valoración del videojuego.
     */
    @Column(name = "valoracion")
    private BigDecimal valoracion;

    /**
     * Usuario propietario del videojuego.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Colección de favoritos asociados a este videojuego.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "videojuegoId")
    private Collection<Favorito> favoritoCollection;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Videojuego() {
    }

    /**
     * Constructor que inicializa el videojuego con su identificador.
     *
     * @param id identificador del videojuego
     */
    public Videojuego(Integer id) {
        this.id = id;
    }

    /**
     * Constructor que inicializa los datos básicos del videojuego.
     *
     * @param id identificador del videojuego
     * @param titulo título del videojuego
     * @param plataforma plataforma del videojuego
     */
    public Videojuego(Integer id, String titulo, String plataforma) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
    }

    /**
     * Devuelve el identificador del videojuego.
     *
     * @return id del videojuego
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del videojuego.
     *
     * @param id nuevo identificador
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el título del videojuego.
     *
     * @return título del videojuego
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del videojuego.
     *
     * @param titulo título del videojuego
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Devuelve la plataforma del videojuego.
     *
     * @return plataforma
     */
    public String getPlataforma() {
        return plataforma;
    }

    /**
     * Establece la plataforma del videojuego.
     *
     * @param plataforma plataforma
     */
    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    /**
     * Devuelve el año de lanzamiento del videojuego.
     *
     * @return año de lanzamiento
     */
    public Integer getAnio() {
        return anio;
    }

    /**
     * Establece el año de lanzamiento del videojuego.
     *
     * @param anio año de lanzamiento
     */
    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    /**
     * Devuelve la valoración del videojuego.
     *
     * @return valoración
     */
    public BigDecimal getValoracion() {
        return valoracion;
    }

    /**
     * Establece la valoración del videojuego.
     *
     * @param valoracion valoración
     */
    public void setValoracion(BigDecimal valoracion) {
        this.valoracion = valoracion;
    }

    /**
     * Devuelve el usuario propietario del videojuego.
     *
     * @return usuario propietario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario propietario del videojuego.
     *
     * @param usuario usuario propietario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Devuelve la colección de favoritos asociados al videojuego.
     *
     * @return colección de favoritos
     */
    public Collection<Favorito> getFavoritoCollection() {
        return favoritoCollection;
    }

    /**
     * Establece la colección de favoritos del videojuego.
     *
     * @param favoritoCollection colección de favoritos
     */
    public void setFavoritoCollection(Collection<Favorito> favoritoCollection) {
        this.favoritoCollection = favoritoCollection;
    }

    /**
     * Calcula el código hash del videojuego.
     *
     * @return valor hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compara este videojuego con otro objeto.
     * <p>
     * Dos videojuegos se consideran iguales si tienen el mismo identificador.
     * </p>
     *
     * @param object objeto a comparar
     * @return {@code true} si son iguales, {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Videojuego)) {
            return false;
        }
        Videojuego other = (Videojuego) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Devuelve una representación en texto del videojuego.
     *
     * @return representación en texto
     */
    @Override
    public String toString() {
        return titulo + " | "
                + plataforma + " | "
                + anio + " | " + valoracion;
    }
}
