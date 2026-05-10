package Vista;

import Controlador.EliminarVideojuegoController;
import Modelo.Usuario;
import Modelo.Videojuego;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ResourceBundle;

/**
 * Vista para la eliminación de videojuegos.
 * Estética oscura. El botón eliminar es rojo para indicar acción destructiva.
 *
 * @author Raquel
 * @version 3.0
 */
public class EliminarVideojuegoVista extends JFrame {

    private static final Color BG_DARK       = new Color(18, 10, 35);
    private static final Color BG_CARD       = new Color(35, 20, 60);
    private static final Color PURPLE_LIGHT  = new Color(180, 120, 255);
    private static final Color PURPLE_DARK   = new Color(110, 40, 180);
    private static final Color RED_ACCENT    = new Color(220, 60, 80);
    private static final Color RED_DARK      = new Color(160, 30, 50);
    private static final Color TEXT_PRIMARY  = new Color(240, 235, 255);
    private static final Color TEXT_SECONDARY= new Color(170, 150, 210);
    private static final Color SEPARATOR     = new Color(70, 45, 110);
    private static final Color TABLE_ROW_ALT = new Color(42, 25, 70);
    private static final Color TABLE_SEL     = new Color(110, 40, 180);
    private static final Color FIELD_BG      = new Color(28, 15, 50);

    private Usuario usuario;
    private ModificarVideojuegosVista padre;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnLimpiarBusqueda, btnEliminar, btnAtras;
    private ResourceBundle texts;
    private EliminarVideojuegoController controller;

    public EliminarVideojuegoVista(Usuario usuario) { this(usuario, null); }

    public EliminarVideojuegoVista(Usuario usuario, ModificarVideojuegosVista padre) {
        this.usuario = usuario; this.padre = padre;
        texts = ResourceBundle.getBundle("i18n.messages");
        controller = new EliminarVideojuegoController(usuario);
        controller.setVista(this);
        setTitle(texts.getString("delete.window.title"));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(padre != null ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
        setResizable(true); setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents(); cargarVideojuegos();
    }

    private void initComponents() {
        JPanel fondo = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, BG_DARK, getWidth(), getHeight(), new Color(28, 10, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight()); g2.dispose();
            }
        };
        fondo.setOpaque(false); setContentPane(fondo);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false); cabecera.setBorder(BorderFactory.createEmptyBorder(25, 40, 10, 40));
        JLabel lblTitulo = new JLabel(texts.getString("delete.title"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 32));
        lblTitulo.setForeground(TEXT_PRIMARY);
        cabecera.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBuscar.setOpaque(false);
        txtBuscar = new JTextField(22);
        txtBuscar.setBackground(FIELD_BG); txtBuscar.setForeground(TEXT_PRIMARY);
        txtBuscar.setCaretColor(TEXT_PRIMARY); txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setPreferredSize(new Dimension(280, 34));
        ponerPlaceholder(txtBuscar, texts.getString("delete.search.placeholder"));
        btnLimpiarBusqueda = crearBotonLimpiar();
        JPanel campoBusqueda = crearCampoBusquedaConLimpiar(txtBuscar, btnLimpiarBusqueda);
        btnBuscar = crearBotonMorado(texts.getString("delete.search"), new Dimension(100, 34), e -> buscarVideojuegos());
        panelBuscar.add(campoBusqueda); panelBuscar.add(btnBuscar);
        cabecera.add(panelBuscar, BorderLayout.SOUTH);
        fondo.add(cabecera, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{
                texts.getString("delete.table.id"), texts.getString("delete.table.title"),
                texts.getString("delete.table.platform"), texts.getString("delete.table.year"),
                texts.getString("delete.table.rating"), texts.getString("delete.table.favorite")
        }, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        tabla = new JTable(modelo) {
            @Override public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) { c.setBackground(TABLE_SEL); c.setForeground(Color.WHITE); }
                else { c.setBackground(row % 2 == 0 ? BG_CARD : TABLE_ROW_ALT); c.setForeground(TEXT_PRIMARY); }
                return c;
            }
        };
        tabla.setRowHeight(44); tabla.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabla.setBackground(BG_CARD); tabla.setForeground(TEXT_PRIMARY);
        tabla.setSelectionBackground(TABLE_SEL); tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(SEPARATOR); tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(50, 30, 85)); header.setForeground(TEXT_PRIMARY);
        header.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, SEPARATOR));
        header.setReorderingAllowed(false);

        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 3; i <= 5; i++) tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(SEPARATOR));
        scroll.getViewport().setBackground(BG_CARD);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false); panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        panelTabla.add(scroll, BorderLayout.CENTER);
        fondo.add(panelTabla, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false); pie.setBorder(BorderFactory.createEmptyBorder(5, 40, 18, 40));
        btnAtras   = crearBotonMorado(texts.getString("delete.back"), new Dimension(120, 34), e -> volverAtras());
        btnEliminar = crearBotonRojo(texts.getString("delete.delete"), new Dimension(190, 40), e -> eliminarSeleccionado());
        JPanel pI = new JPanel(new FlowLayout(FlowLayout.LEFT)); pI.setOpaque(false); pI.add(btnAtras);
        JPanel pD = new JPanel(new FlowLayout(FlowLayout.RIGHT)); pD.setOpaque(false); pD.add(btnEliminar);
        pie.add(pI, BorderLayout.WEST); pie.add(pD, BorderLayout.EAST);
        fondo.add(pie, BorderLayout.SOUTH);

        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());
    }

    private void cargarVideojuegos() {
        modelo.setRowCount(0);
        for (Videojuego v : controller.obtenerVideojuegos()) {
            modelo.addRow(new Object[]{v.getId(), v.getTitulo(), v.getPlataforma(),
                    v.getFechaMostrada(), v.getValoracionMostrada(),
                    v.isFavorito() ? texts.getString("delete.yes") : texts.getString("delete.no")});
        }
    }

    private void buscarVideojuegos() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        modelo.setRowCount(0);
        for (Videojuego v : controller.obtenerVideojuegos()) {
            if (texto.isEmpty() || texto.equals(texts.getString("delete.search.placeholder").toLowerCase())
                    || v.getTitulo().toLowerCase().contains(texto)) {
                modelo.addRow(new Object[]{v.getId(), v.getTitulo(), v.getPlataforma(),
                        v.getFechaMostrada(), v.getValoracionMostrada(),
                        v.isFavorito() ? texts.getString("delete.yes") : texts.getString("delete.no")});
            }
        }
    }

    private void limpiarBusqueda() {
        txtBuscar.setText(""); txtBuscar.setForeground(TEXT_PRIMARY);
        tabla.clearSelection(); cargarVideojuegos(); txtBuscar.requestFocusInWindow();
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, texts.getString("delete.select.warning"),
                texts.getString("delete.select.title"), JOptionPane.WARNING_MESSAGE); return; }
        String titulo = (String) modelo.getValueAt(fila, 1);
        int confirmar = JOptionPane.showConfirmDialog(this,
                java.text.MessageFormat.format(texts.getString("delete.confirm.text"), titulo),
                texts.getString("delete.confirm.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmar != JOptionPane.YES_OPTION) return;
        int id = (int) modelo.getValueAt(fila, 0);
        try {
            controller.eliminarVideojuego(id);
            JOptionPane.showMessageDialog(this, texts.getString("delete.success"),
                    texts.getString("delete.success.title"), JOptionPane.INFORMATION_MESSAGE);
            cargarVideojuegos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, texts.getString("delete.error.generic"),
                    texts.getString("delete.error.title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAtras() {
        dispose();
        if (padre != null) { padre.setVisible(true); padre.toFront(); }
        else { controller.volverModificar(); }
    }

    private JButton crearBotonMorado(String texto, Dimension tam, java.awt.event.ActionListener a) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, PURPLE_LIGHT, 0, getHeight(), PURPLE_DARK));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE); b.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setOpaque(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(tam); b.addActionListener(a); return b;
    }

    private JButton crearBotonRojo(String texto, Dimension tam, java.awt.event.ActionListener a) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, RED_ACCENT, 0, getHeight(), RED_DARK));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE); b.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setOpaque(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(tam); b.addActionListener(a); return b;
    }

    private JButton crearBotonLimpiar() {
        JButton b = new JButton("X"); b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setForeground(TEXT_SECONDARY); b.setFocusPainted(false); b.setBorderPainted(false);
        b.setContentAreaFilled(false); b.setMargin(new Insets(0,0,0,0));
        b.setPreferredSize(new Dimension(26, 30)); b.setToolTipText(texts.getString("search.clear.tooltip"));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); return b;
    }

    private JPanel crearCampoBusquedaConLimpiar(JTextField campo, JButton limpiar) {
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(FIELD_BG);
        p.setBorder(BorderFactory.createLineBorder(SEPARATOR)); p.setPreferredSize(new Dimension(280, 34));
        campo.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 4)); campo.setOpaque(false);
        p.add(campo, BorderLayout.CENTER); p.add(limpiar, BorderLayout.EAST); return p;
    }

    private void ponerPlaceholder(JTextField campo, String texto) {
        campo.setText(texto); campo.setForeground(TEXT_SECONDARY);
        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (campo.getText().equals(texto)) { campo.setText(""); campo.setForeground(TEXT_PRIMARY); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) { campo.setText(texto); campo.setForeground(TEXT_SECONDARY); }
            }
        });
    }
}
