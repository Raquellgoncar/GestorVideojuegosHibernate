/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.DAO.VideojuegoDAO;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Raquel
 */
public class PerfilVista extends JFrame {

    private Usuario usuario;

    private JLabel lblNombreValor;
    private JLabel lblTotalNumero;

    private UsuarioDAO usuarioDAO = new UsuarioDAO_imp();
    private VideojuegoDAO videojuegoDAO = new VideojuegoDAO_imp();

    public PerfilVista(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Perfil");
        setSize(650, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
        cargarTotalJuegos();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        /* ================= PANEL CENTRAL ================= */
        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        add(root, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        /* ================= TÍTULO ================= */
        JLabel lblTitulo = new JLabel("PERFIL", SwingConstants.CENTER);
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

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(fuenteLabel);
        root.add(lblNombre, gbc);

        gbc.gridx = 1;
        lblNombreValor = new JLabel(usuario.getNombre());
        lblNombreValor.setFont(fuenteDato);
        root.add(lblNombreValor, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;

        JButton btnEditar = crearBotonMorado(
                "Editar",
                new Dimension(110, 34)
        );
        btnEditar.addActionListener(e -> editarNombre());
        root.add(btnEditar, gbc);

        gbc.anchor = GridBagConstraints.WEST;

        /* ================= USERNAME ================= */
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets = new Insets(18, 0, 12, 20);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(fuenteLabel);
        root.add(lblUser, gbc);

        gbc.gridx = 1;
        JLabel lblUserValor = new JLabel(usuario.getUsername());
        lblUserValor.setFont(fuenteDato);
        root.add(lblUserValor, gbc);

        /* ================= EMAIL ================= */
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets = new Insets(18, 0, 12, 20);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(fuenteLabel);
        root.add(lblEmail, gbc);

        gbc.gridx = 1;
        JLabel lblEmailValor = new JLabel(usuario.getEmail());
        lblEmailValor.setFont(fuenteDato);
        root.add(lblEmailValor, gbc);

        /* ================= TOTAL JUEGOS ================= */
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(50, 0, 6, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTotalTexto = new JLabel("Total de juegos registrados:");
        lblTotalTexto.setFont(new Font("Arial", Font.BOLD, 16));
        root.add(lblTotalTexto, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(6, 0, 0, 0);

        lblTotalNumero = new JLabel("0");
        lblTotalNumero.setFont(new Font("Arial", Font.BOLD, 30));
        root.add(lblTotalNumero, gbc);

        /* ================= PANEL INFERIOR ================= */
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));
        panelInferior.setOpaque(false);

        JButton btnAtras = crearBotonMorado("Atrás", new Dimension(120, 32));
        btnAtras.addActionListener(e -> {
            new MenuPrincipalVista(usuario).setVisible(true);
            dispose();
        });

        panelInferior.add(btnAtras);
        add(panelInferior, BorderLayout.SOUTH);
    }

    /* ================= TOTAL JUEGOS ================= */
    private void cargarTotalJuegos() {
        int total = videojuegoDAO.fetchAll().size();
        lblTotalNumero.setText(String.valueOf(total));
    }

    /* ================= EDITAR NOMBRE ================= */
    private void editarNombre() {

        JTextField txtNombre = new JTextField(usuario.getNombre());
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        Object[] contenido = {
            "Nuevo nombre:",
            txtNombre
        };

        int opcion = JOptionPane.showOptionDialog(
                this,
                contenido,
                "Editar nombre",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Cancelar", "Aceptar"},
                "Aceptar"
        );

        if (opcion != 1) {
            return;
        }

        String nuevoNombre = txtNombre.getText().trim();
        String nombreAntiguo = usuario.getNombre();

        if (nuevoNombre.isEmpty() || nuevoNombre.equals(nombreAntiguo)) {
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "Seguro que deseas cambiar el nombre\n\n"
                + "de \"" + nombreAntiguo +  "\" a \"" + nuevoNombre + "\"",
                "Confirmar cambio",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        
        usuario.setNombre(nuevoNombre);
        usuarioDAO.update(usuario);

        
        lblNombreValor.setText(nuevoNombre);

        JOptionPane.showMessageDialog(
                this,
                "Nombre actualizado correctamente",
                "Perfil",
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
