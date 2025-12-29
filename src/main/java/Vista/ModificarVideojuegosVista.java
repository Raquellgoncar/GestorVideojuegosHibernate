/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.ModificarVideojuegosController;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ResourceBundle;

/**
 *
 * @author Raquel
 */
public class ModificarVideojuegosVista extends JFrame {

    private Usuario usuario;

    private JButton btnInsertar, btnActualizar, btnEliminar, btnAtras;
    private JLabel lblTitulo;

    private Image imagenFondo;
    private Image fondoOriginal, fondoInsertar, fondoActualizar, fondoEliminar;

    private Box.Filler sep1, sep2;

    private ResourceBundle texts;
    private ModificarVideojuegosController controller;

    public ModificarVideojuegosVista(Usuario usuario) {
        this.usuario = usuario;

        texts = ResourceBundle.getBundle("i18n.messages");

        controller = new ModificarVideojuegosController(usuario);
        controller.setVista(this);

        setTitle(texts.getString("modify.window.title"));
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // ===== CARGA DE IMÁGENES =====
        fondoOriginal = new ImageIcon(getClass().getResource("/img/fondomodificar.png")).getImage();
        fondoInsertar = new ImageIcon(getClass().getResource("/img/insertar.png")).getImage();
        fondoActualizar = new ImageIcon(getClass().getResource("/img/actualizar.jpg")).getImage();
        fondoEliminar = new ImageIcon(getClass().getResource("/img/eliminar.jpg")).getImage();

        imagenFondo = fondoOriginal;

        initComponents();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adaptarTamano();
            }
        });
    }

    private void initComponents() {

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(root);

        GridBagConstraints gbc;

        /* ================= TÍTULO ================= */
        lblTitulo = new JLabel(texts.getString("modify.title"));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(30, 20, 20, 20);
        root.add(lblTitulo, gbc);

        /* ================= PANEL CENTRAL ================= */
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));
        panelCentral.setOpaque(false);

        btnInsertar = crearBoton(texts.getString("modify.insert"));
        btnActualizar = crearBoton(texts.getString("modify.update"));
        btnEliminar = crearBoton(texts.getString("modify.delete"));

        btnInsertar.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnActualizar.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnEliminar.setAlignmentY(Component.CENTER_ALIGNMENT);

        sep1 = (Box.Filler) Box.createRigidArea(new Dimension(60, 0));
        sep2 = (Box.Filler) Box.createRigidArea(new Dimension(60, 0));

        panelCentral.add(btnInsertar);
        panelCentral.add(sep1);
        panelCentral.add(btnActualizar);
        panelCentral.add(sep2);
        panelCentral.add(btnEliminar);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(90, 0, 0, 0);
        root.add(panelCentral, gbc);

        // ===== ACCIONES =====
        btnInsertar.addActionListener(e -> controller.insertar());
        btnActualizar.addActionListener(e -> controller.actualizar());
        btnEliminar.addActionListener(e -> controller.eliminar());

        /* ================= BOTÓN ATRÁS ================= */
        btnAtras = new JButton(texts.getString("common.back"));
        btnAtras.setFocusPainted(false);
        btnAtras.addActionListener(e -> controller.volverMenu());

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(15, 15, 15, 15);
        root.add(btnAtras, gbc);

        /* ================= HOVER FONDOS ================= */
        btnInsertar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                imagenFondo = fondoInsertar;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                imagenFondo = fondoOriginal;
                repaint();
            }
        });

        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                imagenFondo = fondoActualizar;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                imagenFondo = fondoOriginal;
                repaint();
            }
        });

        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                imagenFondo = fondoEliminar;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                imagenFondo = fondoOriginal;
                repaint();
            }
        });

        adaptarTamano();
    }

    /* ================= ESCALADO ================= */
    private void adaptarTamano() {

        float escala = Math.max(1f, getWidth() / 800f);

        lblTitulo.setFont(new Font(
                "Showcard Gothic",
                Font.PLAIN,
                (int) (32 * escala)
        ));

        Dimension tamBoton = new Dimension(
                (int) (155 * escala),
                (int) (35 * escala)
        );

        Font fuenteBoton = new Font(
                "Impact",
                Font.PLAIN,
                (int) (17 * escala)
        );

        for (JButton b : new JButton[]{btnInsertar, btnActualizar, btnEliminar}) {
            b.setPreferredSize(tamBoton);
            b.setMaximumSize(tamBoton);
            b.setFont(fuenteBoton);
        }

        int separacion = (getWidth() < 1000) ? 60 : 120;
        Dimension sep = new Dimension(separacion, 0);
        sep1.changeShape(sep, sep, sep);
        sep2.changeShape(sep, sep, sep);

        Dimension tamAtras = new Dimension(
                (int) (100 * escala),
                (int) (30 * escala)
        );

        btnAtras.setPreferredSize(tamAtras);
        btnAtras.setFont(new Font(
                "Impact",
                Font.PLAIN,
                (int) (16 * escala)
        ));

        revalidate();
        repaint();
    }

    /* ================= BOTÓN MORADO ================= */
    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(200, 170, 240),
                        0, getHeight(), new Color(150, 100, 210)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();

                super.paintComponent(g);
            }
        };

        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }
}
