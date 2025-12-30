/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entidad que representa un videojuego marcado como favorito por un usuario.
 * <p>
 * Esta clase está mapeada a la tabla {@code favoritos} de la base de datos y
 * define la relación entre un usuario y un videojuego, incluyendo la fecha
 * en la que fue añadido como favorito.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
@Entity
@Table(name = "favoritos")
@NamedQueries({
    @NamedQuery(
        name = "Favorito.findAll",
        query = "SELECT f FROM Favorito f"
    ),
    @NamedQuery(
        name = "Favorito.findById",
        query = "SELECT f FROM Favorito f WHERE f.id = :id"
    ),
    @NamedQuery(
        name = "Favorito.findByFechaAnadido",
        query = "SELECT f FROM Favorito f WHERE f.fechaAnadido = :fechaAnadido"
    )
})
public class Favorito implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del favorito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    /**
     * Fecha en la que el videojuego fue añadido como favorito.
     */
    @Column(name = "fecha_anadido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnadido;

    /**
     * Usuario al que pertenece el favorito.
     */
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioId;

    /**
     * Videojuego marcado como favorito.
     */
    @JoinColumn(name = "videojuego_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Videojuego videojuegoId;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Favorito() {
    }

    /**
     * Constructor que inicializa el favorito con su identificador.
     *
     * @param id identificador del favorito
     */
    public Favorito(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el identificador del favorito.
     *
     * @return id del favorito
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del favorito.
     *
     * @param id nuevo identificador
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve la fecha en la que se añadió el favorito.
     *
     * @return fecha de añadido
     */
    public Date getFechaAnadido() {
        return fechaAnadido;
    }

    /**
     * Establece la fecha en la que se añadió el favorito.
     *
     * @param fechaAnadido fecha de añadido
     */
    public void setFechaAnadido(Date fechaAnadido) {
        this.fechaAnadido = fechaAnadido;
    }

    /**
     * Devuelve el usuario asociado al favorito.
     *
     * @return usuario del favorito
     */
    public Usuario getUsuarioId() {
        return usuarioId;
    }

    /**
     * Establece el usuario asociado al favorito.
     *
     * @param usuarioId usuario del favorito
     */
    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Devuelve el videojuego marcado como favorito.
     *
     * @return videojuego favorito
     */
    public Videojuego getVideojuegoId() {
        return videojuegoId;
    }

    /**
     * Establece el videojuego marcado como favorito.
     *
     * @param videojuegoId videojuego favorito
     */
    public void setVideojuegoId(Videojuego videojuegoId) {
        this.videojuegoId = videojuegoId;
    }

    /**
     * Calcula el código hash del favorito.
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
     * Compara este favorito con otro objeto.
     * <p>
     * Dos favoritos se consideran iguales si tienen el mismo identificador.
     * </p>
     *
     * @param object objeto a comparar
     * @return {@code true} si son iguales, {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Favorito)) {
            return false;
        }
        Favorito other = (Favorito) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Devuelve una representación en texto del favorito.
     *
     * @return representación en texto
     */
    @Override
    public String toString() {
        return "Modelo.Favorito[ id=" + id + " ]";
    }
}

