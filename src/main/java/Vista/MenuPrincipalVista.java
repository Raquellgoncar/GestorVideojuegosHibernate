/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.MenuPrincipalController;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.*;
import java.util.ResourceBundle;


/**
 *
 * @author Raquel
 */public class MenuPrincipalVista extends JFrame {

    private JButton btnMostrar, btnModificar, btnFavoritos, btnPerfil, btnAtras;
    private JLabel lblTitulo;
    private Usuario usuario;

    private Image imagenFondo;
    private Image fondoNormal;
    private ImageIcon fondoGif;

    private ResourceBundle texts;
    private MenuPrincipalController controller;

    public MenuPrincipalVista(Usuario usuario) {
        this.usuario = usuario;

        texts = ResourceBundle.getBundle("i18n.messages");

        controller = new MenuPrincipalController(usuario);
        controller.setVista(this);

        setTitle(texts.getString("menu.title"));
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        var urlNormal = getClass().getResource("/img/images.jpg");
        var urlGif = getClass().getResource("/img/fondoMenu.gif");

        if (urlNormal != null) {
            fondoNormal = new ImageIcon(urlNormal).getImage();
        }

        if (urlGif != null) {
            fondoGif = new ImageIcon(urlGif);
        }

        imagenFondo = fondoNormal;

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

        lblTitulo = new JLabel(texts.getString("menu.title"));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setToolTipText("Menú principal de la aplicación");

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(30, 20, 10, 20);
        root.add(lblTitulo, gbc);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        btnMostrar = crearBoton(texts.getString("menu.show"));
        btnMostrar.setToolTipText("Ver el listado de videojuegos");
        btnMostrar.addActionListener(e -> controller.mostrarListado());

        btnModificar = crearBoton(texts.getString("menu.modify"));
        btnModificar.setToolTipText("Modificar o eliminar videojuegos");
        btnModificar.addActionListener(e -> controller.modificarVideojuegos());

        btnFavoritos = crearBoton(texts.getString("menu.favorites"));
        btnFavoritos.setToolTipText("Ver tus videojuegos favoritos");
        btnFavoritos.addActionListener(e -> controller.mostrarFavoritos());

        btnPerfil = crearBoton(texts.getString("menu.profile"));
        btnPerfil.setToolTipText("Ver y editar tu perfil");
        btnPerfil.addActionListener(e -> controller.mostrarPerfil());

        panelCentral.add(btnMostrar);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 40)));
        panelCentral.add(btnModificar);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 40)));
        panelCentral.add(btnFavoritos);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 40)));
        panelCentral.add(btnPerfil);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        root.add(panelCentral, gbc);

        btnAtras = new JButton(texts.getString("menu.back"));
        btnAtras.setFocusPainted(false);
        btnAtras.setToolTipText("Cerrar sesión y volver al login");
        btnAtras.addActionListener(e -> controller.cerrarSesion());

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(12, 12, 12, 12);
        root.add(btnAtras, gbc);

        adaptarTamano();
    }

    private void adaptarTamano() {

        int ancho = getWidth();
        int alto = getHeight();

        float escala = Math.max(1f, ancho / 700f);

        lblTitulo.setFont(new Font(
                "Wide Latin",
                Font.PLAIN,
                (int) (28 * escala)
        ));

        int margenSuperiorTitulo = (alto > 800) ? 80 : 30;

        GridBagLayout gbl = (GridBagLayout) getContentPane().getLayout();
        GridBagConstraints cTitulo = gbl.getConstraints(lblTitulo);
        cTitulo.insets = new Insets(margenSuperiorTitulo, 20, 10, 20);
        gbl.setConstraints(lblTitulo, cTitulo);

        int anchoBoton = (int) (250 * escala);
        int altoBoton = (int) (50 * escala);
        Dimension tamBoton = new Dimension(anchoBoton, altoBoton);

        Font fuenteBoton = new Font(
                "Segoe UI Black",
                Font.PLAIN,
                (int) (17 * escala)
        );

        for (JButton b : new JButton[]{btnMostrar, btnModificar, btnFavoritos, btnPerfil}) {
            b.setMaximumSize(tamBoton);
            b.setFont(fuenteBoton);
        }

        int separacion = (alto > 800) ? 55 : 40;

        Component[] comps = btnMostrar.getParent().getComponents();
        for (Component comp : comps) {
            if (comp instanceof Box.Filler) {
                Box.Filler filler = (Box.Filler) comp;
                filler.changeShape(
                        new Dimension(0, separacion),
                        new Dimension(0, separacion),
                        new Dimension(0, separacion)
                );
            }
        }

        btnAtras.setPreferredSize(
                new Dimension((int) (120 * escala), (int) (28 * escala))
        );
        btnAtras.setFont(fuenteBoton);

        revalidate();
        repaint();
    }

    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(180, 120, 255),
                        0, getHeight(), new Color(110, 40, 180)
                );

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
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
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (fondoGif != null) {
                    imagenFondo = fondoGif.getImage();
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                imagenFondo = fondoNormal;
                repaint();
            }
        });

        return boton;
    }
}
