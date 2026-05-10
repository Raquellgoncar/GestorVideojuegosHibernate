/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.ListadoVideojuegosController;
import Modelo.DAO.Imp.PlataformaDAO_imp;
import Modelo.DAO.PlataformaDAO;
import Modelo.Plataforma;
import Modelo.Videojuego;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Vista de listado de videojuegos del usuario.
 * <p>
 * Muestra en tarjetas todos los videojuegos asociados al usuario
 * autenticado, junto con información básica como plataforma, año,
 * valoración y anotaciones. Los favoritos se destacan con una estrella dorada.
 * </p>
 *
 * Sigue el patrón MVC, delegando la obtención de datos y la navegación
 * en {@link ListadoVideojuegosController}.
 *
 * @author Raquel
 * @version 2.0
 */
public class ListadoVideojuegosVista extends JFrame {

    /** Usuario autenticado */
    private Usuario usuario;

    /** Panel que contiene las tarjetas de videojuegos */
    private JPanel panelTarjetas;

    /** Imagen de fondo de la vista */
    private Image imagenFondo;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /** Etiqueta que muestra el total de videojuegos */
    private JLabel lblTotal;

    private JTextField txtBuscar;
    private JComboBox<String> cmbPlataforma;
    private JComboBox<String> cmbGenero;
    private JComboBox<String> cmbPuntuacion;
    private JButton btnBuscar;
    private JButton btnLimpiarBusqueda;
    private JButton btnLimpiarFiltros;
    private List<Videojuego> videojuegosCargados;
    private static final int ANCHO_MAX_TARJETA = 1450;

    private String PLACEHOLDER_BUSQUEDA;
    private String TODAS_PLATAFORMAS;
    private String TODOS_GENEROS;
    private String TODAS_PUNTUACIONES;
    private static final String[] GENEROS = {
        "",
        "Action", "Adventure", "RPG", "Strategy", "Shooter",
        "Puzzle", "Arcade", "Platformer", "Racing", "Sports",
        "Fighting", "Simulation", "Family", "Board Games",
        "Educational", "Card", "Casual", "Massively Multiplayer",
        "Indie"
    };

    /** Controlador asociado a la vista */
    private ListadoVideojuegosController controller;

    /**
     * Constructor de la vista de listado de videojuegos.
     *
     * @param usuario usuario autenticado
     */
    public ListadoVideojuegosVista(Usuario usuario) {
        this.usuario = usuario;

        texts = ResourceBundle.getBundle("i18n.messages");
        PLACEHOLDER_BUSQUEDA    = texts.getString("filter.search.placeholder");
        TODAS_PLATAFORMAS       = texts.getString("filter.all.platforms");
        TODOS_GENEROS           = texts.getString("filter.all.genres");
        TODAS_PUNTUACIONES      = texts.getString("filter.all.ratings");

        controller = new ListadoVideojuegosController(usuario);
        controller.setVista(this);

        setTitle(texts.getString("list.window.title"));
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addWindowStateListener(e -> MenuPrincipalVista.mantenerMaximizada(this));

        var url = getClass().getResource("/img/fondoListado.jpg");
        if (url != null) {
            imagenFondo = new ImageIcon(url).getImage();
        }

        initComponents();
        cargarVideojuegos();
    }

    /**
     * Inicializa y organiza los componentes gráficos de la ventana.
     */
    private void initComponents() {

        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        root.setFocusable(true);
        setContentPane(root);

        /* ===== TÍTULO ===== */
        JLabel lblTitulo = new JLabel(
                texts.getString("list.title"),
                SwingConstants.CENTER
        );
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 42));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(58, 10, 30, 10));

        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(Color.WHITE);

        JPanel panelNorte = new JPanel(new BorderLayout(0, 8));
        panelNorte.setOpaque(false);
        panelNorte.add(lblTitulo, BorderLayout.NORTH);
        panelNorte.add(crearPanelFiltros(), BorderLayout.SOUTH);
        root.add(panelNorte, BorderLayout.NORTH);

        /* ===== PANEL DE TARJETAS ===== */
        panelTarjetas = new JPanel();
        panelTarjetas.setLayout(new BoxLayout(panelTarjetas, BoxLayout.Y_AXIS));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setBorder(BorderFactory.createEmptyBorder(24, 45, 10, 45));

        JScrollPane scroll = new JScrollPane(panelTarjetas);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        /* ===== PANEL INFERIOR ===== */
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(4, 34, 34, 12));

        JButton btnAtras = crearBotonAtras();

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzq.setOpaque(false);
        panelIzq.add(btnAtras);

        panelSur.add(panelIzq, BorderLayout.WEST);
        root.add(panelSur, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> root.requestFocusInWindow());
    }

    private JPanel crearPanelFiltros() {
        JPanel panelFiltros = new JPanel(new BorderLayout());
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 20, 18, 30));

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(0, 220, 0, 0));
        panelIzquierdo.add(lblTotal, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panelDerecho.setOpaque(false);

        cmbPlataforma = new JComboBox<>();
        cmbPlataforma.setPreferredSize(new Dimension(200, 40));
        cmbPlataforma.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        cmbGenero = new JComboBox<>();
        cmbGenero.setPreferredSize(new Dimension(200, 40));
        cmbGenero.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        cmbPuntuacion = new JComboBox<>();
        cmbPuntuacion.setPreferredSize(new Dimension(240, 40));
        cmbPuntuacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        txtBuscar = new JTextField(18);
        txtBuscar.setToolTipText(texts.getString("filter.search.tooltip"));
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ponerPlaceholder(txtBuscar, PLACEHOLDER_BUSQUEDA);
        btnLimpiarBusqueda = crearBotonLimpiarBusqueda();
        JPanel campoBusqueda = crearCampoBusquedaConLimpiar();
        btnBuscar = crearBotonFiltro(texts.getString("filter.search.button"), new Dimension(120, 40));
        btnLimpiarFiltros = crearBotonFiltro(texts.getString("filter.clear.button"), new Dimension(170, 40));

        panelDerecho.add(cmbPlataforma);
        panelDerecho.add(cmbGenero);
        panelDerecho.add(cmbPuntuacion);
        panelDerecho.add(campoBusqueda);
        panelDerecho.add(btnBuscar);
        panelDerecho.add(btnLimpiarFiltros);

        panelFiltros.add(panelIzquierdo, BorderLayout.CENTER);
        panelFiltros.add(panelDerecho, BorderLayout.EAST);

        cmbPlataforma.addActionListener(e -> aplicarFiltros());
        cmbGenero.addActionListener(e -> aplicarFiltros());
        cmbPuntuacion.addActionListener(e -> aplicarFiltros());
        btnBuscar.addActionListener(e -> aplicarFiltros());
        txtBuscar.addActionListener(e -> aplicarFiltros());
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());
        btnLimpiarFiltros.addActionListener(e -> limpiarFiltros());

        return panelFiltros;
    }

    private JButton crearBotonFiltro(String texto, Dimension tamano) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fondo = getModel().isPressed()
                        ? new Color(230, 220, 245)
                        : getModel().isRollover() ? new Color(244, 238, 252) : Color.WHITE;
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.setColor(new Color(190, 170, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setForeground(new Color(80, 50, 130));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(tamano);
        return boton;
    }

    private JPanel crearCampoBusquedaConLimpiar() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(UIManager.getBorder("TextField.border"));
        panel.setPreferredSize(new Dimension(250, 40));

        JLabel lblLupa = new JLabel(crearIconoLupa(16, 16));
        lblLupa.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 6));

        txtBuscar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
        txtBuscar.setOpaque(false);

        panel.add(lblLupa, BorderLayout.WEST);
        panel.add(txtBuscar, BorderLayout.CENTER);
        panel.add(btnLimpiarBusqueda, BorderLayout.EAST);
        return panel;
    }

    private JButton crearBotonLimpiarBusqueda() {
        JButton boton = new JButton("X");
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(new Color(110, 40, 180));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.setPreferredSize(new Dimension(32, 38));
        boton.setToolTipText(texts.getString("search.clear.tooltip"));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Crea una tarjeta visual para un videojuego.
     * <p>
     * El título y las anotaciones aparecen a la izquierda/centro.
     * Plataforma, año y valoración aparecen en la fila superior.
     * La estrella dorada solo se muestra si {@code v.isFavorito()} es true.
     * </p>
     *
     * @param v videojuego a mostrar
     * @return panel con la tarjeta del videojuego
     */
    private JPanel crearTarjeta(Videojuego v) {

        JPanel tarjeta = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 215));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        /* ===== ESTRELLA (solo si favorito, a la derecha) ===== */
        if (v.isFavorito()) {
            JLabel lblEstrella = new JLabel(crearIconoEstrella(32, 32));
            lblEstrella.setVerticalAlignment(SwingConstants.CENTER);
            lblEstrella.setHorizontalAlignment(SwingConstants.CENTER);
            lblEstrella.setPreferredSize(new Dimension(38, 38));
            lblEstrella.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblEstrella.setToolTipText(texts.getString("favorites.remove"));
            lblEstrella.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int opcion = JOptionPane.showConfirmDialog(
                            ListadoVideojuegosVista.this,
                            java.text.MessageFormat.format(
                                    texts.getString("favorites.confirm.text"), v.getTitulo()),
                            texts.getString("favorites.confirm.title"),
                            JOptionPane.YES_NO_OPTION
                    );
                    if (opcion == JOptionPane.YES_OPTION) {
                        controller.quitarFavorito(v);
                        lblEstrella.setVisible(false);
                    }
                }
            });
            tarjeta.add(lblEstrella, BorderLayout.EAST);
        }

        /* ===== CONTENIDO: fila superior + anotaciones ===== */
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        /* --- Fila: título | plataforma | año | puntuación --- */
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila.setOpaque(false);

        Font fuenteTitulo = new Font("Segoe UI Black", Font.PLAIN, 22);
        Font fuenteDato   = new Font("Segoe UI", Font.BOLD, 20);
        Color colorTitulo = new Color(40, 20, 80);
        Color colorDato   = new Color(80, 50, 130);
        Color colorSep    = new Color(160, 140, 190);

        fila.add(label(v.getTitulo(), fuenteTitulo, colorTitulo));

        fila.add(separador(colorSep));
        fila.add(label(v.getPlataforma(), fuenteDato, colorDato));

        if (!"-".equals(v.getFechaMostrada())) {
            fila.add(separador(colorSep));
            fila.add(label(v.getFechaMostrada(), fuenteDato, colorDato));
        }

        if (v.getValoracion() != null) {
            fila.add(separador(colorSep));
            fila.add(label(
                    v.getValoracionMostrada(),
                    new Font("Segoe UI Black", Font.PLAIN, 22),
                    new Color(110, 40, 180)
            ));
        }

        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(fila);

        /* --- Anotaciones debajo --- */
        boolean tieneAnotacion = v.getAnotacion() != null && !v.getAnotacion().trim().isEmpty();
        if (tieneAnotacion) {
            contenido.add(Box.createRigidArea(new Dimension(0, 5)));
            JLabel lblAnotacion = new JLabel(
                    "<html><i>" + v.getAnotacion().trim().replace("<", "&lt;") + "</i></html>"
            );
            lblAnotacion.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblAnotacion.setForeground(Color.GRAY);
            lblAnotacion.setAlignmentX(Component.LEFT_ALIGNMENT);
            contenido.add(lblAnotacion);
        }

        tarjeta.add(contenido, BorderLayout.CENTER);
        tarjeta.setMaximumSize(new Dimension(ANCHO_MAX_TARJETA, tieneAnotacion ? 130 : 90));
        tarjeta.setPreferredSize(new Dimension(ANCHO_MAX_TARJETA, tieneAnotacion ? 130 : 90));

        return tarjeta;
    }

    /**
     * Crea un JLabel simple con fuente y color dados.
     */
    private JLabel label(String texto, Font fuente, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuente);
        lbl.setForeground(color);
        return lbl;
    }

    /**
     * Crea un separador visual entre elementos de la fila.
     */
    private JLabel separador(Color color) {
        JLabel sep = new JLabel("   |   ");
        sep.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        sep.setForeground(color);
        return sep;
    }

    /**
     * Crea el botón para volver al menú principal.
     *
     * @return botón configurado
     */
    private JButton crearBotonAtras() {
        JButton boton = new JButton(texts.getString("common.back")) {
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
        boton.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 40));
        boton.addActionListener(e -> controller.volverMenu());
        return boton;
    }

    /**
     * Carga los videojuegos del usuario como tarjetas y actualiza el contador total.
     * <p>
     * El favorito se lee directamente con {@code v.isFavorito()}, sin necesidad
     * de un Set de IDs separado.
     * </p>
     */
    private void cargarVideojuegos() {
        videojuegosCargados = controller.obtenerVideojuegos();
        cargarOpcionesFiltros();
        pintarVideojuegos(videojuegosCargados);
    }

    private void pintarVideojuegos(List<Videojuego> videojuegos) {
        panelTarjetas.removeAll();

        int total = 0;

        if (videojuegos != null && !videojuegos.isEmpty()) {
            for (Videojuego v : videojuegos) {
                panelTarjetas.add(crearTarjeta(v));
                panelTarjetas.add(Box.createRigidArea(new Dimension(0, 30)));
                total++;
            }
        } else {
            panelTarjetas.add(Box.createVerticalGlue());
            panelTarjetas.add(crearMensajeVacio(texts.getString("list.empty")));
            panelTarjetas.add(Box.createVerticalGlue());
            total = 0;
        }

        StringBuilder filtroDesc = new StringBuilder();
        String selPlataforma = (String) cmbPlataforma.getSelectedItem();
        String selGenero     = (String) cmbGenero.getSelectedItem();
        String selPuntuacion = (String) cmbPuntuacion.getSelectedItem();
        if (selPlataforma != null && !selPlataforma.equals(TODAS_PLATAFORMAS))
            filtroDesc.append(" ").append(selPlataforma);
        if (selGenero != null && !selGenero.equals(TODOS_GENEROS))
            filtroDesc.append(" ").append(selGenero);
        if (selPuntuacion != null && !selPuntuacion.equals(TODAS_PUNTUACIONES))
            filtroDesc.append(" ").append(selPuntuacion);

        String labelTexto;
        if (filtroDesc.length() > 0) {
            labelTexto = texts.getString("list.total.prefix") + filtroDesc
                    + " " + texts.getString("list.total.of") + " " + usuario.getNombre() + ": " + total;
        } else {
            labelTexto = texts.getString("list.total.prefix") + " " + usuario.getNombre() + ": " + total;
        }

        lblTotal.setText("<html><body>" + escaparHtml(labelTexto) + "</body></html>");

        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JPanel crearMensajeVacio(String texto) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setPreferredSize(new Dimension(ANCHO_MAX_TARJETA, 240));
        panel.setMaximumSize(new Dimension(ANCHO_MAX_TARJETA, 240));
        panel.setMinimumSize(new Dimension(ANCHO_MAX_TARJETA, 240));

        JLabel lblMensaje = new JLabel(texto, SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblMensaje.setForeground(Color.WHITE);
        panel.add(lblMensaje);
        return panel;
    }

    private String escaparHtml(String texto) {
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private void cargarOpcionesFiltros() {
        cmbPlataforma.removeAllItems();
        cmbGenero.removeAllItems();
        cmbPuntuacion.removeAllItems();

        cmbPlataforma.addItem(TODAS_PLATAFORMAS);
        cmbGenero.addItem(TODOS_GENEROS);
        cmbPuntuacion.addItem(TODAS_PUNTUACIONES);

        try {
            PlataformaDAO dao = new PlataformaDAO_imp();
            for (Plataforma plataforma : dao.fetchAll()) {
                cmbPlataforma.addItem(plataforma.getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar plataformas del filtro: " + e.getMessage());
        }

        for (String genero : GENEROS) {
            if (!genero.isEmpty()) cmbGenero.addItem(genero);
        }

        for (int i = 1; i <= 10; i++) {
            cmbPuntuacion.addItem(String.valueOf(i));
        }
    }

    private void aplicarFiltros() {
        if (videojuegosCargados == null) return;

        String busqueda = txtBuscar.getText().trim();
        if (busqueda.equals(PLACEHOLDER_BUSQUEDA)) busqueda = "";
        busqueda = busqueda.toLowerCase();

        String plataforma = (String) cmbPlataforma.getSelectedItem();
        String genero = (String) cmbGenero.getSelectedItem();
        String puntuacion = (String) cmbPuntuacion.getSelectedItem();

        java.util.ArrayList<Videojuego> filtrados = new java.util.ArrayList<>();
        for (Videojuego v : videojuegosCargados) {
            boolean coincideBusqueda = busqueda.isEmpty()
                    || v.getTitulo().toLowerCase().contains(busqueda)
                    || v.getFechaMostrada().toLowerCase().contains(busqueda);
            boolean coincidePlataforma = plataforma == null
                    || plataforma.equals(TODAS_PLATAFORMAS)
                    || plataforma.equals(v.getPlataforma());
            boolean coincideGenero = genero == null
                    || genero.equals(TODOS_GENEROS)
                    || genero.equals(v.getGenero());
            boolean coincidePuntuacion = puntuacion == null
                    || puntuacion.equals(TODAS_PUNTUACIONES)
                    || puntuacion.equals(v.getValoracionMostrada());

            if (coincideBusqueda && coincidePlataforma && coincideGenero && coincidePuntuacion) {
                filtrados.add(v);
            }
        }

        pintarVideojuegos(filtrados);
    }

    private void limpiarBusqueda() {
        txtBuscar.setText("");
        txtBuscar.setForeground(Color.BLACK);
        aplicarFiltros();
        txtBuscar.requestFocusInWindow();
    }

    private void limpiarFiltros() {
        txtBuscar.setText("");
        txtBuscar.setForeground(Color.BLACK);
        cmbPlataforma.setSelectedItem(TODAS_PLATAFORMAS);
        cmbGenero.setSelectedItem(TODOS_GENEROS);
        cmbPuntuacion.setSelectedItem(TODAS_PUNTUACIONES);
        pintarVideojuegos(videojuegosCargados);
        txtBuscar.requestFocusInWindow();
    }

    private Icon crearIconoEstrella(int ancho, int alto) {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagen.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double cx = ancho / 2.0, cy = alto / 2.0;
        double rExt = Math.min(ancho, alto) * 0.46;
        double rInt = rExt * 0.42;

        Path2D star = new Path2D.Double();
        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(-90 + i * 36);
            double r = (i % 2 == 0) ? rExt : rInt;
            double x = cx + Math.cos(angle) * r;
            double y = cy + Math.sin(angle) * r;
            if (i == 0) star.moveTo(x, y); else star.lineTo(x, y);
        }
        star.closePath();

        g2.setColor(new Color(255, 210, 50));
        g2.fill(star);
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(new Color(190, 140, 10));
        g2.draw(star);
        g2.dispose();

        return new ImageIcon(imagen);
    }

    private Icon crearIconoLupa(int ancho, int alto) {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagen.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(120, 120, 120));
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(2, 2, ancho - 8, alto - 8);
        g2.drawLine(ancho - 6, alto - 6, ancho - 2, alto - 2);
        g2.dispose();
        return new ImageIcon(imagen);
    }

    private void ponerPlaceholder(JTextField campo, String texto) {
        campo.setText(texto);
        campo.setForeground(Color.GRAY);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(texto)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setText(texto);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
    }
}
