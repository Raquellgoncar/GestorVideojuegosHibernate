/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Usuario;
import Vista.ActualizarVideojuegoVista;
import Vista.EliminarVideojuegoVista;
import Vista.InsertarVideojuegoVista;
import Vista.MenuPrincipalVista;
import Vista.ModificarVideojuegosVista;

/**
 *
 * @author Raquel
 */
public class ModificarVideojuegosController {

    private Usuario usuario;
    private ModificarVideojuegosVista vista;

    public ModificarVideojuegosController(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setVista(ModificarVideojuegosVista vista) {
        this.vista = vista;
    }

    public void insertar() {
        new InsertarVideojuegoVista(usuario).setVisible(true);
        vista.dispose();
    }

    public void actualizar() {
        new ActualizarVideojuegoVista(usuario).setVisible(true);
        vista.dispose();
    }

    public void eliminar() {
        new EliminarVideojuegoVista(usuario).setVisible(true);
        vista.dispose();
    }

    public void volverMenu() {
        new MenuPrincipalVista(usuario).setVisible(true);
        vista.dispose();
    }
}

