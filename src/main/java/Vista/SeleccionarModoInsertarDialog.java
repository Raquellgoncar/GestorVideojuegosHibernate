package Vista;

import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Diálogo que permite al usuario elegir cómo quiere registrar un videojuego.
 * <p>
 * Presenta dos opciones en forma de tarjetas grandes:
 * <ul>
 *   <li><b>Buscar en catálogo</b>: busca el juego en la API de RAWG por
 *       plataforma y autocompleta título, género e imagen.</li>
 *   <li><b>Añadir manualmente</b>: abre el formulario vacío para que el
 *       usuario introduzca todos los datos a mano.</li>
 * </ul>
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class SeleccionarModoInsertarDialog extends JDialog {

    /** Usuario autenticado */
    private Usuario usuario;

    /** Ventana padre desde la que se abre el diálogo */
    private ModificarVideojuegosVista padre;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /**
     * Constructor del diálogo de selección de modo de inserción.
     *
     * @param padre  ventana padre
     * @param usuario usuario autenticado
     */
    public SeleccionarModoInsertarDialog(ModificarVideojuegosVista padre, Usuario usuario) {
        super(padre, true);
        this.padre = padre;
        this.usuario = usuario;
        this.texts = java.util.ResourceBundle.getBundle("i18n.messages");

        setTitle("Registrar videojuego");
        setSize(640, 430);
        setLocationRelativeTo(padre);
        setResizable(false);
        setUndecorated(false);

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
        JLabel lblTitulo = new JLabel(texts.getString("insert.mode.title"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(35, 20, 25, 20));
        root.add(lblTitulo, BorderLayout.NORTH);

        /* ===== PANEL DE TARJETAS ===== */
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 2, 20, 0));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        JButton btnCatalogo = crearTarjeta(
                "🎮",
                texts.getString("insert.mode.catalog"),
                texts.getString("insert.mode.catalog.sub"),
                new Color(110, 40, 180),
                new Color(80, 20, 140)
        );

        JButton btnManual = crearTarjeta(
                "✏️",
                texts.getString("insert.mode.manual"),
                texts.getString("insert.mode.manual.sub"),
                new Color(60, 60, 160),
                new Color(40, 40, 120)
        );

        panelTarjetas.add(btnCatalogo);
        panelTarjetas.add(btnManual);
        root.add(panelTarjetas, BorderLayout.CENTER);

        /* ===== BOTÓN CANCELAR ===== */
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton btnCancelar = new JButton(texts.getString("insert.cancel"));
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
        btnCatalogo.addActionListener(e -> abrirCatalogo());
        btnManual.addActionListener(e -> abrirManual());
    }

    /**
     * Crea un botón grande estilo tarjeta con icono, título y descripción.
     *
     * @param icono       emoji o símbolo grande
     * @param titulo      texto principal de la tarjeta
     * @param descripcion texto secundario descriptivo
     * @param colorTop    color superior del degradado
     * @param colorBot    color inferior del degradado
     * @return botón configurado como tarjeta
     */
    private JButton crearTarjeta(
            String icono,
            String titulo,
            String descripcion,
            Color colorTop,
            Color colorBot
    ) {
        JButton boton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo degradado
                GradientPaint gp = new GradientPaint(
                        0, 0, colorTop,
                        0, getHeight(), colorBot
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Icono
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
                g2.setColor(Color.WHITE);
                java.awt.font.TextLayout tlIcono = new java.awt.font.TextLayout(
                        icono, g2.getFont(), g2.getFontRenderContext());
                int xIcono = (int)((getWidth() - tlIcono.getAdvance()) / 2);
                g2.drawString(icono, xIcono, 75);

                // Título
                g2.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
                g2.setColor(Color.WHITE);
                FontMetrics fmTitulo = g2.getFontMetrics();
                int xTitulo = (getWidth() - fmTitulo.stringWidth(titulo)) / 2;
                g2.drawString(titulo, xTitulo, 110);

                // Descripción (multilinea)
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.setColor(new Color(220, 200, 255));
                String[] lineas = descripcion.split("\n");
                FontMetrics fmDesc = g2.getFontMetrics();
                int yDesc = 140;
                for (String linea : lineas) {
                    int xDesc = (getWidth() - fmDesc.stringWidth(linea)) / 2;
                    g2.drawString(linea, xDesc, yDesc);
                    yDesc += fmDesc.getHeight();
                }

                g2.dispose();
            }
        };

        boton.setPreferredSize(new Dimension(250, 200));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBorder(null);
            }
        });

        return boton;
    }

    /**
     * Abre el diálogo de búsqueda en el catálogo de RAWG.
     */
    private void abrirCatalogo() {
        dispose();
        new BuscarEnCatalogoDialog(padre, usuario).setVisible(true);
    }

    /**
     * Abre el formulario de inserción manual de videojuegos.
     */
    private void abrirManual() {
        dispose();
        new InsertarVideojuegoVista(usuario, padre).setVisible(true);
    }
}
