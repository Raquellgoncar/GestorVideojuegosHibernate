/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.DAO;

import Modelo.Favorito;
import java.util.List;

/**
 *
 * @author Raquel
 */
public interface FavoritoDAO {

    void insert(Favorito f);

    void delete(int id);
    
    void deleteByUsuarioYVideojuego(int usuarioId, int videojuegoId);

    Favorito fetchOne(int id);

    List<Favorito> fetchAll();

    List<Favorito> fetchByUsuario(int usuarioId);
}
