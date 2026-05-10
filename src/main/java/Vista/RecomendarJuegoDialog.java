package Vista;

import Controlador.PerfilController;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Diálogo que permite al usuario elegir el criterio de recomendación de juegos.
 * <p>
 * Presenta dos opciones en forma de tarjetas:
 * <ul>
 *   <li>Por plataforma más jugada: recomienda juegos de la plataforma
 *       en la que el usuario tiene más títulos registrados.</li>
 *   <li>Por género más jugado: recomienda juegos del género que más
 *       aparece en la biblioteca del usuario.</li>
 * </ul>
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class RecomendarJuegoDialog extends JDialog {

    /** Usuario autenticado */
    private Usuario usuario;

    /** Controlador del perfil para obtener plataforma y género más jugados */
    private PerfilController controller;

    /** Ventana padre */
    private PerfilVista padre;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /**
     * Constructor del diálogo de recomendación.
     *
     * @param padre      ventana padre
     * @param usuario    usuario autenticado
     * @param controller controlador del perfil
     */
    public RecomendarJuegoDialog(PerfilVista padre, Usuario usuario, PerfilController controller) {
        super(padre, true);
        this.padre = padre;
        this.usuario = usuario;
        this.controller = controller;
        this.texts = ResourceBundle.getBundle("i18n.messages");

        setTitle("Recomendar juego");
        setSize(520, 340);
        setLocationRelativeTo(padre);
        setResizable(false);

        initComponents();
    }

    /**
     * Inicializa y organiza los componentes gráficos del diálogo.
     */
    private void initComponents() {

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(25, 15, 45));
        setContentPane(root);

        /* ===== TÍTULO ===== */
        JLabel lblTitulo = new JLabel(texts.getString("profile.rec.desc"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(28, 20, 18, 20));
        root.add(lblTitulo, BorderLayout.NORTH);

        /* ===== TARJETAS ===== */
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 2, 20, 0));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setBorder(BorderFactory.createEmptyBorder(5, 40, 15, 40));

        JButton btnPlataforma = crearTarjeta(
                "🎮",
                texts.getString("profile.rec.plataforma.titulo"),
                texts.getString("profile.rec.plataforma.sub"),
                new Color(110, 40, 180),
                new Color(80, 20, 140)
        );

        JButton btnGenero = crearTarjeta(
                "🎯",
                texts.getString("profile.rec.genero.titulo"),
                texts.getString("profile.rec.genero.sub"),
                new Color(60, 60, 160),
                new Color(40, 40, 120)
        );

        panelTarjetas.add(btnPlataforma);
        panelTarjetas.add(btnGenero);
        root.add(panelTarjetas, BorderLayout.CENTER);

        /* ===== CANCELAR ===== */
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setForeground(Color.LIGHT_GRAY);
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        panelSur.add(btnCancelar);
        root.add(panelSur, BorderLayout.SOUTH);

        /* ===== ACCIONES ===== */
        btnPlataforma.addActionListener(e -> abrirPorPlataforma());
        btnGenero.addActionListener(e -> abrirPorGenero());
    }

    /**
     * Crea un botón grande estilo tarjeta.
     */
    private JButton crearTarjeta(String icono, String titulo, String descripcion,
            Color colorTop, Color colorBot) {

        JButton boton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, colorTop, 0, getHeight(), colorBot));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(icono, (getWidth() - fm.stringWidth(icono)) / 2, 55);

                g2.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
                fm = g2.getFontMetrics();
                g2.drawString(titulo, (getWidth() - fm.stringWidth(titulo)) / 2, 82);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(new Color(220, 200, 255));
                fm = g2.getFontMetrics();
                String[] lineas = descripcion.split("\n");
                int y = 105;
                for (String l : lineas) {
                    g2.drawString(l, (getWidth() - fm.stringWidth(l)) / 2, y);
                    y += fm.getHeight();
                }
                g2.dispose();
            }
        };

        boton.setPreferredSize(new Dimension(190, 150));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBorder(null);
            }
        });
        return boton;
    }

    /**
     * Obtiene la plataforma más jugada y abre la vista de recomendaciones.
     * Si no hay datos suficientes, avisa al usuario.
     */
    private void abrirPorPlataforma() {
        String plataforma = controller.obtenerPlataformaMasJugada();
        if (plataforma == null || plataforma.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    texts.getString("profile.rec.nodata.plataforma"),
                    texts.getString("profile.rec.nodata.title"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        dispose();
        new RecomendacionesVista(padre, usuario, "plataforma", plataforma).setVisible(true);
    }

    /**
     * Obtiene el género más jugado y abre la vista de recomendaciones.
     * Si no hay datos suficientes, avisa al usuario.
     */
    private void abrirPorGenero() {
        String genero = controller.obtenerGeneroMasJugado();
        if (genero == null || genero.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    texts.getString("profile.rec.nodata.genero"),
                    texts.getString("profile.rec.nodata.title"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        dispose();
        new RecomendacionesVista(padre, usuario, "genero", genero).setVisible(true);
    }
}
