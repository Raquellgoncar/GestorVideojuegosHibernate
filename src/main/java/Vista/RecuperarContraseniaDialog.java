/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.RecuperarContraseniaController;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ResourceBundle;

/**
 * Diálogo modal encargado de gestionar la recuperación de contraseña
 * mediante el correo electrónico del usuario.
 * <p>
 * El usuario introduce su email y, si es válido y existe en la base de datos,
 * se genera una nueva contraseña que se envía por correo electrónico.
 * </p>
 *
 * La lógica de negocio se delega en {@link RecuperarContraseniaController},
 * manteniendo la separación de responsabilidades según el patrón MVC.
 *
 * @author Raquel
 * @version 1.0
 */
public class RecuperarContraseniaDialog extends JDialog {

    /** Campo de texto para introducir el email */
    private JTextField txtEmail;

    /** Botón para iniciar la recuperación de contraseña */
    private JButton btnRecuperar;

    /** Botón para cancelar y cerrar el diálogo */
    private JButton btnCancelar;

    /** Controlador asociado al proceso de recuperación */
    private RecuperarContraseniaController controller;

    /** Indica si ya se ha generado una contraseña para evitar duplicados */
    private boolean passwordGenerada = false;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /**
     * Constructor del diálogo de recuperación de contraseña.
     *
     * @param parent ventana padre desde la que se abre el diálogo
     */
    public RecuperarContraseniaDialog(JFrame parent) {
        super(parent, true);

        texts = ResourceBundle.getBundle("i18n.messages");
        controller = new RecuperarContraseniaController();

        setTitle(texts.getString("recover.title"));
        setSize(420, 290);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
    }

    /**
     * Inicializa y organiza los componentes gráficos del diálogo.
     */
    private void initComponents() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        /* ================= TÍTULO ================= */
        JLabel lblTitulo = new JLabel(texts.getString("recover.title"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitulo, gbc);

        /* ================= SUBTÍTULO ================= */
        JLabel lblSubtitulo = new JLabel(texts.getString("recover.subtitle"));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(100, 100, 100));

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 8, 8, 8);
        panel.add(lblSubtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8);

        /* ================= EMAIL ================= */
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(texts.getString("recover.email") + ":"), gbc);

        txtEmail = new JTextField(texts.getString("recover.email"));
        txtEmail.setPreferredSize(new Dimension(220, 26));
        txtEmail.setForeground(Color.GRAY);

        /* Placeholder del email */
        txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtEmail.getText().equals(texts.getString("recover.email"))) {
                    txtEmail.setText("");
                    txtEmail.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtEmail.getText().isEmpty()) {
                    txtEmail.setText(texts.getString("recover.email"));
                    txtEmail.setForeground(Color.GRAY);
                }
            }
        });

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtEmail, gbc);

        /* ================= BOTÓN RECUPERAR ================= */
        btnRecuperar = crearBotonMorado(texts.getString("recover.button"));
        btnRecuperar.setPreferredSize(new Dimension(190, 38));
        btnRecuperar.setMaximumSize(new Dimension(190, 38));
        getRootPane().setDefaultButton(btnRecuperar);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(22, 6, 4, 6);
        panel.add(btnRecuperar, gbc);

        /* ================= BOTÓN CANCELAR ================= */
        btnCancelar = crearBotonCancelar(texts.getString("recover.cancel"));

        gbc.gridy = 4;
        gbc.insets = new Insets(6, 6, 6, 6);
        panel.add(btnCancelar, gbc);

        /* ================= ACCIONES ================= */
        btnCancelar.addActionListener(e -> dispose());
        btnRecuperar.addActionListener(e -> recuperarPassword());
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
     * Crea el botón de cancelar como texto plano.
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
     * Valida el email introducido y delega en el controlador
     * la generación y envío de la nueva contraseña.
     */
    private void recuperarPassword() {

        /* Evita generar varias contraseñas */
        if (passwordGenerada) {
            return;
        }

        String email = txtEmail.getText().trim();

        /* Email vacío */
        if (email.isEmpty() || email.equals(texts.getString("recover.email"))) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("recover.error.email.empty"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        /* Formato de email no válido */
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", email)) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("recover.error.email.format"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        /* Delegar la lógica al controlador */
        boolean ok = controller.recuperarPassword(email);

        if (!ok) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("recover.error.email.notfound"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        /* Mensaje de éxito */
        JOptionPane.showMessageDialog(
                this,
                texts.getString("recover.success.message"),
                texts.getString("recover.success.title"),
                JOptionPane.INFORMATION_MESSAGE
        );

        passwordGenerada = true;
        dispose();
    }
}
