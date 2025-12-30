/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.DAO.FavoritoDAO;
import Modelo.DAO.Imp.FavoritoDAO_imp;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Favorito;
import Modelo.Usuario;
import Modelo.Videojuego;
import Vista.InsertarVideojuegoVista;
import Vista.ModificarVideojuegosVista;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Controlador encargado de gestionar la inserción de nuevos videojuegos.
 * <p>
 * Se encarga de recoger los datos introducidos en la vista, crear el objeto
 * videojuego correspondiente y almacenarlo en la base de datos. También
 * gestiona la inserción opcional del videojuego como favorito.
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
     * DAO para el acceso a datos de favoritos.
     */
    private FavoritoDAO favoritoDAO;

    /**
     * Constructor del controlador.
     * <p>
     * Inicializa el usuario y crea las implementaciones de los DAO necesarios
     * para acceder a la base de datos.
     * </p>
     *
     * @param usuario usuario autenticado
     */
    public InsertarVideojuegoController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
        this.favoritoDAO = new FavoritoDAO_imp();
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
     * asocia al usuario actual. Si el parámetro {@code favorito} es verdadero,
     * también se crea un registro en la tabla de favoritos.
     * </p>
     *
     * @param titulo título del videojuego
     * @param plataforma plataforma en la que se juega
     * @param anio año de lanzamiento
     * @param valoracion valoración del videojuego
     * @param favorito indica si el videojuego se guarda como favorito
     */
    public void insertarVideojuego(
            String titulo,
            String plataforma,
            int anio,
            BigDecimal valoracion,
            boolean favorito
    ) {

        Videojuego v = new Videojuego();
        v.setTitulo(titulo);
        v.setPlataforma(plataforma);
        v.setAnio(anio);
        v.setValoracion(valoracion);
        v.setUsuario(usuario);

        videojuegoDAO.insert(v);

        if (favorito) {
            Favorito f = new Favorito();
            f.setUsuarioId(usuario);
            f.setVideojuegoId(v);
            f.setFechaAnadido(new Date());
            favoritoDAO.insert(f);
        }
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
