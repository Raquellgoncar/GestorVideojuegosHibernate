/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Videojuego;
import Modelo.DAO.VideojuegoDAO;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Raquel
 */
public class ListadoVideojuegosVista extends JFrame {

    private Usuario usuario;
    private JTable tabla;
    private DefaultTableModel modelo;
    private VideojuegoDAO videojuegoDAO;

    private Image imagenFondo;

    public ListadoVideojuegosVista(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();

        setTitle("Listado de videojuegos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        var url = getClass().getResource("/img/fondoListado.jpg");
        if (url != null) {
            imagenFondo = new ImageIcon(url).getImage();
        }

        initComponents();
        cargarVideojuegos();
    }

    private void initComponents() {

        /* ===== PANEL ROOT CON FONDO ===== */
        JPanel root = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(
                            imagenFondo,
                            0, 0,
                            getWidth(), getHeight(),
                            this
                    );
                }
            }
        };
        setContentPane(root);

        /* ===== TÍTULO ===== */
        JLabel lblTitulo = new JLabel("VIDEOJUEGOS REGISTRADOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        root.add(lblTitulo, BorderLayout.NORTH);

        /* ===== TABLA ===== */
        modelo = new DefaultTableModel(
                new Object[]{"Título", "Plataforma", "Año", "Valoración"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(26);

        tabla.setOpaque(false);
        tabla.setBackground(new Color(255, 255, 255, 190));

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setOpaque(false);
        tabla.getTableHeader().setBackground(new Color(255, 255, 255, 200));

        tabla.getColumnModel().getColumn(0).setPreferredWidth(250);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado);

        tabla.setSelectionBackground(new Color(220, 220, 250));
        tabla.setSelectionForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        root.add(scroll, BorderLayout.CENTER);

        /* ===== PANEL INFERIOR ===== */
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);

        JButton btnAtras = crearBotonMoradoAtras();

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzq.setOpaque(false);
        panelIzq.add(btnAtras);

        panelSur.add(panelIzq, BorderLayout.WEST);
        root.add(panelSur, BorderLayout.SOUTH);
    }

    /* ===== BOTÓN ATRÁS ===== */
    private JButton crearBotonMoradoAtras() {

        JButton boton = new JButton("Atrás") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

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
        boton.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(120, 30));

        boton.addActionListener(e -> {
            new MenuPrincipalVista(usuario).setVisible(true);
            dispose();
        });

        return boton;
    }

    /* ===== CARGAR VIDEOJUEGOS ===== */
    private void cargarVideojuegos() {

        modelo.setRowCount(0);

        List<Videojuego> videojuegos = videojuegoDAO.fetchAll();
        if (videojuegos == null || videojuegos.isEmpty()) return;

        for (Videojuego v : videojuegos) {
            modelo.addRow(new Object[]{
                    v.getTitulo(),
                    v.getPlataforma(),
                    v.getAnio(),
                    v.getValoracion()
            });
        }
    }
}
