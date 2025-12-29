/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.MostrarFavoritosController;
import Modelo.Favorito;
import Modelo.Usuario;
import Modelo.Videojuego;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.JTableHeader;
import java.util.ResourceBundle;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author Raquel
 */
public class MostrarFavoritosVista extends JFrame {

    private Usuario usuario;
    private JTable tabla;
    private DefaultTableModel modelo;
    private List<Favorito> listaFavoritos;

    private ResourceBundle texts;
    private MostrarFavoritosController controller;

    public MostrarFavoritosVista(Usuario usuario) {
        this.usuario = usuario;
        this.texts = ResourceBundle.getBundle("i18n.messages");

        controller = new MostrarFavoritosController(usuario);
        controller.setVista(this);

        setTitle(texts.getString("favorites.window.title"));
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        cargarFavoritos();
    }

    private void initComponents() {

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

        JLabel lblTitulo = new JLabel(
                texts.getString("favorites.title"),
                SwingConstants.CENTER
        );
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        root.add(lblTitulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{
                    texts.getString("favorites.table.title"),
                    texts.getString("favorites.table.platform"),
                    texts.getString("favorites.table.year"),
                    texts.getString("favorites.table.rating")
                },
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

        // ===== CENTRADO DE AÑO Y VALORACIÓN (IGUAL QUE LA CLASE ANTIGUA) =====
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centrado); // Año
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado); // Valoración
        // ==================================================================

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        root.add(scroll, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);

        JButton btnAtras = crearBotonMorado(
                texts.getString("favorites.back"),
                new Dimension(120, 30),
                e -> controller.volverMenu()
        );

        JButton btnQuitar = crearBotonMorado(
                texts.getString("favorites.remove"),
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

    /* ===== CARGAR FAVORITOS ===== */
    public void cargarFavoritos() {

        listaFavoritos = controller.obtenerFavoritos();
        modelo.setRowCount(0);

        if (listaFavoritos == null || listaFavoritos.isEmpty()) return;

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
                    texts.getString("favorites.select.warning"),
                    texts.getString("favorites.select.title"),
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                texts.getString("favorites.confirm.text"),
                texts.getString("favorites.confirm.title"),
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            controller.quitarFavorito(listaFavoritos.get(fila));
        }
    }

    /* ===== BOTÓN MORADO ===== */
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
}

