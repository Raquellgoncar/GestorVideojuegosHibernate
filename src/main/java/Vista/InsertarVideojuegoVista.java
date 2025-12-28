/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

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

/**
 *
 * @author Raquel
 */
public class InsertarVideojuegoVista extends JFrame {

    private JTextField txtTitulo, txtPlataforma, txtAnio, txtValoracion;
    private JToggleButton btnFavorito;
    private JButton btnAceptar, btnCancelar;

    private Usuario usuario;

    private VideojuegoDAO videojuegoDAO = new VideojuegoDAO_imp();
    private FavoritoDAO favoritoDAO = new FavoritoDAO_imp();

    public InsertarVideojuegoVista(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Insertar videojuego");
        setSize(560, 520);                 // ⬅ un poco más ancho
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        /* ===== CONTENEDOR PRINCIPAL ===== */
        setLayout(new BorderLayout());

        /* ===== PANEL CENTRAL ===== */
        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30)); // ⬅ más aire arriba
        add(root, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 10, 14, 10); // ⬅ más separación vertical
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        /* ===== TÍTULO GRANDE ===== */
        JLabel lblTitulo = new JLabel("INSERTAR VIDEOJUEGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 34)); // ⬅ más grande
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        root.add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        Font fuenteLabel = new Font("Arial", Font.BOLD, 15);

        /* ===== CAMPOS ===== */
        gbc.gridy++;

        JLabel lblT = new JLabel("Título:");
        lblT.setFont(fuenteLabel);
        root.add(lblT, gbc);

        txtTitulo = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblP = new JLabel("Plataforma:");
        lblP.setFont(fuenteLabel);
        root.add(lblP, gbc);

        txtPlataforma = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtPlataforma, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblA = new JLabel("Año:");
        lblA.setFont(fuenteLabel);
        root.add(lblA, gbc);

        txtAnio = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtAnio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblV = new JLabel("Valoración:");
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

        JLabel lblFav = new JLabel("Marcar como favorito");
        lblFav.setFont(new Font("Arial", Font.BOLD, 14));

        panelFavorito.add(btnFavorito);
        panelFavorito.add(lblFav);

        root.add(panelFavorito, gbc);

        /* ===== PANEL INFERIOR (BOTONES) ===== */
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 25, 18, 25));
        panelInferior.setOpaque(false);

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzq.setOpaque(false);

        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDer.setOpaque(false);

        btnCancelar = crearBotonMorado(
                "Cancelar",
                new Dimension(120, 32),
                e -> {
                    new ModificarVideojuegosVista(usuario).setVisible(true);
                    dispose();
                }
        );

        btnAceptar = crearBotonMorado(
                "Aceptar",
                new Dimension(200, 38),   // ⬅ más grande
                e -> validarYProcesar()
        );

        panelIzq.add(btnCancelar);
        panelDer.add(btnAceptar);

        panelInferior.add(panelIzq, BorderLayout.WEST);
        panelInferior.add(panelDer, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }

    /* ===== CAMPO DE TEXTO GRANDE ===== */
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(260, 34)); // ⬅ más alto
        return campo;
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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

    /* ================= VALIDACIONES + BD ================= */
    private void validarYProcesar() {

        if (txtTitulo.getText().trim().isEmpty()
                || txtPlataforma.getText().trim().isEmpty()
                || txtAnio.getText().trim().isEmpty()
                || txtValoracion.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Rellena todos los campos.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(txtAnio.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número entero.");
            txtAnio.requestFocus();
            return;
        }

        BigDecimal valoracion;
        try {
            valoracion = new BigDecimal(txtValoracion.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La valoración debe ser un número decimal.");
            txtValoracion.requestFocus();
            return;
        }

        try {
            Videojuego v = new Videojuego();
            v.setTitulo(txtTitulo.getText().trim());
            v.setPlataforma(txtPlataforma.getText().trim());
            v.setAnio(anio);
            v.setValoracion(valoracion);

            videojuegoDAO.insert(v);

            if (btnFavorito.isSelected()) {
                Favorito f = new Favorito();
                f.setUsuarioId(usuario);
                f.setVideojuegoId(v);
                f.setFechaAnadido(new Date());
                favoritoDAO.insert(f);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Videojuego insertado correctamente"
                            + (btnFavorito.isSelected() ? " y marcado como favorito" : ""),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new ModificarVideojuegosVista(usuario).setVisible(true);
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error al insertar el videojuego.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /* ===== ICONOS ===== */
    private ImageIcon cargarIconoEscalado(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}