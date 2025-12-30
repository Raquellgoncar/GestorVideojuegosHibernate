/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.RegistroController;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

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
        setSize(420, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
    }

    /**
     * Inicializa y distribuye los componentes gráficos del diálogo.
     */
    private void initComponents() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        /* ================= USERNAME ================= */
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel(texts.getString("register.username") + ":"), gbc);

        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(200, 26));
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        /* ================= NOMBRE ================= */
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(texts.getString("register.name") + ":"), gbc);

        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(200, 26));
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        /* ================= EMAIL ================= */
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(texts.getString("register.email") + ":"), gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(200, 26));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        /* ================= CONTRASEÑA ================= */
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(texts.getString("register.password") + ":"), gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 26));
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        /* ================= BOTÓN CANCELAR ================= */
        btnCancelar = new JButton(texts.getString("register.cancel"));
        btnCancelar.setPreferredSize(new Dimension(90, 30));

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(20, 6, 0, 6);
        panel.add(btnCancelar, gbc);

        /* ================= BOTÓN REGISTRAR ================= */
        btnRegistrar = new JButton(texts.getString("register.button"));
        btnRegistrar.setPreferredSize(new Dimension(130, 34));
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 13));

        /* Permite enviar el formulario pulsando Enter */
        getRootPane().setDefaultButton(btnRegistrar);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnRegistrar, gbc);

        /* ================= ACCIONES ================= */
        btnCancelar.addActionListener(e -> dispose());
        btnRegistrar.addActionListener(e -> registrarUsuario());
    }

    /**
     * Valida los datos introducidos por el usuario y delega el registro
     * al controlador.
     * <p>
     * Se comprueban campos vacíos, formato del email y posibles duplicados
     * de username o email.
     * </p>
     */
    private void registrarUsuario() {

        String username = txtUsername.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());

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
