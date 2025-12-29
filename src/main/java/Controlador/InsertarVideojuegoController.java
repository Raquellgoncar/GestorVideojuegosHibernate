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
 *
 * @author Raquel
 */
public class InsertarVideojuegoController {

    private Usuario usuario;
    private InsertarVideojuegoVista vista;

    private VideojuegoDAO videojuegoDAO;
    private FavoritoDAO favoritoDAO;

    public InsertarVideojuegoController(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
        this.favoritoDAO = new FavoritoDAO_imp();
    }

    public void setVista(InsertarVideojuegoVista vista) {
        this.vista = vista;
    }

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

    public void volverModificar() {
        new ModificarVideojuegosVista(usuario).setVisible(true);
        vista.dispose();
    }

    public Usuario getUsuario() {
        return usuario;
    }
}