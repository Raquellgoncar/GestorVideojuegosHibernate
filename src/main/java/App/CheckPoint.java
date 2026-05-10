/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import Vista.LoginVista;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.ResourceBundle;


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
        FlatLightLaf.setup();
        aplicarIdiomaBotones();
        SwingUtilities.invokeLater(() -> new LoginVista().setVisible(true));
    }

    /**
     * Aplica los textos de los botones de JOptionPane según el idioma activo.
     * Debe llamarse al arrancar y cada vez que cambie el idioma.
     */
    public static void aplicarIdiomaBotones() {
        ResourceBundle texts = ResourceBundle.getBundle("i18n.messages");
        UIManager.put("OptionPane.yesButtonText",    texts.getString("dialog.yes"));
        UIManager.put("OptionPane.noButtonText",     texts.getString("dialog.no"));
        UIManager.put("OptionPane.cancelButtonText", texts.getString("dialog.cancel"));
        UIManager.put("OptionPane.okButtonText",     texts.getString("dialog.ok"));
    }
}