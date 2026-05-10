/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Usuario;
import Modelo.Videojuego;
import Vista.InsertarVideojuegoVista;
import Vista.ModificarVideojuegosVista;
import java.math.BigDecimal;

/**
 * Controlador encargado de gestionar la inserción de nuevos videojuegos.
 * <p>
 * Se encarga de recoger los datos introducidos en la vista, crear el objeto
 * videojuego correspondiente y almacenarlo en la base de datos. El campo
 * favorito se gestiona directamente en la entidad {@code Videojuego}.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class InsertarVideojuegoController {

    /**
     * Usuario autenticado que realiza las operaciones.
     */
    private Usuario usuario;

    /**
     * Vista asociada a la inserción de videojuegos.
     */
    private InsertarVideojuegoVista vista;

    /**
     * DAO para el acceso a datos de videojuegos.
     */
    private VideojuegoDAO videojuegoDAO;

    /**
     * Constructor del controlador.
     * <p>
     * Inicializa el usuario y crea la implementación del DAO necesario
     * para acceder a la base de datos.
     * </p>
     *
     * @param usuario usuario autenticado
     */
    public InsertarVideojuegoController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
    }

    /**
     * Asocia la vista de inserción de videojuegos al controlador.
     *
     * @param vista vista de inserción
     */
    public void setVista(InsertarVideojuegoVista vista) {
        this.vista = vista;
    }

    /**
     * Inserta un nuevo videojuego en la base de datos.
     * <p>
     * Crea el objeto {@code Videojuego} con los datos proporcionados y lo
     * asocia al usuario actual. El campo {@code favorito} se guarda
     * directamente en la entidad, sin necesidad de tabla separada.
     * </p>
     *
     * @param titulo      título del videojuego
     * @param plataforma  plataforma en la que se juega
     * @param anio        año de lanzamiento del videojuego
     * @param valoracion  valoración personal del usuario
     * @param anotacion   anotaciones personales del usuario
     * @param favorito    indica si el videojuego se marca como favorito
     */
    public void insertarVideojuego(
            String titulo,
            String plataforma,
            String anio,
            BigDecimal valoracion,
            String anotacion,
            boolean favorito
    ) {
        Videojuego v = new Videojuego();
        v.setTitulo(titulo);
        v.setPlataforma(plataforma);
        v.setAnio(anio);
        v.setValoracion(valoracion);
        v.setAnotacion(anotacion);
        v.setFavorito(favorito);
        v.setUsuario(usuario);

        videojuegoDAO.insert(v);
    }

    /**
     * Inserta un nuevo videojuego con datos provenientes de la API de RAWG.
     * <p>
     * Además de los campos básicos, almacena el género y la URL de la imagen
     * obtenidos automáticamente del catálogo externo.
     * </p>
     *
     * @param titulo      título del videojuego
     * @param plataforma  plataforma en la que se juega
     * @param anio        año de lanzamiento del videojuego
     * @param valoracion  valoración personal del usuario
     * @param anotacion   anotaciones personales del usuario
     * @param favorito    indica si el videojuego se marca como favorito
     * @param genero      género obtenido de la API RAWG
     * @param imagenUrl   URL de la portada obtenida de la API RAWG
     */
    public void insertarVideojuegoDesdeAPI(
            String titulo,
            String plataforma,
            String anio,
            BigDecimal valoracion,
            String anotacion,
            boolean favorito,
            String genero,
            String imagenUrl
    ) {
        Videojuego v = new Videojuego();
        v.setTitulo(titulo);
        v.setPlataforma(plataforma);
        v.setAnio(anio);
        v.setValoracion(valoracion);
        v.setAnotacion(anotacion);
        v.setFavorito(favorito);
        v.setGenero(genero);
        v.setImagenUrl(imagenUrl);
        v.setUsuario(usuario);

        videojuegoDAO.insert(v);
    }

    /**
     * Vuelve a la vista de modificación de videojuegos.
     * <p>
     * Abre la ventana de modificación y cierra la vista actual.
     * </p>
     */
    public void volverModificar() {
        new ModificarVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }

    /**
     * Devuelve el usuario asociado al controlador.
     *
     * @return usuario actual
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
