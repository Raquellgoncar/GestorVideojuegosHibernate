/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Favorito;
import Modelo.Usuario;
import Modelo.Videojuego;
import Modelo.DAO.Imp.FavoritoDAO_imp;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Raquel
 */
public class MostrarFavoritosVista extends JFrame {

    private Usuario usuario;
    private JTable tabla;
    private DefaultTableModel modelo;
    private List<Favorito> listaFavoritos;

    public MostrarFavoritosVista(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Mis favoritos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        cargarFavoritos();
    }

    private void initComponents() {

        /* ===== PANEL CON FONDO ===== */
        JPanel root = new JPanel(new BorderLayout(10, 10)) {

            private Image fondo = new ImageIcon(
                    getClass().getResource("/img/fondoFav.png")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        setContentPane(root);

        /* ===== TÍTULO ===== */
        JLabel lblTitulo = new JLabel("MIS FAVORITOS", SwingConstants.CENTER);
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
        tabla.setBackground(new Color(255, 255, 255, 180));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);
        header.setOpaque(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        root.add(scroll, BorderLayout.CENTER);

        /* ===== PANEL INFERIOR ===== */
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);

        JButton btnAtras = crearBotonMorado(
                "Atrás",
                new Dimension(120, 30),
                e -> {
                    new MenuPrincipalVista(usuario).setVisible(true);
                    dispose();
                }
        );

        JButton btnQuitar = crearBotonMorado(
                "Quitar de favoritos",
                new Dimension(200, 40),
                e -> quitarFavorito()
        );

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzq.setOpaque(false);
        panelIzq.add(btnAtras);

        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDer.setOpaque(false);
        panelDer.add(btnQuitar);

        panelSur.add(panelIzq, BorderLayout.WEST);
        panelSur.add(panelDer, BorderLayout.EAST);

        root.add(panelSur, BorderLayout.SOUTH);
    }

    /* ===== BOTÓN MORADO REUTILIZABLE ===== */
    private JButton crearBotonMorado(
            String texto,
            Dimension tamaño,
            ActionListener action
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
        boton.setPreferredSize(tamaño);

        boton.addActionListener(action);

        return boton;
    }

    /* ===== CARGAR FAVORITOS ===== */
    private void cargarFavoritos() {

        FavoritoDAO_imp dao = new FavoritoDAO_imp();
        listaFavoritos = dao.fetchByUsuario(usuario.getId());

        modelo.setRowCount(0);

        if (listaFavoritos == null || listaFavoritos.isEmpty()) {
            return;
        }

        for (Favorito f : listaFavoritos) {
            Videojuego v = f.getVideojuegoId();
            modelo.addRow(new Object[]{
                    v.getTitulo(),
                    v.getPlataforma(),
                    v.getAnio(),
                    v.getValoracion()
            });
        }
    }

    /* ===== QUITAR FAVORITO ===== */
    private void quitarFavorito() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un videojuego de la tabla",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Quieres quitar este videojuego de favoritos?",
                "Quitar de favoritos",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            Favorito f = listaFavoritos.get(fila);
            new FavoritoDAO_imp().delete(f.getId());
            cargarFavoritos();
        }
    }
}