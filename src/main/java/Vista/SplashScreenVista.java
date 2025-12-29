/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Usuario;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 *
 * @author Raquel
 */
public class SplashScreenVista extends JWindow {

    private Image fondo;
    private Usuario usuario;

    public SplashScreenVista(Usuario usuario) {
        this.usuario = usuario;

        var url = getClass().getResource("/img/imagenFondo.png");
        if (url != null) {
            fondo = new ImageIcon(url).getImage();
        }

        setSize(700, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (fondo != null) {
                    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                }

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                );

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Wide Latin", Font.PLAIN, 42));

                String texto = "CheckPoint";
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(texto)) / 2;
                int y = getHeight() / 2;

                g2.drawString(texto, x, y);
            }
        };

        setContentPane(panel);

        Timer timer = new Timer(2500, e -> {
            new MenuPrincipalVista(usuario).setVisible(true);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
