/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.DAO.Imp.FavoritoDAO_imp;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Usuario;
import Modelo.Videojuego;
import Modelo.Favorito;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Raquel
 */
public class ActualizarVideojuegoVista extends JFrame {

    private Usuario usuario;

    private VideojuegoDAO videojuegoDAO;
    private FavoritoDAO_imp favoritoDAO;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnActualizar, btnAtras;

    public ActualizarVideojuegoVista(Usuario usuario) {
        this.usuario = usuario;
        this.videojuegoDAO = new VideojuegoDAO_imp();
        this.favoritoDAO = new FavoritoDAO_imp();

        setTitle("Actualizar videojuego");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        cargarVideojuegos();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        /* ================= TÍTULO ================= */
        JLabel lblTitulo = new JLabel("ACTUALIZAR JUEGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 28));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(lblTitulo, BorderLayout.NORTH);

        /* ================= PANEL CENTRAL ================= */
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));

        /* ===== BUSCADOR ===== */
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        txtBuscar = new JTextField(20);
        txtBuscar.setPreferredSize(new Dimension(
                txtBuscar.getPreferredSize().width,
                32
        ));
        ponerPlaceholder(txtBuscar, "Buscar por nombre...");

        btnBuscar = new JButton("Buscar");

        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelCentro.add(panelBuscar, BorderLayout.NORTH);

        /* ===== TABLA ===== */
        modelo = new DefaultTableModel(
                new Object[]{"ID", "Título", "Plataforma", "Año", "Valoración", "Favorito"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(26);

        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(4).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(5).setCellRenderer(centrado);

        JScrollPane scroll = new JScrollPane(tabla);
        panelCentro.add(scroll, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        /* ================= PANEL INFERIOR ================= */
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        btnAtras = crearBotonMorado(
                "Atrás",
                new Dimension(120, 32),
                e -> {
                    new ModificarVideojuegosVista(usuario).setVisible(true);
                    dispose();
                }
        );

        btnActualizar = crearBotonMorado(
                "Actualizar",
                new Dimension(180, 38),
                e -> actualizarSeleccionado()
        );

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzq.setOpaque(false);
        panelIzq.add(btnAtras);

        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDer.setOpaque(false);
        panelDer.add(btnActualizar);

        panelSur.add(panelIzq, BorderLayout.WEST);
        panelSur.add(panelDer, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);

        /* ================= ACCIONES ================= */
        btnBuscar.addActionListener(e -> buscarVideojuegos());

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    /* ================= BOTÓN MORADO ================= */
    private JButton crearBotonMorado(
            String texto,
            Dimension tamaño,
            java.awt.event.ActionListener action
    ) {

        JButton boton = new JButton(texto) {
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(tamaño);

        boton.addActionListener(action);
        return boton;
    }

    /* ================= CARGAR VIDEOJUEGOS ================= */
    private void cargarVideojuegos() {

        modelo.setRowCount(0);

        List<Videojuego> videojuegos = videojuegoDAO.fetchAll();
        List<Favorito> favoritos = favoritoDAO.fetchByUsuario(usuario.getId());

        for (Videojuego v : videojuegos) {

            boolean esFavorito = favoritos.stream()
                    .anyMatch(f -> f.getVideojuegoId().getId().equals(v.getId()));

            modelo.addRow(new Object[]{
                v.getId(),
                v.getTitulo(),
                v.getPlataforma(),
                v.getAnio(),
                v.getValoracion(),
                esFavorito ? "Sí" : "No"
            });
        }
    }

    /* ================= BUSCAR ================= */
    private void buscarVideojuegos() {

        String texto = txtBuscar.getText().trim().toLowerCase();
        modelo.setRowCount(0);

        List<Videojuego> videojuegos = videojuegoDAO.fetchAll();
        List<Favorito> favoritos = favoritoDAO.fetchByUsuario(usuario.getId());

        for (Videojuego v : videojuegos) {

            if (texto.isEmpty()
                    || texto.equals("buscar por nombre...")
                    || v.getTitulo().toLowerCase().contains(texto)) {

                boolean esFavorito = favoritos.stream()
                        .anyMatch(f -> f.getVideojuegoId().getId().equals(v.getId()));

                modelo.addRow(new Object[]{
                    v.getId(),
                    v.getTitulo(),
                    v.getPlataforma(),
                    v.getAnio(),
                    v.getValoracion(),
                    esFavorito ? "Sí" : "No"
                });
            }
        }
    }

    /* ================= ACTUALIZAR ================= */
    private void actualizarSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione videojuego a actualizar",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idVideojuego = (int) modelo.getValueAt(fila, 0);
        Videojuego v = videojuegoDAO.fetchOne(idVideojuego);

        boolean esFavorito = modelo.getValueAt(fila, 5).equals("Sí");

        new ActualizarVideojuegoDialog(
                this,
                usuario,
                v,
                esFavorito
        ).setVisible(true);

        cargarVideojuegos();
    }

    /* ================= PLACEHOLDER ================= */
    private void ponerPlaceholder(JTextField campo, String texto) {

        campo.setText(texto);
        campo.setForeground(Color.GRAY);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(texto)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(texto);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
    }
}


