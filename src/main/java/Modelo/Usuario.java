/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable; 
import java.util.Collection; 
import java.util.Date; 
import javax.persistence.Basic; 
import javax.persistence.CascadeType; 
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.NamedQueries; 
import javax.persistence.NamedQuery; 
import javax.persistence.OneToMany; 
import javax.persistence.Table; 
import javax.persistence.Temporal; 
import javax.persistence.TemporalType;

/**
* 
* @author Raquel 
*/
@Entity
@Table(name = "usuarios")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll",
                query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findById",
                query = "SELECT u FROM Usuario u WHERE u.id = :id"),
    @NamedQuery(name = "Usuario.findByUsername",
                query = "SELECT u FROM Usuario u WHERE u.username = :username"),
    @NamedQuery(name = "Usuario.findByPasswordHash",
                query = "SELECT u FROM Usuario u WHERE u.passwordHash = :passwordHash"),
    @NamedQuery(name = "Usuario.findByEmail",
                query = "SELECT u FROM Usuario u WHERE u.email = :email"),
    @NamedQuery(name = "Usuario.findByNombre",
                query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuario.findByUltimaConexion",
                query = "SELECT u FROM Usuario u WHERE u.ultimaConexion = :ultimaConexion")
})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "username")
    private String username;

    @Basic(optional = false)
    @Column(name = "password_hash")
    private String passwordHash;

    @Basic(optional = false)
    @Column(name = "email")
    private String email;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ultima_conexion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaConexion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private Collection<Favorito> favoritoCollection;

  

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(Integer id, String username, String passwordHash, String email) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getUltimaConexion() {
        return ultimaConexion;
    }

    public void setUltimaConexion(Date ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Modelo.Usuario[ id=" + id + " ]";
    }
}