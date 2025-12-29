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
 *
 * @author Raquel
 */
public class RecuperarContraseniaDialog extends JDialog {

    private JTextField txtEmail;
    private JButton btnRecuperar;
    private JButton btnCancelar;

    private RecuperarContraseniaController controller;
    private boolean passwordGenerada = false;

    private ResourceBundle texts;

    public RecuperarContraseniaDialog(JFrame parent) {
        super(parent, true);

        texts = ResourceBundle.getBundle("i18n.messages");
        controller = new RecuperarContraseniaController();

        setTitle(texts.getString("recover.title"));
        setSize(420, 260);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        /* ---------- TÍTULO ---------- */
        JLabel lblTitulo = new JLabel(texts.getString("recover.title"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        /* ---------- EMAIL ---------- */
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(texts.getString("recover.email") + ":"), gbc);

        txtEmail = new JTextField(texts.getString("recover.email"));
        txtEmail.setPreferredSize(new Dimension(220, 26));
        txtEmail.setForeground(Color.GRAY);

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

        /* ---------- BOTÓN RECUPERAR ---------- */
        btnRecuperar = new JButton(texts.getString("recover.button"));

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnRecuperar, gbc);

        /* ---------- BOTÓN CANCELAR ---------- */
        btnCancelar = new JButton(texts.getString("recover.cancel"));

        gbc.gridy = 3;
        gbc.insets = new Insets(15, 6, 6, 6);
        panel.add(btnCancelar, gbc);

        /* ---------- ACCIONES ---------- */
        btnCancelar.addActionListener(e -> dispose());
        btnRecuperar.addActionListener(e -> recuperarPassword());
    }

    private void recuperarPassword() {

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

        /* Email válido */
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", email)) {
            JOptionPane.showMessageDialog(
                    this,
                    texts.getString("recover.error.email.format"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        /* delegar lógica al controlador */
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

