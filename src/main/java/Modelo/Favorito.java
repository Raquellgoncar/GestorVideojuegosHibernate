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
 *
 * @author Raquel
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha_anadido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnadido;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioId;
    @JoinColumn(name = "videojuego_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Videojuego videojuegoId;

    public Favorito() {
    }

    public Favorito(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaAnadido() {
        return fechaAnadido;
    }

    public void setFechaAnadido(Date fechaAnadido) {
        this.fechaAnadido = fechaAnadido;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Videojuego getVideojuegoId() {
        return videojuegoId;
    }

    public void setVideojuegoId(Videojuego videojuegoId) {
        this.videojuegoId = videojuegoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Favorito)) {
            return false;
        }
        Favorito other = (Favorito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Favorito[ id=" + id + " ]";
    }

}
