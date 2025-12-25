/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.Videojuego;
import java.util.List;

/**
 *
 * @author Raquel
 */
public interface VideojuegoDAO {
    
    void insert(Videojuego v);

    void update(Videojuego v);

    void delete(int id);

    Videojuego fetchOne(int id);

    List<Videojuego> fetchAll();
}
