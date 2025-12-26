/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.DAO.UsuarioDAO;
import Modelo.Usuario;
import Modelo.util.PasswordService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 *
 * @author Raquel
 */
public class LoginVista extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnAcceder;
    private JButton btnRegistrar;
    private JButton btnRecuperar;
    private Image imagenFondo;

   
    private UsuarioDAO usuarioDAO = new UsuarioDAO_imp();

    public LoginVista() {
        setTitle("Checkpoint - Login");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        var url = getClass().getResource("/img/imagenFondo.png");

        if (url == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encuentra la imagen /img/imagenFondo.png",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } else {
            imagenFondo = new ImageIcon(url).getImage();
        }

        initComponents();
    }

    private void initComponents() {

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(root);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(520, 420));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(60, 0, 0, 0);
        root.add(panel, gbc);

        /* ---------- TÍTULO ---------- */
        JLabel lblTitulo = new JLabel("CheckPoint", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Wide Latin", Font.PLAIN, 40));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setMaximumSize(new Dimension(500, 60));

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        /* ---------- USERNAME ---------- */
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(280, 32));
        addPlaceholder(txtUsername, "Username");

        panel.add(txtUsername);
        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        /* ---------- CONTRASEÑA ---------- */
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(280, 32));
        addPasswordPlaceholder(txtPassword, "Contraseña");

        panel.add(txtPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));

        /* ---------- RECUPERAR ---------- */
        JLabel lblOlvidado = new JLabel("¿Has olvidado la contraseña?");
        lblOlvidado.setFont(new Font("Arial", Font.BOLD, 11));
        lblOlvidado.setForeground(Color.WHITE);
        lblOlvidado.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRecuperar = crearBotonLilaSuave("Recuperar contraseña");
        btnRecuperar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRecuperar.addActionListener(e -> {
            RecuperarContraseniaDialog dialog = new RecuperarContraseniaDialog(this);
            dialog.setVisible(true);
        });

        panel.add(lblOlvidado);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(btnRecuperar);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        /* ---------- ACCEDER ---------- */
        btnAcceder = crearBotonMorado("Acceder");
        btnAcceder.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        btnAcceder.setMaximumSize(new Dimension(280, 40));
        btnAcceder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAcceder.addActionListener(e -> login());

        panel.add(btnAcceder);
        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        /* ---------- REGISTRO ---------- */
        JLabel lblRegistro = new JLabel("¿No tienes cuenta?");
        lblRegistro.setFont(new Font("Arial", Font.BOLD, 13));
        lblRegistro.setForeground(Color.WHITE);
        lblRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegistrar = crearBotonMorado("Registrarse");
        btnRegistrar.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        btnRegistrar.setMaximumSize(new Dimension(280, 40));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.addActionListener(e -> {
            RegistroDialog dialog = new RegistroDialog(this);
            dialog.setVisible(true);
        });

        panel.add(lblRegistro);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(btnRegistrar);
    }

    /* ---------- LOGIN ---------- */
    private void login() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || username.equals("Username")) {
            JOptionPane.showMessageDialog(this,
                    "Introduce el nombre de usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty() || password.equals("Contraseña")) {
            JOptionPane.showMessageDialog(this,
                    "Introduce la contraseña",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = usuarioDAO.fetchByUsername(username);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!PasswordService.verifyPassword(password, usuario.getPasswordHash())) {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Bienvenido/a " + usuario.getNombre(),
                "Acceso correcto",
                JOptionPane.INFORMATION_MESSAGE);

        abrirPantallaPrincipal(usuario);
        dispose();
    }

    /* ---------- SIGUIENTE PANTALLA (TEMPORAL) ---------- */
    private void abrirPantallaPrincipal(Usuario usuario) {
        System.out.println("Usuario logueado: " + usuario.getUsername());
        // new PantallaPrincipal(usuario).setVisible(true);
    }

    /* ---------- PLACEHOLDERS ---------- */
    private void addPlaceholder(JTextField field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void addPasswordPlaceholder(JPasswordField field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                }
            }
        });
    }

    /* ---------- BOTONES ---------- */
    private JButton crearBotonMorado(String texto) {
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        boton.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMaximumSize(new Dimension(280, 42));

        return boton;
    }

    private JButton crearBotonLilaSuave(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(230, 200, 255),
                        0, getHeight(), new Color(190, 150, 235)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();

                super.paintComponent(g);
            }
        };

        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(160, 110, 210)));
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMaximumSize(new Dimension(200, 30));

        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginVista().setVisible(true));
    }
}
