/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import Vista.LoginVista;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;


/**
 * Clase principal de la aplicación CheckPoint.
 * <p>
 * Esta clase contiene el método {@code main}, que actúa como punto de entrada
 * del programa. Se encarga de configurar el estilo visual de la aplicación
 * mediante FlatLaf y de lanzar la ventana de inicio de sesión.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class CheckPoint {

    /**
     * Método principal que inicia la ejecución de la aplicación.
     * <p>
     * Configura el tema visual FlatLightLaf y lanza la interfaz gráfica
     * asegurando que se ejecute en el hilo de eventos de Swing.
     * </p>
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Configuración del tema visual de la aplicación
        FlatLightLaf.setup();

        // Lanzamiento de la ventana de login en el hilo de Swing
        SwingUtilities.invokeLater(() -> new LoginVista().setVisible(true));
    }
}