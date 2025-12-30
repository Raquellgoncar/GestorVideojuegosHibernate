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
 * Entidad que representa a un usuario de la aplicación.
 * <p>
 * Esta clase está mapeada a la tabla {@code usuarios} de la base de datos y
 * almacena la información necesaria para la autenticación, identificación
 * y gestión del perfil del usuario.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
@NamedQueries({
    @NamedQuery(
        name = "Usuario.findAll",
        query = "SELECT u FROM Usuario u"
    ),
    @NamedQuery(
        name = "Usuario.findById",
        query = "SELECT u FROM Usuario u WHERE u.id = :id"
    ),
    @NamedQuery(
        name = "Usuario.findByUsername",
        query = "SELECT u FROM Usuario u WHERE u.username = :username"
    ),
    @NamedQuery(
        name = "Usuario.findByPasswordHash",
        query = "SELECT u FROM Usuario u WHERE u.passwordHash = :passwordHash"
    ),
    @NamedQuery(
        name = "Usuario.findByEmail",
        query = "SELECT u FROM Usuario u WHERE u.email = :email"
    ),
    @NamedQuery(
        name = "Usuario.findByNombre",
        query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"
    ),
    @NamedQuery(
        name = "Usuario.findByUltimaConexion",
        query = "SELECT u FROM Usuario u WHERE u.ultimaConexion = :ultimaConexion"
    )
})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    /**
     * Nombre de usuario utilizado para el inicio de sesión.
     */
    @Basic(optional = false)
    @Column(name = "username")
    private String username;

    /**
     * Contraseña del usuario almacenada en formato hash.
     */
    @Basic(optional = false)
    @Column(name = "password_hash")
    private String passwordHash;

    /**
     * Dirección de correo electrónico del usuario.
     */
    @Basic(optional = false)
    @Column(name = "email")
    private String email;

    /**
     * Nombre real del usuario.
     */
    @Column(name = "nombre")
    private String nombre;

    /**
     * Fecha y hora de la última conexión del usuario.
     */
    @Column(name = "ultima_conexion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaConexion;

    /**
     * Colección de videojuegos marcados como favoritos por el usuario.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private Collection<Favorito> favoritoCollection;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Usuario() {
    }

    /**
     * Constructor que inicializa el usuario con su identificador.
     *
     * @param id identificador del usuario
     */
    public Usuario(Integer id) {
        this.id = id;
    }

    /**
     * Constructor que inicializa los datos principales del usuario.
     *
     * @param id identificador del usuario
     * @param username nombre de usuario
     * @param passwordHash contraseña cifrada mediante hash
     * @param email correo electrónico del usuario
     */
    public Usuario(Integer id, String username, String passwordHash, String email) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    /**
     * Devuelve el identificador del usuario.
     *
     * @return id del usuario
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param id nuevo identificador
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de usuario.
     *
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve la contraseña cifrada del usuario.
     *
     * @return hash de la contraseña
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Establece la contraseña cifrada del usuario.
     *
     * @param passwordHash hash de la contraseña
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Devuelve el correo electrónico del usuario.
     *
     * @return email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve el nombre real del usuario.
     *
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre real del usuario.
     *
     * @param nombre nombre real
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la fecha de la última conexión del usuario.
     *
     * @return fecha de última conexión
     */
    public Date getUltimaConexion() {
        return ultimaConexion;
    }

    /**
     * Establece la fecha de la última conexión del usuario.
     *
     * @param ultimaConexion fecha de última conexión
     */
    public void setUltimaConexion(Date ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    /**
     * Devuelve la colección de favoritos del usuario.
     *
     * @return colección de favoritos
     */
    public Collection<Favorito> getFavoritoCollection() {
        return favoritoCollection;
    }

    /**
     * Establece la colección de favoritos del usuario.
     *
     * @param favoritoCollection colección de favoritos
     */
    public void setFavoritoCollection(Collection<Favorito> favoritoCollection) {
        this.favoritoCollection = favoritoCollection;
    }

    /**
     * Calcula el código hash del usuario.
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
     * Compara este usuario con otro objeto.
     * <p>
     * Dos usuarios se consideran iguales si tienen el mismo identificador.
     * </p>
     *
     * @param object objeto a comparar
     * @return {@code true} si son iguales, {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Devuelve una representación en texto del usuario.
     *
     * @return representación en texto
     */
    @Override
    public String toString() {
        return "Modelo.Usuario[ id=" + id + " ]";
    }
}
