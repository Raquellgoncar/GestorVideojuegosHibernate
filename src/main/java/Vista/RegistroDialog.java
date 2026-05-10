package Vista;

import Controlador.RegistroController;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Diálogo modal encargado del registro de nuevos usuarios en la aplicación.
 * <p>
 * Permite introducir los datos básicos de un usuario (username, nombre, email
 * y contraseña) y delega la validación y el alta en el sistema al
 * {@link RegistroController}, siguiendo el patrón MVC.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class RegistroDialog extends JDialog {

    /** Campo de texto para el nombre de usuario */
    private JTextField txtUsername;

    /** Campo de texto para el nombre real del usuario */
    private JTextField txtNombre;

    /** Campo de texto para el email */
    private JTextField txtEmail;

    /** Campo de contraseña */
    private JPasswordField txtPassword;

    /** Etiqueta de error de contraseña */
    private JLabel lblPasswordError;

    /** Botón para confirmar el registro */
    private JButton btnRegistrar;

    /** Botón para cancelar el registro */
    private JButton btnCancelar;

    /** Controlador encargado de la lógica de registro */
    private RegistroController registroController;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /**
     * Constructor del diálogo de registro.
     *
     * @param parent ventana padre desde la que se abre el diálogo
     */
    public RegistroDialog(JFrame parent) {
        super(parent, true);

        texts = ResourceBundle.getBundle("i18n.messages");
        registroController = new RegistroController();

        setTitle(texts.getString("register.title"));
        setSize(380, 530);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    /**
     * Inicializa y distribuye los componentes gráficos del diálogo.
     */
    private void initComponents() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        add(panel);

        /* ================= TÍTULO ================= */
        JLabel lblTitulo = new JLabel(texts.getString("register.title.header"));
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(30, 30, 50));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitulo);

        panel.add(Box.createVerticalStrut(6));

        /* ================= SUBTÍTULO ================= */
        JLabel lblSubtitulo = new JLabel("<html>" + texts.getString("register.subtitle") + "</html>");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(120, 120, 140));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblSubtitulo);

        panel.add(Box.createVerticalStrut(24));

        /* ================= USERNAME ================= */
        panel.add(crearLabel(texts.getString("register.username").toUpperCase()));
        panel.add(Box.createVerticalStrut(4));
        txtUsername = crearCampo(texts.getString("register.placeholder.username"));
        panel.add(crearPanelConIcono(txtUsername, "\uD83D\uDC64"));

        panel.add(Box.createVerticalStrut(12));

        /* ================= NOMBRE ================= */
        panel.add(crearLabel(texts.getString("register.name").toUpperCase()));
        panel.add(Box.createVerticalStrut(4));
        txtNombre = crearCampo(texts.getString("register.placeholder.nombre"));
        panel.add(crearPanelConIcono(txtNombre, "\uD83D\uDC64"));

        panel.add(Box.createVerticalStrut(12));

        /* ================= EMAIL ================= */
        panel.add(crearLabel(texts.getString("register.email").toUpperCase()));
        panel.add(Box.createVerticalStrut(4));
        txtEmail = crearCampo(texts.getString("register.placeholder.email"));
        panel.add(crearPanelConIcono(txtEmail, "@"));

        panel.add(Box.createVerticalStrut(12));

        /* ================= CONTRASEÑA ================= */
        panel.add(crearLabel(texts.getString("register.password").toUpperCase()));
        panel.add(Box.createVerticalStrut(4));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBackground(new Color(242, 242, 247));
        txtPassword.setBorder(null);
        String pwPlaceholder = texts.getString("register.placeholder.password");
        txtPassword.setEchoChar((char) 0);
        txtPassword.setText(pwPlaceholder);
        txtPassword.setForeground(new Color(160, 160, 170));
        txtPassword.putClientProperty("placeholder", pwPlaceholder);
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (String.valueOf(txtPassword.getPassword()).equals(pwPlaceholder)) {
                    txtPassword.setText("");
                    txtPassword.setEchoChar('\u2022');
                    txtPassword.setForeground(new Color(40, 40, 50));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtPassword.getPassword().length == 0) {
                    txtPassword.setEchoChar((char) 0);
                    txtPassword.setText(pwPlaceholder);
                    txtPassword.setForeground(new Color(160, 160, 170));
                }
            }
        });
        panel.add(crearPanelPasswordConOjo(pwPlaceholder));

        panel.add(Box.createVerticalStrut(3));

        /* ================= ERROR CONTRASEÑA ================= */
        lblPasswordError = new JLabel(" ");
        lblPasswordError.setForeground(Color.RED);
        lblPasswordError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPasswordError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPasswordError.setMinimumSize(new Dimension(0, 16));
        lblPasswordError.setPreferredSize(new Dimension(0, 16));
        lblPasswordError.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        panel.add(lblPasswordError);

        panel.add(Box.createVerticalStrut(8));

        /* ================= BOTÓN REGISTRAR ================= */
        btnRegistrar = crearBotonMorado(texts.getString("register.button"));
        btnRegistrar.setPreferredSize(new Dimension(155, 38));
        btnRegistrar.setMaximumSize(new Dimension(155, 38));
        getRootPane().setDefaultButton(btnRegistrar);
        JPanel panelRegistrar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelRegistrar.setBackground(Color.WHITE);
        panelRegistrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelRegistrar.add(btnRegistrar);
        panel.add(panelRegistrar);

        panel.add(Box.createVerticalStrut(6));

        /* ================= BOTÓN CANCELAR ================= */
        btnCancelar = crearBotonCancelar(texts.getString("register.cancel"));
        JPanel panelCancelar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelCancelar.setBackground(Color.WHITE);
        panelCancelar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCancelar.add(btnCancelar);
        panel.add(panelCancelar);

        /* ================= ACCIONES ================= */
        btnCancelar.addActionListener(e -> dispose());
        btnRegistrar.addActionListener(e -> registrarUsuario());
    }

    /**
     * Crea una etiqueta de campo con estilo uppercase pequeño.
     *
     * @param texto texto de la etiqueta
     * @return etiqueta configurada
     */
    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(80, 80, 100));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    /**
     * Crea un campo de texto con placeholder manual: aparece en gris al inicio
     * y desaparece al hacer clic (focus), restaurándose si se deja vacío.
     *
     * @param placeholder texto de ayuda del campo
     * @return campo configurado
     */
    private JTextField crearCampo(String placeholder) {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBackground(new Color(242, 242, 247));
        campo.setBorder(null);
        campo.setText(placeholder);
        campo.setForeground(new Color(160, 160, 170));
        campo.putClientProperty("placeholder", placeholder);
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(new Color(40, 40, 50));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(new Color(160, 160, 170));
                }
            }
        });
        return campo;
    }

    /**
     * Envuelve un campo de texto en un panel con icono a la izquierda,
     * fondo gris redondeado y sin borde visible.
     *
     * @param campo  campo de texto o contraseña
     * @param icono  carácter Unicode que representa el icono
     * @return panel compuesto con icono + campo
     */
    private JPanel crearPanelConIcono(JTextField campo, String icono) {
        JPanel wrapper = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(242, 242, 247));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        lblIcono.setForeground(new Color(160, 160, 170));

        campo.setBackground(new Color(242, 242, 247));
        campo.setBorder(null);
        campo.setOpaque(true);

        GridBagConstraints gbcIcon = new GridBagConstraints();
        gbcIcon.gridx = 0;
        gbcIcon.gridy = 0;
        gbcIcon.anchor = GridBagConstraints.CENTER;
        gbcIcon.insets = new Insets(0, 0, 0, 8);
        wrapper.add(lblIcono, gbcIcon);

        GridBagConstraints gbcField = new GridBagConstraints();
        gbcField.gridx = 1;
        gbcField.gridy = 0;
        gbcField.fill = GridBagConstraints.HORIZONTAL;
        gbcField.weightx = 1.0;
        gbcField.anchor = GridBagConstraints.CENTER;
        wrapper.add(campo, gbcField);

        return wrapper;
    }

    private JPanel crearPanelPasswordConOjo(String placeholder) {
        JPanel wrapper = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(242, 242, 247));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 8));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword.setBackground(new Color(242, 242, 247));
        txtPassword.setBorder(null);
        txtPassword.setOpaque(true);

        JButton btnOjo = new JButton("\uD83D\uDC41");
        btnOjo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btnOjo.setForeground(new Color(160, 160, 170));
        btnOjo.setFocusPainted(false);
        btnOjo.setBorderPainted(false);
        btnOjo.setContentAreaFilled(false);
        btnOjo.setOpaque(false);
        btnOjo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOjo.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        btnOjo.addActionListener(e -> {
            String pw = String.valueOf(txtPassword.getPassword());
            if (pw.equals(placeholder)) return;
            if (txtPassword.getEchoChar() != (char) 0) {
                txtPassword.setEchoChar((char) 0);
                btnOjo.setText("<html><s>\uD83D\uDC41</s></html>");
            } else {
                txtPassword.setEchoChar('\u2022');
                btnOjo.setText("\uD83D\uDC41");
            }
        });
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (String.valueOf(txtPassword.getPassword()).equals(placeholder)) {
                    btnOjo.setText("\uD83D\uDC41");
                }
            }
        });

        GridBagConstraints gbcField = new GridBagConstraints();
        gbcField.gridx = 0;
        gbcField.gridy = 0;
        gbcField.fill = GridBagConstraints.HORIZONTAL;
        gbcField.weightx = 1.0;
        gbcField.anchor = GridBagConstraints.CENTER;
        wrapper.add(txtPassword, gbcField);

        GridBagConstraints gbcOjo = new GridBagConstraints();
        gbcOjo.gridx = 1;
        gbcOjo.gridy = 0;
        gbcOjo.anchor = GridBagConstraints.CENTER;
        wrapper.add(btnOjo, gbcOjo);

        return wrapper;
    }

    /**
     * Crea el botón principal con degradado morado.
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
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Crea el botón de cancelar como texto plano (sin fondo ni borde).
     *
     * @param texto texto del botón
     * @return botón configurado
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
     * Devuelve el texto real de un campo, o cadena vacía si aún muestra el placeholder.
     *
     * @param campo campo de texto
     * @return texto introducido por el usuario, sin espacios
     */
    private String getCampoTexto(JTextField campo) {
        String placeholder = (String) campo.getClientProperty("placeholder");
        String texto = campo.getText().trim();
        return texto.equals(placeholder) ? "" : texto;
    }

    /**
     * Devuelve la contraseña real, o cadena vacía si aún muestra el placeholder.
     *
     * @return contraseña introducida por el usuario
     */
    private String getPasswordTexto() {
        String placeholder = (String) txtPassword.getClientProperty("placeholder");
        String texto = String.valueOf(txtPassword.getPassword()).trim();
        return texto.equals(placeholder) ? "" : texto;
    }

    /**
     * Valida los datos introducidos por el usuario y delega el registro
     * al controlador.
     * <p>
     * Se comprueban campos vacíos, formato del email, seguridad de la
     * contraseña y posibles duplicados de username o email.
     * </p>
     */
    private void registrarUsuario() {

        String username = getCampoTexto(txtUsername);
        String nombre = getCampoTexto(txtNombre);
        String email = getCampoTexto(txtEmail);
        String password = getPasswordTexto();

        /* ===== CAMPOS VACÍOS ===== */
        if (username.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("register.error.empty"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        /* ===== EMAIL VÁLIDO ===== */
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("register.error.email.format"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        /* ===== CONTRASEÑA SEGURA ===== */
        if (!Pattern.matches("^(?=.*[a-zA-Z]).{8,}$", password)) {
            lblPasswordError.setText(texts.getString("register.error.password.format"));
            return;
        }
        lblPasswordError.setText(" ");

        try {
            registroController.registrar(username, nombre, email, password);

            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("register.success.message"),
                    texts.getString("register.success.title"),
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (IllegalArgumentException ex) {

            /* Username ya existente */
            if ("USERNAME_EXISTE".equals(ex.getMessage())) {
                JOptionPane.showMessageDialog(
                        this,
                        texts.getString("register.error.username.exists"),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

            /* Email ya existente */
            } else if ("EMAIL_EXISTE".equals(ex.getMessage())) {
                JOptionPane.showMessageDialog(
                        this,
                        texts.getString("register.error.email.exists"),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
