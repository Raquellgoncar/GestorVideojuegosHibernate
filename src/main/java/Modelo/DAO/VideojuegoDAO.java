package Modelo.DAO;

import Modelo.Videojuego;
import java.util.List;
import java.util.Map;

/**
 * Interfaz DAO para la gestión de videojuegos.
 * <p>
 * Define las operaciones de acceso a datos relacionadas con los videojuegos.
 * La gestión de favoritos se realiza ahora a través del campo booleano
 * {@code favorito} de la propia entidad {@code Videojuego}, sin tabla separada.
 * </p>
 *
 * @author Raquel
 * @version 2.0
 */
public interface VideojuegoDAO {

    /**
     * Inserta un nuevo videojuego en la base de datos.
     *
     * @param v videojuego a insertar
     */
    void insert(Videojuego v);

    /**
     * Actualiza los datos de un videojuego existente.
     *
     * @param v videojuego con los datos actualizados
     */
    void update(Videojuego v);

    /**
     * Elimina un videojuego a partir de su identificador.
     *
     * @param id identificador del videojuego
     */
    void delete(int id);

    /**
     * Obtiene un videojuego a partir de su identificador.
     *
     * @param id identificador del videojuego
     * @return videojuego correspondiente al id
     */
    Videojuego fetchOne(int id);

    /**
     * Obtiene todos los videojuegos registrados.
     *
     * @return lista de videojuegos
     */
    List<Videojuego> fetchAll();

    /**
     * Obtiene los videojuegos asociados a un usuario concreto.
     *
     * @param idUsuario identificador del usuario
     * @return lista de videojuegos del usuario
     */
    List<Videojuego> fetchByUsuario(int idUsuario);

    /**
     * Obtiene el número total de videojuegos registrados por un usuario.
     *
     * @param usuarioId identificador del usuario
     * @return número total de videojuegos del usuario
     */
    int countByUsuario(int usuarioId);

    /**
     * Obtiene los videojuegos marcados como favoritos por un usuario.
     * <p>
     * Sustituye a la antigua consulta sobre la tabla {@code favoritos}.
     * Ahora filtra directamente por el campo booleano {@code favorito}
     * de la tabla {@code videojuegos}.
     * </p>
     *
     * @param idUsuario identificador del usuario
     * @return lista de videojuegos favoritos del usuario
     */
    List<Videojuego> fetchFavoritosByUsuario(int idUsuario);

    /**
     * Obtiene los videojuegos de un usuario filtrados por género.
     * <p>
     * Se usa en la pantalla de perfil para calcular el género más jugado
     * y generar recomendaciones personalizadas desde la API de RAWG.
     * </p>
     *
     * @param idUsuario identificador del usuario
     * @param genero    género por el que filtrar
     * @return lista de videojuegos del usuario con ese género
     */
    List<Videojuego> fetchByGeneroAndUsuario(int idUsuario, String genero);

    /**
     * Obtiene el género más frecuente en la biblioteca de un usuario.
     * <p>
     * Se utiliza para las recomendaciones del perfil. Devuelve {@code null}
     * si el usuario no tiene videojuegos con género registrado.
     * </p>
     *
     * @param idUsuario identificador del usuario
     * @return nombre del género más jugado o {@code null}
     */
    String fetchGeneroMasJugado(int idUsuario);

    /**
     * Obtiene el número de videojuegos registrados por plataforma para un usuario.
     * <p>
     * Solo devuelve plataformas que realmente existan en la biblioteca del usuario,
     * por lo que las plataformas sin juegos no aparecen en el resultado.
     * </p>
     *
     * @param idUsuario identificador del usuario
     * @return mapa con la plataforma y el número de videojuegos asociados
     */
    Map<String, Long> countPlataformasByUsuario(int idUsuario);
}
