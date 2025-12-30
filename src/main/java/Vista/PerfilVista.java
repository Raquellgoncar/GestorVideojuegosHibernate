/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.PerfilController;
import Modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Vista encargada de mostrar y gestionar la información del perfil
 * del usuario autenticado.
 * <p>
 * Desde esta pantalla el usuario puede:
 * <ul>
 *   <li>Visualizar su nombre, username y email</li>
 *   <li>Editar su nombre</li>
 *   <li>Cambiar su contraseña</li>
 *   <li>Consultar el número total de videojuegos registrados</li>
 * </ul>
 * </p>
 *
 * La lógica de negocio se delega en {@link PerfilController},
 * siguiendo el patrón MVC.
 *
 * @author Raquel
 * @version 1.0
 */
public class PerfilVista extends JFrame {

    /** Usuario autenticado */
    private Usuario usuario;

    /** Controlador asociado a la vista */
    private PerfilController controller;

    /** Etiqueta que muestra el nombre del usuario */
    private JLabel lblNombreValor;

    /** Etiqueta que muestra el total de videojuegos */
    private JLabel lblTotalNumero;

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
        setSize(650, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
        cargarTotalJuegos();
    }

    /**
     * Inicializa y organiza todos los componentes gráficos
     * de la vista de perfil.
     */
    private void initComponents() {

        setLayout(new BorderLayout());

        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        add(root, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;

        /* ================= TÍTULO ================= */
        JLabel lblTitulo = new JLabel(
                texts.getString("profile.title"),
                SwingConstants.CENTER
        );
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 34));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 35, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        root.add(lblTitulo, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        Font fuenteLabel = new Font("Arial", Font.BOLD, 15);
        Font fuenteDato = new Font("Arial", Font.PLAIN, 15);

        /* ================= NOMBRE ================= */
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 12, 20);

        JLabel lblNombre = new JLabel(texts.getString("profile.name") + ":");
        lblNombre.setFont(fuenteLabel);
        root.add(lblNombre, gbc);

        gbc.gridx = 1;
        lblNombreValor = new JLabel(usuario.getNombre());
        lblNombreValor.setFont(fuenteDato);
        root.add(lblNombreValor, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;

        JButton btnEditar = crearBotonMorado(
                texts.getString("profile.edit"),
                new Dimension(110, 34)
        );
        btnEditar.addActionListener(e -> editarNombre());
        root.add(btnEditar, gbc);

        gbc.anchor = GridBagConstraints.WEST;

        /* ================= USERNAME ================= */
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets = new Insets(18, 0, 12, 20);

        JLabel lblUser = new JLabel(texts.getString("profile.username") + ":");
        lblUser.setFont(fuenteLabel);
        root.add(lblUser, gbc);

        gbc.gridx = 1;
        JLabel lblUserValor = new JLabel(usuario.getUsername());
        lblUserValor.setFont(fuenteDato);
        root.add(lblUserValor, gbc);

        /* ================= EMAIL ================= */
        gbc.gridy++;
        gbc.gridx = 0;

        JLabel lblEmail = new JLabel(texts.getString("profile.email") + ":");
        lblEmail.setFont(fuenteLabel);
        root.add(lblEmail, gbc);

        gbc.gridx = 1;
        JLabel lblEmailValor = new JLabel(usuario.getEmail());
        lblEmailValor.setFont(fuenteDato);
        root.add(lblEmailValor, gbc);

        /* ================= CAMBIAR CONTRASEÑA ================= */
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(35, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JButton btnCambiarPass = crearBotonMorado(
                texts.getString("profile.change.password"),
                new Dimension(280, 44)
        );
        btnCambiarPass.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        btnCambiarPass.addActionListener(e -> cambiarContrasena());
        root.add(btnCambiarPass, gbc);

        /* ================= TOTAL DE JUEGOS ================= */
        gbc.gridy++;
        gbc.insets = new Insets(25, 0, 6, 0);

        JLabel lblTotalTexto = new JLabel(
                texts.getString("profile.total.text") + ":"
        );
        lblTotalTexto.setFont(new Font("Arial", Font.BOLD, 16));
        root.add(lblTotalTexto, gbc);

        gbc.gridy++;
        lblTotalNumero = new JLabel("0");
        lblTotalNumero.setFont(new Font("Arial", Font.BOLD, 30));
        root.add(lblTotalNumero, gbc);

        /* ================= BOTÓN ATRÁS ================= */
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));
        panelInferior.setOpaque(false);

        JButton btnAtras = crearBotonMorado(
                texts.getString("profile.back"),
                new Dimension(120, 32)
        );
        btnAtras.addActionListener(e -> controller.volverMenu());

        panelInferior.add(btnAtras);
        add(panelInferior, BorderLayout.SOUTH);
    }

    /* ================= DATOS ================= */

    /**
     * Carga y muestra el total de videojuegos
     * DEL USUARIO AUTENTICADO.
     */
    private void cargarTotalJuegos() {
        lblTotalNumero.setText(
                String.valueOf(controller.obtenerTotalJuegos())
        );
    }

    /* ================= EDITAR NOMBRE ================= */

    private void editarNombre() {

        JTextField txtNombre = new JTextField(usuario.getNombre());

        int opcion = JOptionPane.showConfirmDialog(
                this,
                txtNombre,
                texts.getString("profile.edit.dialog.title"),
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion != JOptionPane.OK_OPTION) return;

        String nuevoNombre = txtNombre.getText().trim();
        if (nuevoNombre.isEmpty() || nuevoNombre.equals(usuario.getNombre())) return;

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres cambiar tu nombre de \"" +
                        usuario.getNombre() + "\" a \"" + nuevoNombre + "\"?",
                texts.getString("profile.edit.confirm.title"),
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) return;

        controller.actualizarNombre(nuevoNombre);
        usuario.setNombre(nuevoNombre);
        lblNombreValor.setText(nuevoNombre);

        JOptionPane.showMessageDialog(
                this,
                texts.getString("profile.edit.success"),
                texts.getString("profile.edit.success.title"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /* ================= CAMBIAR CONTRASEÑA ================= */

    private void cambiarContrasena() {

        JPasswordField txtPassword = new JPasswordField();

        int opcion = JOptionPane.showConfirmDialog(
                this,
                txtPassword,
                texts.getString("profile.password.title"),
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion != JOptionPane.OK_OPTION) return;

        String nuevaPassword = new String(txtPassword.getPassword()).trim();
        if (nuevaPassword.isEmpty()) return;

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                texts.getString("profile.password.confirm"),
                texts.getString("profile.password.confirm.title"),
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) return;

        controller.actualizarPassword(nuevaPassword);

        JOptionPane.showMessageDialog(
                this,
                texts.getString("profile.password.success"),
                texts.getString("profile.password.success.title"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /* ================= BOTÓN MORADO ================= */

    private JButton crearBotonMorado(String texto, Dimension tamaño) {

        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setPaint(new GradientPaint(
                        0, 0, new Color(180, 120, 255),
                        0, getHeight(), new Color(110, 40, 180)
                ));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
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

        return boton;
    }
}
