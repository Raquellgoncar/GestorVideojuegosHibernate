/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.InsertarVideojuegoController;
import Modelo.Videojuego;
import Modelo.Favorito;
import Modelo.Usuario;
import Modelo.DAO.FavoritoDAO;
import Modelo.DAO.Imp.FavoritoDAO_imp;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Vista para la inserción de nuevos videojuegos.
 * <p>
 * Permite al usuario introducir los datos de un videojuego
 * (título, plataforma, año, valoración) y marcarlo opcionalmente
 * como favorito. Incluye validaciones básicas antes de delegar
 * la inserción al controlador.
 * </p>
 *
 * Sigue el patrón MVC, delegando la lógica de negocio en
 * {@link InsertarVideojuegoController}.
 *
 * @author Raquel
 * @version 1.0
 */
public class InsertarVideojuegoVista extends JFrame {

    /** Campos de texto para los datos del videojuego */
    private JTextField txtTitulo, txtPlataforma, txtAnio, txtValoracion;

    /** Botón para marcar el videojuego como favorito */
    private JToggleButton btnFavorito;

    /** Botones de acción */
    private JButton btnAceptar, btnCancelar;

    /** Usuario autenticado */
    private Usuario usuario;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /** Controlador asociado a la vista */
    private InsertarVideojuegoController controller;

    /**
     * Constructor de la vista de inserción de videojuegos.
     *
     * @param usuario usuario autenticado
     */
    public InsertarVideojuegoVista(Usuario usuario) {
        this.usuario = usuario;

        texts = ResourceBundle.getBundle("i18n.messages");

        controller = new InsertarVideojuegoController(usuario);
        controller.setVista(this);

        setTitle(texts.getString("insert.window.title"));
        setSize(560, 520);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    /**
     * Inicializa y organiza los componentes gráficos de la ventana.
     */
    private void initComponents() {

        setLayout(new BorderLayout());

        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
        add(root, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 10, 14, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===== TÍTULO ===== */
        JLabel lblTitulo = new JLabel(
                texts.getString("insert.title"),
                SwingConstants.CENTER
        );
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 34));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        root.add(lblTitulo, gbc);
        gbc.gridwidth = 1;

        Font fuenteLabel = new Font("Arial", Font.BOLD, 15);

        /* ===== CAMPOS ===== */
        gbc.gridy++;

        JLabel lblT = new JLabel(texts.getString("insert.field.title") + ":");
        lblT.setFont(fuenteLabel);
        root.add(lblT, gbc);

        txtTitulo = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblP = new JLabel(texts.getString("insert.field.platform") + ":");
        lblP.setFont(fuenteLabel);
        root.add(lblP, gbc);

        txtPlataforma = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtPlataforma, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblA = new JLabel(texts.getString("insert.field.year") + ":");
        lblA.setFont(fuenteLabel);
        root.add(lblA, gbc);

        txtAnio = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtAnio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblV = new JLabel(texts.getString("insert.field.rating") + ":");
        lblV.setFont(fuenteLabel);
        root.add(lblV, gbc);

        txtValoracion = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtValoracion, gbc);

        /* ===== FAVORITO ===== */
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JPanel panelFavorito = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFavorito.setOpaque(false);

        btnFavorito = new JToggleButton();
        btnFavorito.setIcon(cargarIconoEscalado("/img/estrellagris.png", 42, 42));
        btnFavorito.setSelectedIcon(cargarIconoEscalado("/img/estrellaamarilla.png", 42, 42));
        btnFavorito.setBorderPainted(false);
        btnFavorito.setContentAreaFilled(false);
        btnFavorito.setFocusPainted(false);
        btnFavorito.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblFav = new JLabel(texts.getString("insert.favorite"));
        lblFav.setFont(new Font("Arial", Font.BOLD, 14));

        panelFavorito.add(btnFavorito);
        panelFavorito.add(lblFav);

        root.add(panelFavorito, gbc);

        /* ===== BOTONES ===== */
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 25, 18, 25));
        panelInferior.setOpaque(false);

        btnCancelar = crearBotonMorado(
                texts.getString("insert.cancel"),
                new Dimension(120, 32),
                e -> controller.volverModificar()
        );

        btnAceptar = crearBotonMorado(
                texts.getString("insert.accept"),
                new Dimension(200, 38),
                e -> validarYProcesar()
        );

        getRootPane().setDefaultButton(btnAceptar);

        panelInferior.add(btnCancelar, BorderLayout.WEST);
        panelInferior.add(btnAceptar, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Crea un campo de texto con el estilo común de la aplicación.
     *
     * @return campo de texto configurado
     */
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(260, 34));
        return campo;
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
     * Valida los datos introducidos y solicita al controlador
     * la inserción del videojuego.
     */
    private void validarYProcesar() {

        if (txtTitulo.getText().trim().isEmpty()
                || txtPlataforma.getText().trim().isEmpty()
                || txtAnio.getText().trim().isEmpty()
                || txtValoracion.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("insert.error.empty"),
                    texts.getString("insert.error.empty.title"),
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(txtAnio.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, texts.getString("insert.error.year"));
            return;
        }

        BigDecimal valoracion;
        try {
            valoracion = new BigDecimal(txtValoracion.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, texts.getString("insert.error.rating"));
            return;
        }

        try {
            controller.insertarVideojuego(
                    txtTitulo.getText().trim(),
                    txtPlataforma.getText().trim(),
                    anio,
                    valoracion,
                    btnFavorito.isSelected()
            );

            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("insert.success")
                            + (btnFavorito.isSelected()
                            ? " " + texts.getString("insert.success.favorite")
                            : ""),
                    texts.getString("insert.success.title"),
                    JOptionPane.INFORMATION_MESSAGE
            );

            controller.volverModificar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("insert.error.generic"),
                    texts.getString("insert.error.title"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Carga y escala un icono desde los recursos del proyecto.
     *
     * @param ruta ruta del recurso
     * @param ancho ancho deseado
     * @param alto alto deseado
     * @return icono escalado
     */
    private ImageIcon cargarIconoEscalado(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}

