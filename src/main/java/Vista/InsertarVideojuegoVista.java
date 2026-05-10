package Vista;

import Controlador.InsertarVideojuegoController;
import Modelo.DAO.Imp.PlataformaDAO_imp;
import Modelo.DAO.PlataformaDAO;
import Modelo.Plataforma;
import Modelo.Usuario;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ResourceBundle;

public class InsertarVideojuegoVista extends JFrame {

    private static final Color BG_DARK       = new Color(18, 10, 35);
    private static final Color BG_CARD       = new Color(35, 20, 60);
    private static final Color PURPLE_LIGHT  = new Color(180, 120, 255);
    private static final Color PURPLE_DARK   = new Color(110, 40, 180);
    private static final Color TEXT_PRIMARY  = new Color(240, 235, 255);
    private static final Color TEXT_SECONDARY= new Color(170, 150, 210);
    private static final Color SEPARATOR     = new Color(70, 45, 110);
    private static final Color FIELD_BG      = new Color(28, 15, 50);

    private JTextField txtTitulo, txtAnio;
    private JComboBox<Plataforma> cmbPlataforma;
    private JComboBox<String> cmbGenero;
    private JComboBox<Integer> cmbValoracion;
    private JTextArea txtAnotaciones;
    private JLabel lblContadorAnotaciones;
    private JToggleButton btnFavorito;
    private JButton btnAceptar, btnCancelar;
    private Usuario usuario;
    private ModificarVideojuegosVista padre;
    private ResourceBundle texts;
    private InsertarVideojuegoController controller;
    private String imagenUrlDesdeAPI = null;
    private boolean vieneDesdeCatalogo = false;
    private static final int MAX_ANOTACIONES = 120;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private static final String[] GENEROS = {
        "", "Action", "Adventure", "RPG", "Strategy", "Shooter",
        "Puzzle", "Arcade", "Platformer", "Racing", "Sports",
        "Fighting", "Simulation", "Family", "Board Games",
        "Educational", "Card", "Casual", "Massively Multiplayer", "Indie"
    };

    public InsertarVideojuegoVista(Usuario usuario) { this(usuario, null); }

    public InsertarVideojuegoVista(Usuario usuario, ModificarVideojuegosVista padre) {
        this(usuario, padre, false);
    }

    public InsertarVideojuegoVista(Usuario usuario, ModificarVideojuegosVista padre, boolean vieneDesdeCatalogo) {
        this.usuario = usuario;
        this.padre = padre;
        this.vieneDesdeCatalogo = vieneDesdeCatalogo;
        texts = ResourceBundle.getBundle("i18n.messages");
        controller = new InsertarVideojuegoController(usuario);
        controller.setVista(this);
        setTitle(texts.getString("insert.window.title"));
        setSize(660, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(padre != null ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    public void prerellenarDesdeAPI(String titulo, String plataforma, Integer anio, String genero, String imagenUrl) {
        txtTitulo.setText(titulo);
        txtAnio.setText("");
        this.imagenUrlDesdeAPI = imagenUrl;
        for (int i = 0; i < cmbPlataforma.getItemCount(); i++) {
            if (cmbPlataforma.getItemAt(i).getNombre().equals(plataforma)) {
                cmbPlataforma.setSelectedIndex(i); break;
            }
        }
        if (genero != null && !genero.isEmpty()) {
            for (int i = 0; i < cmbGenero.getItemCount(); i++) {
                if (cmbGenero.getItemAt(i).equalsIgnoreCase(genero)) {
                    cmbGenero.setSelectedIndex(i); break;
                }
            }
        }
        cmbGenero.setEnabled(false);
        txtTitulo.setEditable(false);
        txtTitulo.setBackground(new Color(45, 28, 75));
        txtTitulo.setForeground(TEXT_SECONDARY);
        cmbPlataforma.setEnabled(false);
    }

    private void initComponents() {
        JPanel fondo = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, BG_DARK, getWidth(), getHeight(), new Color(28, 10, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        fondo.setOpaque(false);
        setContentPane(fondo);

        JPanel tarjeta = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(SEPARATOR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(25, 35, 15, 35));

        JPanel centrador = new JPanel(new GridBagLayout());
        centrador.setOpaque(false);
        centrador.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.fill = GridBagConstraints.BOTH; gbcC.weightx = 1; gbcC.weighty = 1;
        centrador.add(tarjeta, gbcC);
        fondo.add(centrador, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 10, 7, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel(texts.getString("insert.title"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 30));
        lblTitulo.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        tarjeta.add(lblTitulo, gbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(SEPARATOR); sep.setBackground(SEPARATOR);
        gbc.gridy++; gbc.insets = new Insets(0, 10, 10, 10);
        tarjeta.add(sep, gbc);
        gbc.insets = new Insets(7, 10, 7, 10);
        gbc.gridwidth = 1;

        Font fl = new Font("Segoe UI", Font.BOLD, 14);

        gbc.gridy++; gbc.gridx = 0; tarjeta.add(crearLabel(texts.getString("insert.field.title") + ":", fl), gbc);
        txtTitulo = crearCampo(); gbc.gridx = 1; tarjeta.add(txtTitulo, gbc);

        gbc.gridy++; gbc.gridx = 0; tarjeta.add(crearLabel(texts.getString("insert.field.platform") + ":", fl), gbc);
        cmbPlataforma = new JComboBox<>(); cargarPlataformas(); estilizarCombo(cmbPlataforma);
        gbc.gridx = 1; tarjeta.add(cmbPlataforma, gbc);

        gbc.gridy++; gbc.gridx = 0; tarjeta.add(crearLabel(texts.getString("insert.field.year") + ":", fl), gbc);
        txtAnio = crearCampo(); gbc.gridx = 1; tarjeta.add(txtAnio, gbc);

        gbc.gridy++; gbc.gridx = 0; tarjeta.add(crearLabel("Género:", fl), gbc);
        cmbGenero = new JComboBox<>(GENEROS); estilizarCombo(cmbGenero);
        cmbGenero.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? PURPLE_DARK : FIELD_BG);
                if (value == null || value.toString().isEmpty()) {
                    setText("— Sin género —"); setForeground(TEXT_SECONDARY);
                } else { setForeground(TEXT_PRIMARY); }
                return this;
            }
        });
        gbc.gridx = 1; tarjeta.add(cmbGenero, gbc);

        gbc.gridy++; gbc.gridx = 0; tarjeta.add(crearLabel(texts.getString("insert.field.rating") + ":", fl), gbc);
        cmbValoracion = new JComboBox<>();
        for (int i = 1; i <= 10; i++) cmbValoracion.addItem(i);
        estilizarCombo(cmbValoracion);
        gbc.gridx = 1; tarjeta.add(cmbValoracion, gbc);

        gbc.gridy++; gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        tarjeta.add(crearLabel(texts.getString("insert.field.annotation") + ":", fl), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        txtAnotaciones = new JTextArea(5, 20);
        txtAnotaciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAnotaciones.setLineWrap(true); txtAnotaciones.setWrapStyleWord(true);
        txtAnotaciones.setBackground(FIELD_BG); txtAnotaciones.setForeground(TEXT_PRIMARY);
        txtAnotaciones.setCaretColor(TEXT_PRIMARY);
        txtAnotaciones.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        limitarCaracteres(txtAnotaciones, MAX_ANOTACIONES);
        JScrollPane sc = new JScrollPane(txtAnotaciones);
        sc.setPreferredSize(new Dimension(340, 110));
        sc.setBorder(BorderFactory.createLineBorder(SEPARATOR));
        sc.getViewport().setBackground(FIELD_BG);
        gbc.gridx = 1; tarjeta.add(crearPanelAnotaciones(sc, new Dimension(340, 130)), gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel pf = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); pf.setOpaque(false);
        btnFavorito = new JToggleButton();
        btnFavorito.setIcon(crearIconoEstrella(38, 38, false));
        btnFavorito.setSelectedIcon(crearIconoEstrella(38, 38, true));
        btnFavorito.setBorderPainted(false); btnFavorito.setContentAreaFilled(false);
        btnFavorito.setFocusPainted(false); btnFavorito.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel lF = new JLabel(texts.getString("insert.favorite"));
        lF.setFont(new Font("Segoe UI", Font.BOLD, 14)); lF.setForeground(TEXT_PRIMARY);
        pf.add(btnFavorito); pf.add(lF); tarjeta.add(pf, gbc);

        JPanel pi = new JPanel(new BorderLayout());
        pi.setOpaque(false); pi.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        btnCancelar = boton(texts.getString("insert.cancel"), new Dimension(130, 36), e -> volverAtras());
        btnAceptar  = boton(texts.getString("insert.accept"),  new Dimension(210, 42), e -> validarYProcesar());
        getRootPane().setDefaultButton(btnAceptar);
        pi.add(btnCancelar, BorderLayout.WEST); pi.add(btnAceptar, BorderLayout.EAST);
        fondo.add(pi, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel lbl = new JLabel(texto); lbl.setFont(fuente); lbl.setForeground(TEXT_SECONDARY); return lbl;
    }

    private JTextField crearCampo() {
        JTextField c = new JTextField();
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setPreferredSize(new Dimension(340, 34));
        c.setBackground(FIELD_BG); c.setForeground(TEXT_PRIMARY); c.setCaretColor(TEXT_PRIMARY);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SEPARATOR), BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return c;
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(340, 34));
        combo.setBackground(FIELD_BG); combo.setForeground(TEXT_PRIMARY);
    }

    private void cargarPlataformas() {
        try {
            PlataformaDAO dao = new PlataformaDAO_imp();
            for (Plataforma p : dao.fetchAll()) cmbPlataforma.addItem(p);
        } catch (Exception e) { System.err.println("Error plataformas: " + e.getMessage()); }
    }

    private void limitarCaracteres(JTextArea area, int max) {
        ((AbstractDocument) area.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                int esp = max - fb.getDocument().getLength();
                if (esp <= 0) return;
                super.insertString(fb, offset, string.substring(0, Math.min(string.length(), esp)), attr);
            }
            @Override public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) { super.replace(fb, offset, length, null, attrs); return; }
                int esp = max - (fb.getDocument().getLength() - length);
                if (esp <= 0) return;
                super.replace(fb, offset, length, text.substring(0, Math.min(text.length(), esp)), attrs);
            }
        });
    }

    private JPanel crearPanelAnotaciones(JScrollPane scroll, Dimension tamano) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false); panel.setPreferredSize(tamano); panel.setMinimumSize(tamano);
        panel.add(scroll, BorderLayout.CENTER);
        lblContadorAnotaciones = new JLabel("0/" + MAX_ANOTACIONES, SwingConstants.RIGHT);
        lblContadorAnotaciones.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblContadorAnotaciones.setForeground(TEXT_SECONDARY);
        panel.add(lblContadorAnotaciones, BorderLayout.SOUTH);
        txtAnotaciones.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { act(); }
            @Override public void removeUpdate(DocumentEvent e) { act(); }
            @Override public void changedUpdate(DocumentEvent e) { act(); }
            void act() { lblContadorAnotaciones.setText(txtAnotaciones.getText().length() + "/" + MAX_ANOTACIONES); }
        });
        return panel;
    }

    private JButton boton(String texto, Dimension tam, ActionListener a) {
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

    private void volverAtras() {
        if (vieneDesdeCatalogo && padre != null) { dispose(); new BuscarEnCatalogoDialog(padre, usuario).setVisible(true); return; }
        if (padre != null) { dispose(); padre.setVisible(true); new SeleccionarModoInsertarDialog(padre, usuario).setVisible(true); }
        else { controller.volverModificar(); }
    }

    private void validarYProcesar() {
        if (txtTitulo.getText().trim().isEmpty() || txtAnio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, texts.getString("insert.error.empty"),
                    texts.getString("insert.error.empty.title"), JOptionPane.WARNING_MESSAGE); return;
        }
        String anio;
        try { anio = normalizarFecha(txtAnio.getText().trim()); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, texts.getString("insert.error.year")); return; }
        Plataforma p = (Plataforma) cmbPlataforma.getSelectedItem();
        String plat = p != null ? p.getNombre() : "";
        BigDecimal val = new BigDecimal((Integer) cmbValoracion.getSelectedItem());
        String anot = txtAnotaciones.getText().trim();
        String gen = (String) cmbGenero.getSelectedItem();
        if (gen == null) gen = "";
        try {
            if (imagenUrlDesdeAPI != null || !gen.isEmpty()) {
                controller.insertarVideojuegoDesdeAPI(txtTitulo.getText().trim(), plat, anio, val, anot, btnFavorito.isSelected(), gen, imagenUrlDesdeAPI);
            } else {
                controller.insertarVideojuego(txtTitulo.getText().trim(), plat, anio, val, anot, btnFavorito.isSelected());
            }
            JOptionPane.showMessageDialog(this, texts.getString("insert.success") +
                    (btnFavorito.isSelected() ? " " + texts.getString("insert.success.favorite") : ""),
                    texts.getString("insert.success.title"), JOptionPane.INFORMATION_MESSAGE);
            volverModificarDespuesDeGuardar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, texts.getString("insert.error.generic"),
                    texts.getString("insert.error.title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverModificarDespuesDeGuardar() {
        if (padre != null) { dispose(); padre.setVisible(true); padre.toFront(); }
        else { controller.volverModificar(); }
    }

    private String normalizarFecha(String input) {
        if (input.matches("\\d{2}/\\d{2}/\\d{4}")) {
            LocalDate f = LocalDate.parse(input, FORMATO_FECHA);
            return f.format(FORMATO_FECHA);
        }
        throw new IllegalArgumentException("La fecha debe usar dd/MM/yyyy");
    }

    private Icon crearIconoEstrella(int ancho, int alto, boolean selected) {
        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double cx = ancho / 2.0, cy = alto / 2.0;
        double rExt = Math.min(ancho, alto) * 0.46, rInt = rExt * 0.42;
        Path2D star = new Path2D.Double();
        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(-90 + i * 36);
            double r = (i % 2 == 0) ? rExt : rInt;
            double x = cx + Math.cos(angle) * r, y = cy + Math.sin(angle) * r;
            if (i == 0) star.moveTo(x, y); else star.lineTo(x, y);
        }
        star.closePath();
        g2.setColor(selected ? new Color(255, 210, 50) : new Color(180, 180, 180));
        g2.fill(star);
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(selected ? new Color(190, 140, 10) : new Color(120, 120, 120));
        g2.draw(star);
        g2.dispose();
        return new ImageIcon(img);
    }
}
