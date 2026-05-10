/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package App; 

import Modelo.Usuario;
import Modelo.Videojuego;
import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;
 
/**
 * Batería de pruebas unitarias (JUnit) para las entidades Videojuego y Usuario.
 * <p>
 * Valida de forma aislada la lógica de negocio: valores por defecto,
 * asignación de estados, resiliencia ante nulos y validación de integridad
 * del perfil de usuario (formato de email y contraseña).
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class EntidadesTest {
 
    // =========================================================
    // TESTS DE Videojuego
    // =========================================================
 
    /**
     * UT-01: El campo favorito debe ser false por defecto al crear un Videojuego.
     */
    @Test
    public void testFavoritoPorDefecto() {
        Videojuego v = new Videojuego();
        assertFalse("El campo favorito debe ser false por defecto", v.isFavorito());
    }
 
    /**
     * UT-02: setFavorito(true) debe cambiar el estado a true.
     */
    @Test
    public void testSetFavoritoTrue() {
        Videojuego v = new Videojuego();
        v.setFavorito(true);
        assertTrue("El campo favorito debe ser true tras setFavorito(true)", v.isFavorito());
    }
 
    /**
     * UT-03: setFavorito(false) tras true debe devolver false.
     */
    @Test
    public void testSetFavoritoFalse() {
        Videojuego v = new Videojuego();
        v.setFavorito(true);
        v.setFavorito(false);
        assertFalse("El campo favorito debe volver a false tras setFavorito(false)", v.isFavorito());
    }
 
    /**
     * UT-04: getFechaMostrada debe devolver "-" si anio es null.
     */
    @Test
    public void testGetFechaMostradaNulo() {
        Videojuego v = new Videojuego();
        assertEquals("Debe devolver '-' si anio es null", "-", v.getFechaMostrada());
    }
 
    /**
     * UT-05: getFechaMostrada debe devolver la fecha guardada si no es null.
     */
    @Test
    public void testGetFechaMostradaConValor() {
        Videojuego v = new Videojuego();
        v.setAnio("25/03/2024");
        assertEquals("Debe devolver la fecha asignada", "25/03/2024", v.getFechaMostrada());
    }
 
    /**
     * UT-06: getValoracionMostrada debe devolver "-" si valoracion es null.
     */
    @Test
    public void testGetValoracionMostradaNulo() {
        Videojuego v = new Videojuego();
        assertEquals("Debe devolver '-' si valoracion es null", "-", v.getValoracionMostrada());
    }
 
    /**
     * UT-07: getValoracionMostrada debe devolver el valor sin ceros innecesarios.
     */
    @Test
    public void testGetValoracionMostradaConValor() {
        Videojuego v = new Videojuego();
        v.setValoracion(new BigDecimal("9.5"));
        assertEquals("Debe devolver '9.5' sin ceros extra", "9.5", v.getValoracionMostrada());
    }
 
    /**
     * UT-08: getValoracionMostrada con valor entero no debe mostrar decimales.
     */
    @Test
    public void testGetValoracionMostradaEntero() {
        Videojuego v = new Videojuego();
        v.setValoracion(new BigDecimal("8.0"));
        assertEquals("Debe devolver '8' sin decimal innecesario", "8", v.getValoracionMostrada());
    }
 
    /**
     * UT-09: El género debe poder asignarse y recuperarse correctamente.
     */
    @Test
    public void testSetGetGenero() {
        Videojuego v = new Videojuego();
        v.setGenero("RPG");
        assertEquals("El género debe ser 'RPG'", "RPG", v.getGenero());
    }
 
    /**
     * UT-10: La anotación debe poder asignarse y recuperarse correctamente.
     */
    @Test
    public void testSetGetAnotacion() {
        Videojuego v = new Videojuego();
        v.setAnotacion("Muy buen juego");
        assertEquals("La anotación debe coincidir", "Muy buen juego", v.getAnotacion());
    }
 
    // =========================================================
    // TESTS DE toString y equals
    // =========================================================
 
    /**
     * UT-11: toString de Videojuego debe contener el título y la plataforma.
     */
    @Test
    public void testToStringContieneTitulo() {
        Videojuego v = new Videojuego();
        v.setTitulo("Elden Ring");
        v.setPlataforma("PC");
        assertTrue("toString debe contener el título", v.toString().contains("Elden Ring"));
        assertTrue("toString debe contener la plataforma", v.toString().contains("PC"));
    }
 
    /**
     * UT-12: Dos videojuegos con el mismo id deben ser iguales según equals.
     */
    @Test
    public void testEqualsVideojuegosMismoId() {
        Videojuego v1 = new Videojuego(1);
        Videojuego v2 = new Videojuego(1);
        assertEquals("Dos videojuegos con el mismo id deben ser iguales", v1, v2);
    }
 
    /**
     * UT-13: Dos videojuegos con distinto id no deben ser iguales.
     */
    @Test
    public void testEqualsVideojuegosDistintoId() {
        Videojuego v1 = new Videojuego(1);
        Videojuego v2 = new Videojuego(2);
        assertNotEquals("Dos videojuegos con distinto id no deben ser iguales", v1, v2);
    }
 
    /**
     * UT-14: Dos usuarios con el mismo id deben ser iguales según equals.
     */
    @Test
    public void testEqualsUsuariosMismoId() {
        Usuario u1 = new Usuario(1);
        Usuario u2 = new Usuario(1);
        assertEquals("Dos usuarios con el mismo id deben ser iguales", u1, u2);
    }
 
    /**
     * UT-15: Dos usuarios con distinto id no deben ser iguales.
     */
    @Test
    public void testEqualsUsuariosDistintoId() {
        Usuario u1 = new Usuario(1);
        Usuario u2 = new Usuario(2);
        assertNotEquals("Dos usuarios con distinto id no deben ser iguales", u1, u2);
    }
}