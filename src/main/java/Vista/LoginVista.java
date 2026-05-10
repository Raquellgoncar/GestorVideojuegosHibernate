/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import App.CheckPoint;
import Controlador.LoginController;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Vista principal de inicio de sesión de la aplicación.
 * <p>
 * Permite al usuario autenticarse mediante nombre de usuario y contraseña,
 * acceder al registro de nuevos usuarios, recuperar la contraseña mediante
 * correo electrónico y seleccionar el idioma de la aplicación.
 * </p>
 *
 * Esta vista actúa como punto de entrada a la aplicación y delega la
 * autenticación en {@link LoginController}, siguiendo el patrón MVC.
 *
 * @author Raquel
 * @version 1.0
 */
public class LoginVista extends JFrame {

    /** Campo de texto para el nombre de usuario */
    private JTextField txtUsername;

    /** Campo de texto para la contraseña */
    private JPasswordField txtPassword;

    /** Botones principales de la vista */
    private JButton btnAcceder;
    private JButton btnRegistrar;
    private JButton btnRecuperar;

    /** Imagen de fondo de la ventana */
    private Image imagenFondo;

    /** Controlador encargado de la autenticación */
    private LoginController loginController;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /**
     * Constructor de la vista de login.
     */
    public LoginVista() {
        setTitle("Checkpoint - Login");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        texts = ResourceBundle.getBundle("i18n.messages");
        loginController = new LoginController();

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

    /**
     * Inicializa y organiza todos los componentes gráficos de la ventana.
     */
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
        panel.setPreferredSize(new Dimension(640, 520));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(50, 0, 0, 0);
        root.add(panel, gbc);

        /* ===== TÍTULO ===== */
        JLabel lblTitulo = new JLabel("CheckPoint", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Wide Latin", Font.PLAIN, 50));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setMaximumSize(new Dimension(620, 75));
        lblTitulo.setToolTipText(texts.getString("login.tooltip.title"));

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 85)));

        /* ===== USUARIO ===== */
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        addPlaceholder(txtUsername, texts.getString("login.username"));
        txtUsername.setToolTipText(texts.getString("login.tooltip.username"));

        JLabel lblUserIcon = new JLabel("\uD83D\uDC64");
        lblUserIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        lblUserIcon.setForeground(Color.GRAY);
        lblUserIcon.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 6));

        JPanel wrapUsername = new JPanel(new BorderLayout());
        wrapUsername.setBorder(txtUsername.getBorder());
        wrapUsername.setBackground(txtUsername.getBackground());
        txtUsername.setBorder(null);
        txtUsername.setOpaque(false);
        wrapUsername.setMinimumSize(new Dimension(250, 34));
        wrapUsername.setPreferredSize(new Dimension(250, 34));
        wrapUsername.setMaximumSize(new Dimension(250, 34));
        wrapUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapUsername.add(txtUsername, BorderLayout.CENTER);
        wrapUsername.add(lblUserIcon, BorderLayout.EAST);
        panel.add(wrapUsername);

        panel.add(Box.createRigidArea(new Dimension(0, 22)));

        /* ===== CONTRASEÑA ===== */
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        addPasswordPlaceholder(txtPassword, texts.getString("login.password"));
        txtPassword.setToolTipText(texts.getString("login.tooltip.password"));

        JButton btnOjo = new JButton("\uD83D\uDC41");
        btnOjo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btnOjo.setForeground(Color.GRAY);
        btnOjo.setFocusPainted(false);
        btnOjo.setBorderPainted(false);
        btnOjo.setContentAreaFilled(false);
        btnOjo.setOpaque(false);
        btnOjo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOjo.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 6));
        btnOjo.addActionListener(e -> {
            String pw = new String(txtPassword.getPassword());
            if (pw.equals(texts.getString("login.password"))) return;
            if (txtPassword.getEchoChar() != (char) 0) {
                txtPassword.setEchoChar((char) 0);
                btnOjo.setText("<html><s>\uD83D\uDC41</s></html>");
            } else {
                txtPassword.setEchoChar('\u2022');
                btnOjo.setText("\uD83D\uDC41");
            }
        });

        JPanel wrapPassword = new JPanel(new BorderLayout());
        wrapPassword.setBorder(txtPassword.getBorder());
        wrapPassword.setBackground(txtPassword.getBackground());
        txtPassword.setBorder(null);
        txtPassword.setOpaque(false);
        wrapPassword.setMinimumSize(new Dimension(250, 34));
        wrapPassword.setPreferredSize(new Dimension(250, 34));
        wrapPassword.setMaximumSize(new Dimension(250, 34));
        wrapPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapPassword.add(txtPassword, BorderLayout.CENTER);
        wrapPassword.add(btnOjo, BorderLayout.EAST);
        panel.add(wrapPassword);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        /* ===== ACCEDER ===== */
        btnAcceder = crearBotonMorado(texts.getString("login.access"));
        btnAcceder.setFont(new Font("Segoe UI Black", Font.PLAIN, 25));
        btnAcceder.setMaximumSize(new Dimension(275, 50));
        btnAcceder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAcceder.setToolTipText(texts.getString("login.tooltip.access"));
        btnAcceder.addActionListener(e -> login());

        getRootPane().setDefaultButton(btnAcceder);
        panel.add(btnAcceder);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel lblOlvidado = new JLabel(texts.getString("login.forgot"));
        lblOlvidado.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblOlvidado.setForeground(Color.WHITE);
        lblOlvidado.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblOlvidado.setToolTipText(texts.getString("login.tooltip.forgot"));

        /* ===== RECUPERAR CONTRASEÑA ===== */
        btnRecuperar = crearBotonLilaSuave(texts.getString("login.recover"));
        btnRecuperar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRecuperar.setMinimumSize(new Dimension(225, 38));
        btnRecuperar.setPreferredSize(new Dimension(225, 38));
        btnRecuperar.setMaximumSize(new Dimension(225, 38));
        btnRecuperar.setToolTipText(texts.getString("login.tooltip.recover"));
        btnRecuperar.addActionListener(e -> {
            RecuperarContraseniaDialog dialog = new RecuperarContraseniaDialog(this);
            dialog.setVisible(true);
        });

        panel.add(lblOlvidado);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(btnRecuperar);

        panel.add(Box.createRigidArea(new Dimension(0, 38)));

        /* ===== REGISTRO ===== */
        JLabel lblRegistro = new JLabel(texts.getString("login.noaccount"));
        lblRegistro.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblRegistro.setForeground(Color.WHITE);
        lblRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRegistro.setToolTipText(texts.getString("login.tooltip.register.label"));

        btnRegistrar = crearBotonMorado(texts.getString("login.register"));
        btnRegistrar.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
        btnRegistrar.setMaximumSize(new Dimension(275, 40));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setToolTipText(texts.getString("login.tooltip.register.button"));
        btnRegistrar.addActionListener(e -> {
            RegistroDialog dialog = new RegistroDialog(this);
            dialog.setVisible(true);
        });

        panel.add(lblRegistro);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(btnRegistrar);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        /* ===== IDIOMA ===== */
        JButton btnIdioma = crearBotonMorado(texts.getString("login.language"));
        btnIdioma.setIcon(crearIconoBandera(Locale.getDefault().getLanguage()));
        btnIdioma.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnIdioma.setIconTextGap(8);
        btnIdioma.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        btnIdioma.setMaximumSize(new Dimension(225, 40));
        btnIdioma.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIdioma.setToolTipText(texts.getString("login.tooltip.language"));
        btnIdioma.addActionListener(e -> seleccionarIdioma());

        panel.add(btnIdioma);

        SwingUtilities.invokeLater(() -> root.requestFocusInWindow());
    }

    /**
     * Realiza el proceso de autenticación del usuario.
     */
    private void login() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || username.equals(texts.getString("login.username"))) {
            JOptionPane.showMessageDialog(this, texts.getString("error.username"), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty() || password.equals(texts.getString("login.password"))) {
            JOptionPane.showMessageDialog(this, texts.getString("error.password"), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = loginController.autenticar(username, password);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, texts.getString("error.login"),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();
        new SplashScreenVista(usuario).setVisible(true);
    }

    /**
     * Muestra un diálogo para seleccionar el idioma de la aplicación.
     */
    private void seleccionarIdioma() {

        JDialog dialog = new JDialog(this, texts.getString("language.title"), true);
        dialog.setSize(320, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel lblTexto = new JLabel(texts.getString("language.select"), SwingConstants.CENTER);
        lblTexto.setFont(new Font("Arial", Font.BOLD, 16));
        lblTexto.setForeground(new Color(30, 30, 50));
        lblTexto.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        dialog.add(lblTexto, BorderLayout.NORTH);

        JRadioButton rbEs = new JRadioButton();
        JRadioButton rbEn = new JRadioButton();

        ButtonGroup group = new ButtonGroup();
        group.add(rbEs);
        group.add(rbEn);

        if (Locale.getDefault().getLanguage().equals("en")) {
            rbEn.setSelected(true);
        } else {
            rbEs.setSelected(true);
        }

        JPanel pEs = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pEs.setOpaque(false);
        pEs.add(rbEs);
        pEs.add(new JLabel(crearIconoBandera("es")));
        pEs.add(new JLabel(texts.getString("language.es")));
        pEs.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { rbEs.setSelected(true); }
        });

        JPanel pEn = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pEn.setOpaque(false);
        pEn.add(rbEn);
        pEn.add(new JLabel(crearIconoBandera("en")));
        pEn.add(new JLabel(texts.getString("language.en")));
        pEn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { rbEn.setSelected(true); }
        });

        JPanel panelRadios = new JPanel();
        panelRadios.add(pEs);
        panelRadios.add(pEn);
        dialog.add(panelRadios, BorderLayout.CENTER);

        JButton btnCancelar = crearBotonCancelar(texts.getString("language.cancel"));
        JButton btnAceptar = crearBotonMorado(texts.getString("language.accept"));
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAceptar.setPreferredSize(new Dimension(92, 32));

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnAceptar.addActionListener(e -> {
            if (rbEs.isSelected()) {
                Locale.setDefault(new Locale("es"));
            } else {
                Locale.setDefault(new Locale("en"));
            }
            CheckPoint.aplicarIdiomaBotones();
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

    /**
     * Añade un placeholder a un campo de texto.
     *
     * @param field campo de texto
     * @param text texto del placeholder
     */
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

    /**
     * Añade un placeholder específico para campos de contraseña.
     *
     * @param field campo de contraseña
     * @param text texto del placeholder
     */
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

    /**
     * Crea un botón principal con estilo morado.
     *
     * @param texto texto del botón
     * @return botón configurado
     */
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

    /**
     * Crea un botón secundario con estilo lila suave.
     *
     * @param texto texto del botón
     * @return botón configurado
     */
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boton.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        boton.setForeground(new Color(140, 80, 190));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Crea un botÃ³n de cancelar como texto plano.
     *
     * @param texto texto del botÃ³n
     * @return botÃ³n configurado
     */
    private JButton crearBotonCancelar(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        boton.setForeground(new Color(110, 40, 180));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Crea un icono de bandera dibujado programáticamente.
     *
     * @param lang código de idioma ("es" o "en")
     * @return icono con la bandera correspondiente
     */
    private ImageIcon crearIconoBandera(String lang) {
        int w = 18, h = 12;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if ("en".equals(lang)) {
            // Fondo azul
            g.setColor(new Color(0x01, 0x21, 0x69));
            g.fillRect(0, 0, w, h);
            // Aspas blancas (diagonales)
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(3));
            g.drawLine(0, 0, w, h);
            g.drawLine(w, 0, 0, h);
            // Cruz blanca
            g.setStroke(new BasicStroke(5));
            g.drawLine(w / 2, 0, w / 2, h);
            g.drawLine(0, h / 2, w, h / 2);
            // Cruz roja encima
            g.setColor(new Color(0xC8, 0x10, 0x2E));
            g.setStroke(new BasicStroke(3));
            g.drawLine(w / 2, 0, w / 2, h);
            g.drawLine(0, h / 2, w, h / 2);
        } else {
            // España: franjas rojo-amarillo-rojo
            g.setColor(new Color(0xC6, 0x0B, 0x1E));
            g.fillRect(0, 0, w, h / 3 + 1);
            g.setColor(new Color(0xFF, 0xC4, 0x00));
            g.fillRect(0, h / 3, w, h / 3 + 2);
            g.setColor(new Color(0xC6, 0x0B, 0x1E));
            g.fillRect(0, 2 * h / 3, w, h / 3 + 1);
        }

        g.dispose();
        return new ImageIcon(img);
    }
}
