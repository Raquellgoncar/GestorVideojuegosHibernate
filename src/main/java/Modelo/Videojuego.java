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
 *
 * @author Raquel
 */
@Entity
@Table(name = "videojuegos")
@NamedQueries({
    @NamedQuery(name = "Videojuego.findAll",
            query = "SELECT v FROM Videojuego v"),
    @NamedQuery(name = "Videojuego.findById",
            query = "SELECT v FROM Videojuego v WHERE v.id = :id"),
    @NamedQuery(name = "Videojuego.findByTitulo",
            query = "SELECT v FROM Videojuego v WHERE v.titulo = :titulo"),
    @NamedQuery(name = "Videojuego.findByPlataforma",
            query = "SELECT v FROM Videojuego v WHERE v.plataforma = :plataforma"),
    @NamedQuery(name = "Videojuego.findByAnio",
            query = "SELECT v FROM Videojuego v WHERE v.anio = :anio"),
    @NamedQuery(name = "Videojuego.findByValoracion",
            query = "SELECT v FROM Videojuego v WHERE v.valoracion = :valoracion")
})
public class Videojuego implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;

    @Basic(optional = false)
    @Column(name = "plataforma")
    private String plataforma;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "valoracion")
    private BigDecimal valoracion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "videojuegoId")
    private Collection<Favorito> favoritoCollection;

    public Videojuego() {
    }

    public Videojuego(Integer id) {
        this.id = id;
    }

    public Videojuego(Integer id, String titulo, String plataforma) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getValoracion() {
        return valoracion;
    }

    public void setValoracion(BigDecimal valoracion) {
        this.valoracion = valoracion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Collection<Favorito> getFavoritoCollection() {
        return favoritoCollection;
    }

    public void setFavoritoCollection(Collection<Favorito> favoritoCollection) {
        this.favoritoCollection = favoritoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Videojuego)) {
            return false;
        }
        Videojuego other = (Videojuego) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return titulo + " | "
                + plataforma + " | "
                + anio + " | " + valoracion;
    }
}
