package Vista;

import Controlador.PerfilController;
import Modelo.Usuario;
import java.awt.*;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Vista encargada de mostrar y gestionar la información del perfil
 * del usuario autenticado.
 * <p>
 * Layout de dos columnas: izquierda con datos personales y acciones,
 * derecha con estadísticas y recomendaciones.
 * Estética oscura con acentos morados, coherente con el resto de la app.
 * </p>
 *
 * @author Raquel
 * @version 3.0
 */
public class PerfilVista extends JFrame {

    // Paleta de colores
    private static final Color BG_DARK = new Color(18, 10, 35);
    private static final Color BG_CARD = new Color(35, 20, 60);
    private static final Color BG_CARD2 = new Color(28, 15, 50);
    private static final Color PURPLE_LIGHT = new Color(180, 120, 255);
    private static final Color PURPLE_DARK = new Color(110, 40, 180);
    private static final Color PURPLE_MID = new Color(140, 80, 220);
    private static final Color TEXT_PRIMARY = new Color(240, 235, 255);
    private static final Color TEXT_SECONDARY = new Color(170, 150, 210);
    private static final Color SEPARATOR = new Color(70, 45, 110);

    /** Usuario autenticado */
    private Usuario usuario;

    /** Controlador asociado a la vista */
    private PerfilController controller;

    /** Etiqueta que muestra el nombre del usuario */
    private JLabel lblNombreValor;

    /** Etiqueta que muestra el total de videojuegos */
    private JLabel lblTotalNumero;

    /** Panel donde se inserta el gráfico de plataformas */
    private JPanel panelGraficoPlataformas;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /**
     * Constructor de la vista de perfil.
     *
     * @param usuario usuario autenticado
     */
    public PerfilVista(Usuario usuario) {
        this.usuario = usuario;
        this.texts = ResourceBundle.getBundle("i18n.messages");

        controller = new PerfilController(usuario);
        controller.setVista(this);

        setTitle(texts.getString("profile.window.title"));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addWindowStateListener(e -> MenuPrincipalVista.mantenerMaximizada(this));

        initComponents();
        cargarTotalJuegos();
        cargarGraficoPlataformas();
    }

    /**
     * Inicializa y organiza todos los componentes gráficos de la vista.
     * Layout de dos columnas sobre fondo oscuro.
     */
    private void initComponents() {

        // Fondo principal oscuro
        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, BG_DARK, getWidth(), getHeight(),
                        new Color(28, 10, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        /* ===== CABECERA ===== */
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.setBorder(BorderFactory.createEmptyBorder(30, 50, 15, 50));

        JLabel lblTitulo = new JLabel(texts.getString("profile.title"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 38));
        lblTitulo.setForeground(TEXT_PRIMARY);
        cabecera.add(lblTitulo, BorderLayout.CENTER);

        // Línea decorativa bajo el título
        JPanel lineaTitulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 0),
                        getWidth(), 0, PURPLE_MID));
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(0, 1, getWidth(), 1);
                g2.dispose();
            }
        };
        lineaTitulo.setOpaque(false);
        lineaTitulo.setPreferredSize(new Dimension(0, 4));
        cabecera.add(lineaTitulo, BorderLayout.SOUTH);

        root.add(cabecera, BorderLayout.NORTH);

        /* ===== CUERPO: DOS COLUMNAS ===== */
        JPanel cuerpo = new JPanel(new GridLayout(1, 2, 25, 0));
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        cuerpo.add(crearColumnaIzquierda());
        cuerpo.add(crearColumnaDerecha());

        root.add(cuerpo, BorderLayout.CENTER);

        /* ===== PIE: BOTÓN ATRÁS ===== */
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(5, 40, 18, 40));

        JButton btnAtras = crearBotonMorado(texts.getString("profile.back"), new Dimension(120, 34));
        btnAtras.addActionListener(e -> controller.volverMenu());
        pie.add(btnAtras);
        root.add(pie, BorderLayout.SOUTH);
    }

    /**
     * Crea la columna izquierda con los datos del usuario y los botones de acción.
     */
    private JPanel crearColumnaIzquierda() {

        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        // ---- TARJETA: DATOS DEL USUARIO ----
        JPanel tarjetaDatos = crearTarjeta();
        tarjetaDatos.setLayout(new BoxLayout(tarjetaDatos, BoxLayout.Y_AXIS));

        // Panel superior: botón contraseña arriba a la derecha
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton btnPass = crearBotonMorado(texts.getString("profile.change.password"), new Dimension(200, 36));
        btnPass.setFont(new Font("Segoe UI Black", Font.PLAIN, 13));
        btnPass.addActionListener(e -> cambiarContrasena());
        JPanel panelBtnPass = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBtnPass.setOpaque(false);
        panelBtnPass.add(btnPass);
        panelSuperior.add(panelBtnPass, BorderLayout.NORTH);

        // Avatar más abajo, centrado
        JLabel avatar = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, PURPLE_LIGHT, getWidth(), getHeight(), PURPLE_DARK));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI Black", Font.PLAIN, 36));
                FontMetrics fm = g2.getFontMetrics();
                String inicial = usuario.getNombre() != null && !usuario.getNombre().isEmpty()
                        ? String.valueOf(usuario.getNombre().charAt(0)).toUpperCase()
                        : "?";
                g2.drawString(inicial,
                        (getWidth() - fm.stringWidth(inicial)) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(90, 90));
        JPanel avatarCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
        avatarCentro.setOpaque(false);
        avatarCentro.add(avatar);
        panelSuperior.add(avatarCentro, BorderLayout.CENTER);

        tarjetaDatos.add(panelSuperior);
        tarjetaDatos.add(Box.createRigidArea(new Dimension(0, 8)));

        // Datos del usuario
        tarjetaDatos.add(crearFilaDato(texts.getString("profile.name") + ":",
                usuario.getNombre() != null ? usuario.getNombre() : "-", true));
        tarjetaDatos.add(Box.createRigidArea(new Dimension(0, 2)));
        tarjetaDatos.add(crearSeparador());
        tarjetaDatos.add(Box.createRigidArea(new Dimension(0, 2)));
        tarjetaDatos.add(crearFilaDato(texts.getString("profile.username") + ":", usuario.getUsername(), false));
        tarjetaDatos.add(Box.createRigidArea(new Dimension(0, 2)));
        tarjetaDatos.add(crearSeparador());
        tarjetaDatos.add(Box.createRigidArea(new Dimension(0, 2)));
        tarjetaDatos.add(crearFilaDato(texts.getString("profile.email") + ":", usuario.getEmail(), false));
        tarjetaDatos.add(Box.createRigidArea(new Dimension(0, 15)));

        col.add(tarjetaDatos);
        col.add(Box.createRigidArea(new Dimension(0, 70)));

        // ---- TARJETA: TOTAL JUEGOS ----
        JPanel tarjetaTotal = crearTarjeta();
        tarjetaTotal.setLayout(new BoxLayout(tarjetaTotal, BoxLayout.Y_AXIS));

        tarjetaTotal.add(Box.createRigidArea(new Dimension(0, 55)));

        JLabel lblTotalTxt = new JLabel(texts.getString("profile.total.text"));
        lblTotalTxt.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTotalTxt.setForeground(TEXT_SECONDARY);
        lblTotalTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjetaTotal.add(lblTotalTxt);

        tarjetaTotal.add(Box.createRigidArea(new Dimension(0, 26)));

        lblTotalNumero = new JLabel("0", SwingConstants.CENTER);
        lblTotalNumero.setFont(new Font("Showcard Gothic", Font.PLAIN, 90));
        lblTotalNumero.setForeground(PURPLE_LIGHT);
        lblTotalNumero.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotalNumero.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalNumero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        lblTotalNumero.setPreferredSize(new Dimension(Integer.MAX_VALUE, 95));
        tarjetaTotal.add(lblTotalNumero);

        JLabel lblJuegosLabel = new JLabel(texts.getString("profile.total.label"), SwingConstants.CENTER);
        lblJuegosLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblJuegosLabel.setForeground(TEXT_SECONDARY);
        lblJuegosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblJuegosLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblJuegosLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        tarjetaTotal.add(lblJuegosLabel);
        tarjetaTotal.add(Box.createRigidArea(new Dimension(0, 8)));

        col.add(tarjetaTotal);
        col.add(Box.createVerticalGlue());

        return col;
    }

    /**
     * Crea la columna derecha con el gráfico de plataformas y el botón de
     * recomendación.
     */
    private JPanel crearColumnaDerecha() {

        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        // ---- TARJETA: GRÁFICO ----
        JPanel tarjetaGrafico = crearTarjeta();
        tarjetaGrafico.setLayout(new BorderLayout(0, 8));

        JLabel lblGrafico = new JLabel(texts.getString("profile.chart.title"), SwingConstants.CENTER);
        lblGrafico.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        lblGrafico.setForeground(TEXT_PRIMARY);
        lblGrafico.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        tarjetaGrafico.add(lblGrafico, BorderLayout.NORTH);

        panelGraficoPlataformas = new JPanel(new BorderLayout());
        panelGraficoPlataformas.setOpaque(false);
        panelGraficoPlataformas.setPreferredSize(new Dimension(400, 320));
        tarjetaGrafico.add(panelGraficoPlataformas, BorderLayout.CENTER);

        col.add(tarjetaGrafico);
        col.add(Box.createRigidArea(new Dimension(0, 18)));

        // ---- TARJETA: RECOMENDAR (dos botones directos) ----
        JPanel tarjetaRec = crearTarjeta();
        tarjetaRec.setLayout(new BoxLayout(tarjetaRec, BoxLayout.Y_AXIS));

        JLabel lblRecTitulo = new JLabel(texts.getString("profile.rec.title"));
        lblRecTitulo.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        lblRecTitulo.setForeground(TEXT_PRIMARY);
        lblRecTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjetaRec.add(lblRecTitulo);

        tarjetaRec.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel lblRecDesc = new JLabel(
                "<html><div style='text-align:center;'>" + texts.getString("profile.rec.desc") + "</div></html>",
                SwingConstants.CENTER);
        lblRecDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRecDesc.setForeground(TEXT_SECONDARY);
        lblRecDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRecDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tarjetaRec.add(lblRecDesc);

        tarjetaRec.add(Box.createRigidArea(new Dimension(0, 14)));

        // Panel con dos tarjetas lado a lado
        JPanel panelTarjetasRec = new JPanel(new GridLayout(1, 2, 14, 0));
        panelTarjetasRec.setOpaque(false);
        panelTarjetasRec.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTarjetasRec.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JButton btnPorPlataforma = crearTarjetaRecomendacion(
                "🎮", texts.getString("profile.rec.plataforma.titulo"),
                texts.getString("profile.rec.plataforma.sub"),
                new Color(110, 40, 180), new Color(80, 20, 140)
        );
        btnPorPlataforma.addActionListener(e -> {
            String plataforma = controller.obtenerPlataformaMasJugada();
            if (plataforma == null || plataforma.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        texts.getString("profile.rec.nodata.plataforma"),
                        texts.getString("profile.rec.nodata.title"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                new RecomendacionesVista(this, usuario, "plataforma", plataforma).setVisible(true);
            }
        });

        JButton btnPorGenero = crearTarjetaRecomendacion(
                "⚔️", texts.getString("profile.rec.genero.titulo"),
                texts.getString("profile.rec.genero.sub"),
                new Color(60, 60, 160), new Color(40, 40, 120)
        );
        btnPorGenero.addActionListener(e -> {
            String genero = controller.obtenerGeneroMasJugado();
            if (genero == null || genero.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        texts.getString("profile.rec.nodata.genero"),
                        texts.getString("profile.rec.nodata.title"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                new RecomendacionesVista(this, usuario, "genero", genero).setVisible(true);
            }
        });

        panelTarjetasRec.add(btnPorPlataforma);
        panelTarjetasRec.add(btnPorGenero);
        tarjetaRec.add(panelTarjetasRec);
        tarjetaRec.add(Box.createRigidArea(new Dimension(0, 8)));

        col.add(tarjetaRec);
        col.add(Box.createVerticalGlue());

        return col;
    }

    /**
     * Crea una tarjeta con fondo redondeado y padding interno.
     */
    private JPanel crearTarjeta() {
        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                // Borde sutil
                g2.setColor(SEPARATOR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return tarjeta;
    }

    /**
     * Crea una fila de dato (label + valor) para la tarjeta de datos del usuario.
     * Si es editable, muestra la etiqueta del nombre que se puede actualizar.
     */
    private JPanel crearFilaDato(String label, String valor, boolean esNombre) {
        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, esNombre ? 65 : 62));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setMinimumSize(new Dimension(0, 30));
        fila.add(lbl, BorderLayout.WEST);

        if (esNombre) {
            lblNombreValor = new JLabel(valor);
            lblNombreValor.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            lblNombreValor.setForeground(TEXT_PRIMARY);
            // Icono lápiz relleno con separación del nombre
            JLabel lblLapiz = new JLabel("  \uD83D\uDD8A");
            lblLapiz.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            lblLapiz.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
            lblLapiz.setForeground(new Color(160, 110, 240));
            lblLapiz.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblLapiz.setToolTipText(texts.getString("profile.tooltip.edit"));
            lblLapiz.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    editarNombre();
                }

                public void mouseEntered(java.awt.event.MouseEvent e) {
                    lblLapiz.setForeground(PURPLE_LIGHT);
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    lblLapiz.setForeground(new Color(140, 100, 220));
                }
            });

            // Panel nombre + lápiz juntos
            JPanel panelNombreLapiz = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panelNombreLapiz.setOpaque(false);
            panelNombreLapiz.add(lblNombreValor);
            panelNombreLapiz.add(lblLapiz);
            fila.add(panelNombreLapiz, BorderLayout.CENTER);
        } else {
            JLabel val = new JLabel(valor);
            val.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            val.setForeground(TEXT_PRIMARY);
            fila.add(val, BorderLayout.CENTER);
        }

        return fila;
    }

    /**
     * Crea una línea separadora sutil entre filas de datos.
     */
    private JPanel crearSeparador() {
        JPanel sep = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(SEPARATOR);
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        sep.setOpaque(false);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setPreferredSize(new Dimension(0, 1));
        return sep;
    }

    /* ================= DATOS ================= */

    private void cargarTotalJuegos() {
        lblTotalNumero.setText(String.valueOf(controller.obtenerTotalJuegos()));
    }

    private void cargarGraficoPlataformas() {
        Map<String, Long> datos = controller.obtenerConteoPlataformas();
        panelGraficoPlataformas.removeAll();

        if (datos.isEmpty()) {
            JLabel lblSin = new JLabel("Aún no hay juegos registrados.", SwingConstants.CENTER);
            lblSin.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblSin.setForeground(TEXT_SECONDARY);
            panelGraficoPlataformas.add(lblSin, BorderLayout.CENTER);
        } else {
            DefaultPieDataset dataset = new DefaultPieDataset();
            // Paleta morada personalizada para las secciones
            Color[] coloresSecciones = {
                    new Color(180, 120, 255), new Color(110, 40, 180),
                    new Color(220, 160, 255), new Color(80, 20, 140),
                    new Color(150, 90, 220), new Color(60, 10, 120),
                    new Color(200, 140, 255), new Color(130, 60, 200)
            };

            for (Map.Entry<String, Long> e : datos.entrySet()) {
                dataset.setValue(e.getKey(), e.getValue());
            }

            RingPlot plot = new RingPlot(dataset);
            plot.setSectionDepth(0.40);
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
            plot.setBackgroundPaint(BG_CARD2);
            plot.setOutlineVisible(false);
            plot.setShadowPaint(null);
            plot.setLabelBackgroundPaint(new Color(40, 24, 70));
            plot.setLabelOutlinePaint(SEPARATOR);
            plot.setLabelShadowPaint(null);
            plot.setLabelFont(new Font("Segoe UI", Font.BOLD, 11));
            plot.setLabelPaint(TEXT_PRIMARY);

            // Aplicar colores personalizados a cada sección
            int idx = 0;
            for (Object key : dataset.getKeys()) {
                plot.setSectionPaint((Comparable<?>) key,
                        coloresSecciones[idx % coloresSecciones.length]);
                idx++;
            }

            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBackgroundPaint(BG_CARD2);
            chart.getLegend().setBackgroundPaint(BG_CARD2);
            chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 11));
            chart.getLegend().setItemPaint(TEXT_PRIMARY);

            ChartPanel cp = new ChartPanel(chart);
            cp.setPreferredSize(new Dimension(400, 300));
            cp.setMouseWheelEnabled(false);
            cp.setPopupMenu(null);
            cp.setBackground(BG_CARD2);
            cp.setOpaque(false);

            panelGraficoPlataformas.add(cp, BorderLayout.CENTER);
        }

        panelGraficoPlataformas.revalidate();
        panelGraficoPlataformas.repaint();
    }

    /* ================= EDITAR NOMBRE ================= */

    private void editarNombre() {
        JTextField txtNombre = new JTextField(usuario.getNombre());
        int opcion = JOptionPane.showConfirmDialog(this, txtNombre,
                texts.getString("profile.edit.dialog.title"), JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION)
            return;

        String nuevoNombre = txtNombre.getText().trim();
        if (nuevoNombre.isEmpty() || nuevoNombre.equals(usuario.getNombre()))
            return;

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres cambiar tu nombre de \"" +
                        usuario.getNombre() + "\" a \"" + nuevoNombre + "\"?",
                texts.getString("profile.edit.confirm.title"), JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION)
            return;

        controller.actualizarNombre(nuevoNombre);
        usuario.setNombre(nuevoNombre);
        lblNombreValor.setText(nuevoNombre);

        JOptionPane.showMessageDialog(this,
                texts.getString("profile.edit.success"),
                texts.getString("profile.edit.success.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    /* ================= CAMBIAR CONTRASEÑA ================= */

    private void cambiarContrasena() {
        JLabel lblSub = new JLabel(texts.getString("profile.password.subtitle"));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPasswordField txtPassword = new JPasswordField(22);

        JButton btnOjo = new JButton("👁");
        btnOjo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnOjo.setForeground(Color.GRAY);
        btnOjo.setFocusPainted(false);
        btnOjo.setBorderPainted(false);
        btnOjo.setContentAreaFilled(false);
        btnOjo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOjo.setMargin(new Insets(0, 4, 0, 4));
        btnOjo.addActionListener(e -> {
            if (txtPassword.getEchoChar() != (char) 0) {
                txtPassword.setEchoChar((char) 0);
                btnOjo.setText("<html><s>👁</s></html>");
            } else {
                txtPassword.setEchoChar('•');
                btnOjo.setText("👁");
            }
        });

        JPanel wrapPassword = new JPanel(new BorderLayout());
        wrapPassword.add(txtPassword, BorderLayout.CENTER);
        wrapPassword.add(btnOjo, BorderLayout.EAST);

        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 8));
        panel.add(lblSub);
        panel.add(wrapPassword);

        int opcion = JOptionPane.showConfirmDialog(this, panel,
                texts.getString("profile.password.title"), JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) return;

        String nuevaPassword = new String(txtPassword.getPassword()).trim();
        if (nuevaPassword.isEmpty()) return;

        if (!nuevaPassword.matches("^(?=.*[a-zA-Z]).{8,}$")) {
            JOptionPane.showMessageDialog(this,
                    texts.getString("profile.password.error.format"),
                    texts.getString("profile.password.error.title"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                texts.getString("profile.password.confirm"),
                texts.getString("profile.password.confirm.title"),
                JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;

        controller.actualizarPassword(nuevaPassword);
        JOptionPane.showMessageDialog(this,
                texts.getString("profile.password.success"),
                texts.getString("profile.password.success.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    /* ================= TARJETA RECOMENDACION ================= */

    /**
     * Crea un botón estilo tarjeta grande para las opciones de recomendación.
     *
     * @param titulo     título principal de la tarjeta
     * @param subtitulo  descripción secundaria
     * @param colorTop   color superior del degradado
     * @param colorBot   color inferior del degradado
     * @return botón configurado como tarjeta
     */
    private JButton crearTarjetaRecomendacion(String icono, String titulo, String subtitulo,
            Color colorTop, Color colorBot) {

        JButton boton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, colorTop, 0, getHeight(), colorBot));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // Icono centrado (se elimina el selector de variante U+FE0F para medir bien)
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
                g2.setColor(Color.WHITE);
                String iconoMedida = icono.replace("️", "");
                FontMetrics fmI = g2.getFontMetrics();
                int xIcono = (getWidth() - fmI.stringWidth(iconoMedida.isEmpty() ? icono : iconoMedida)) / 2;
                g2.drawString(icono, xIcono, getHeight() / 2 - 16);

                // Título en negrita centrado
                g2.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
                FontMetrics fmT = g2.getFontMetrics();
                g2.drawString(titulo, (getWidth() - fmT.stringWidth(titulo)) / 2, getHeight() / 2 + 10);

                // Subtítulo partido en dos líneas
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.setColor(new Color(220, 200, 255));
                FontMetrics fmS = g2.getFontMetrics();
                String[] partes = subtitulo.split("\n");
                int yS = getHeight() / 2 + 26;
                for (String parte : partes) {
                    g2.drawString(parte, (getWidth() - fmS.stringWidth(parte)) / 2, yS);
                    yS += fmS.getHeight();
                }

                g2.dispose();
            }
        };

        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBorder(null);
            }
        });

        return boton;
    }

    /* ================= BOTÓN MORADO ================= */

    private JButton crearBotonMorado(String texto, Dimension tamaño) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, PURPLE_LIGHT, 0, getHeight(), PURPLE_DARK));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
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
        boton.setMaximumSize(tamaño);
        return boton;
    }
}
