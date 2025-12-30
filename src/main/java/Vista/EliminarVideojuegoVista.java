/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.EliminarVideojuegoController;
import Modelo.Favorito;
import Modelo.Usuario;
import Modelo.Videojuego;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Vista para la eliminación de videojuegos.
 * <p>
 * Muestra un listado de los videojuegos del usuario en una tabla,
 * permite buscarlos por título y eliminar el videojuego seleccionado,
 * solicitando confirmación previa al usuario.
 * </p>
 *
 * Sigue el patrón MVC, delegando la lógica de eliminación en
 * {@link EliminarVideojuegoController}.
 *
 * @author Raquel
 * @version 1.0
 */
public class EliminarVideojuegoVista extends JFrame {

    /** Usuario autenticado */
    private Usuario usuario;

    /** Tabla que muestra los videojuegos */
    private JTable tabla;

    /** Modelo de la tabla */
    private DefaultTableModel modelo;

    /** Campo de búsqueda */
    private JTextField txtBuscar;

    /** Botones de la vista */
    private JButton btnBuscar, btnEliminar, btnAtras;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /** Controlador asociado a la vista */
    private EliminarVideojuegoController controller;

    /**
     * Constructor de la vista de eliminación de videojuegos.
     *
     * @param usuario usuario autenticado
     */
    public EliminarVideojuegoVista(Usuario usuario) {
        this.usuario = usuario;

        texts = ResourceBundle.getBundle("i18n.messages");

        controller = new EliminarVideojuegoController(usuario);
        controller.setVista(this);

        setTitle(texts.getString("delete.window.title"));
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        cargarVideojuegos();
    }

    /**
     * Inicializa y organiza los componentes gráficos de la ventana.
     */
    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        /* ================= TÍTULO ================= */
        JLabel lblTitulo = new JLabel(
                texts.getString("delete.title"),
                SwingConstants.CENTER
        );
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
        ponerPlaceholder(txtBuscar, texts.getString("delete.search.placeholder"));

        btnBuscar = new JButton(texts.getString("delete.search"));

        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelCentro.add(panelBuscar, BorderLayout.NORTH);

        /* ===== TABLA ===== */
        modelo = new DefaultTableModel(
                new Object[]{
                    texts.getString("delete.table.id"),
                    texts.getString("delete.table.title"),
                    texts.getString("delete.table.platform"),
                    texts.getString("delete.table.year"),
                    texts.getString("delete.table.rating"),
                    texts.getString("delete.table.favorite")
                },
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
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(8, 25, 18, 25));
        panelInferior.setOpaque(false);

        btnAtras = crearBotonMorado(
                texts.getString("delete.back"),
                new Dimension(120, 32),
                e -> controller.volverModificar()
        );

        btnEliminar = crearBotonMorado(
                texts.getString("delete.delete"),
                new Dimension(200, 38),
                e -> eliminarSeleccionado()
        );

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzq.setOpaque(false);
        panelIzq.add(btnAtras);

        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDer.setOpaque(false);
        panelDer.add(btnEliminar);

        panelInferior.add(panelIzq, BorderLayout.WEST);
        panelInferior.add(panelDer, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarVideojuegos());

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    /**
     * Carga en la tabla los videojuegos del usuario.
     */
    private void cargarVideojuegos() {

        modelo.setRowCount(0);

        List<Videojuego> videojuegos = controller.obtenerVideojuegos();
        List<Favorito> favoritos = controller.obtenerFavoritos();

        for (Videojuego v : videojuegos) {

            boolean esFavorito = favoritos.stream()
                    .anyMatch(f -> f.getVideojuegoId().getId().equals(v.getId()));

            modelo.addRow(new Object[]{
                v.getId(),
                v.getTitulo(),
                v.getPlataforma(),
                v.getAnio(),
                v.getValoracion(),
                esFavorito
                ? texts.getString("delete.yes")
                : texts.getString("delete.no")
            });
        }
    }

    /**
     * Filtra los videojuegos mostrados en la tabla según el texto introducido.
     */
    private void buscarVideojuegos() {

        String texto = txtBuscar.getText().trim().toLowerCase();
        modelo.setRowCount(0);

        List<Videojuego> videojuegos = controller.obtenerVideojuegos();
        List<Favorito> favoritos = controller.obtenerFavoritos();

        for (Videojuego v : videojuegos) {

            if (texto.isEmpty()
                    || texto.equals(texts.getString("delete.search.placeholder").toLowerCase())
                    || v.getTitulo().toLowerCase().contains(texto)) {

                boolean esFavorito = favoritos.stream()
                        .anyMatch(f -> f.getVideojuegoId().getId().equals(v.getId()));

                modelo.addRow(new Object[]{
                    v.getId(),
                    v.getTitulo(),
                    v.getPlataforma(),
                    v.getAnio(),
                    v.getValoracion(),
                    esFavorito
                    ? texts.getString("delete.yes")
                    : texts.getString("delete.no")
                });
            }
        }
    }

    /**
     * Elimina el videojuego seleccionado tras confirmar la acción.
     */
    private void eliminarSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("delete.select.warning"),
                    texts.getString("delete.select.title"),
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String titulo = (String) modelo.getValueAt(fila, 1);

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                java.text.MessageFormat.format(
                        texts.getString("delete.confirm.text"),
                        titulo
                ),
                texts.getString("delete.confirm.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        int idVideojuego = (int) modelo.getValueAt(fila, 0);

        try {
            controller.eliminarVideojuego(idVideojuego);

            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("delete.success"),
                    texts.getString("delete.success.title"),
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarVideojuegos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("delete.error.generic"),
                    texts.getString("delete.error.title"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Crea un botón con estilo morado personalizado.
     *
     * @param texto texto del botón
     * @param tamaño tamaño del botón
     * @param action acción a ejecutar
     * @return botón configurado
     */
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

    /**
     * Aplica un comportamiento de placeholder a un campo de texto.
     *
     * @param campo campo de texto
     * @param texto texto del placeholder
     */
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
