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
import com.formdev.flatlaf.FlatLightLaf;

import java.util.Locale;
import java.util.ResourceBundle;

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
    private ResourceBundle texts;

    public LoginVista() {
        setTitle("Checkpoint - Login");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        texts = ResourceBundle.getBundle("i18n.messages");

        var url = getClass().getResource("/img/imagenFondo.png");
        if (url != null) {
            imagenFondo = new ImageIcon(url).getImage();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encuentra la imagen /img/imagenFondo.png",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        initComponents();
    }

    private void initComponents() {

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
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

        JLabel lblTitulo = new JLabel("CheckPoint", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Wide Latin", Font.PLAIN, 40));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setMaximumSize(new Dimension(500, 60));
        lblTitulo.setToolTipText("Aplicación de gestión de videojuegos");

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(280, 32));
        addPlaceholder(txtUsername, texts.getString("login.username"));
        txtUsername.setToolTipText("Introduce tu nombre de usuario");

        panel.add(txtUsername);
        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(280, 32));
        addPasswordPlaceholder(txtPassword, texts.getString("login.password"));
        txtPassword.setToolTipText("Introduce tu contraseña");

        panel.add(txtPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));

        JLabel lblOlvidado = new JLabel(texts.getString("login.forgot"));
        lblOlvidado.setFont(new Font("Arial", Font.BOLD, 11));
        lblOlvidado.setForeground(Color.WHITE);
        lblOlvidado.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblOlvidado.setToolTipText("Pulsa para recuperar tu contraseña");

        btnRecuperar = crearBotonLilaSuave(texts.getString("login.recover"));
        btnRecuperar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRecuperar.setToolTipText("Recuperar contraseña mediante email");
        btnRecuperar.addActionListener(e -> {
            RecuperarContraseniaDialog dialog = new RecuperarContraseniaDialog(this);
            dialog.setVisible(true);
        });

        panel.add(lblOlvidado);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(btnRecuperar);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        btnAcceder = crearBotonMorado(texts.getString("login.access"));
        btnAcceder.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        btnAcceder.setMaximumSize(new Dimension(280, 40));
        btnAcceder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAcceder.setToolTipText("Acceder a la aplicación");
        btnAcceder.addActionListener(e -> login());

        getRootPane().setDefaultButton(btnAcceder);
        
        panel.add(btnAcceder);
        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        JLabel lblRegistro = new JLabel(texts.getString("login.noaccount"));
        lblRegistro.setFont(new Font("Arial", Font.BOLD, 13));
        lblRegistro.setForeground(Color.WHITE);
        lblRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRegistro.setToolTipText("Crear una nueva cuenta");

        btnRegistrar = crearBotonMorado(texts.getString("login.register"));
        btnRegistrar.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        btnRegistrar.setMaximumSize(new Dimension(280, 40));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setToolTipText("Registrar un nuevo usuario");
        btnRegistrar.addActionListener(e -> {
            RegistroDialog dialog = new RegistroDialog(this);
            dialog.setVisible(true);
        });

        panel.add(lblRegistro);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(btnRegistrar);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnIdioma = crearBotonMorado(texts.getString("login.language"));
        btnIdioma.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        btnIdioma.setMaximumSize(new Dimension(200, 34));
        btnIdioma.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIdioma.setToolTipText("Seleccionar el idioma de la aplicación");
        btnIdioma.addActionListener(e -> seleccionarIdioma());

        panel.add(btnIdioma);

        SwingUtilities.invokeLater(() -> root.requestFocusInWindow());
    }

    private void login() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || username.equals(texts.getString("login.username"))) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("error.username"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (password.isEmpty() || password.equals(texts.getString("login.password"))) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("error.password"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Usuario usuario = usuarioDAO.fetchByUsername(username);

        if (usuario == null || !PasswordService.verifyPassword(password, usuario.getPasswordHash())) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("error.login"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        abrirPantallaPrincipal(usuario);
        dispose();
    }

    private void abrirPantallaPrincipal(Usuario usuario) {
        dispose();
        new SplashScreenVista(usuario).setVisible(true);
    }

    private void seleccionarIdioma() {

        JDialog dialog = new JDialog(this, texts.getString("language.title"), true);
        dialog.setSize(320, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel lblTexto = new JLabel(texts.getString("language.select"), SwingConstants.CENTER);
        lblTexto.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        dialog.add(lblTexto, BorderLayout.NORTH);

        JRadioButton rbEs = new JRadioButton(texts.getString("language.es"));
        JRadioButton rbEn = new JRadioButton(texts.getString("language.en"));

        ButtonGroup group = new ButtonGroup();
        group.add(rbEs);
        group.add(rbEn);

        if (Locale.getDefault().getLanguage().equals("en")) {
            rbEn.setSelected(true);
        } else {
            rbEs.setSelected(true);
        }

        JPanel panelRadios = new JPanel();
        panelRadios.add(rbEs);
        panelRadios.add(rbEn);
        dialog.add(panelRadios, BorderLayout.CENTER);

        JButton btnCancelar = new JButton(texts.getString("language.cancel"));
        JButton btnAceptar = new JButton(texts.getString("language.accept"));

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnAceptar.addActionListener(e -> {
            if (rbEs.isSelected()) {
                Locale.setDefault(new Locale("es"));
            } else if (rbEn.isSelected()) {
                Locale.setDefault(new Locale("en"));
            }

            dialog.dispose();
            dispose();
            new LoginVista().setVisible(true);
        });

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panelBotones.add(btnCancelar, BorderLayout.WEST);
        panelBotones.add(btnAceptar, BorderLayout.EAST);

        dialog.add(panelBotones, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

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

        boton.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        boton.setForeground(new Color(140, 80, 190));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(160, 110, 210)));
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMaximumSize(new Dimension(200, 30));

        return boton;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> new LoginVista().setVisible(true));
    }
}

