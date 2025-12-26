/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Usuario;
import Modelo.DAO.UsuarioDAO;
import Modelo.DAO.Imp.UsuarioDAO_imp;
import Modelo.util.PasswordService;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;


/**
 *
 * @author Raquel
 */
public class RegistroDialog extends JDialog {

    private JTextField txtUsername;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    private UsuarioDAO usuarioDAO = new UsuarioDAO_imp();

    public RegistroDialog(JFrame parent) {
        super(parent, "Registro de usuario", true);
        setSize(420, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        // USERNAME
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(200, 26));
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        // NOMBRE
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Nombre:"), gbc);

        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(200, 26));
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        // EMAIL
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(200, 26));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // CONTRASEÑA
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 26));
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // BOTÓN CANCELAR
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(90, 30));

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(20, 6, 0, 6);
        panel.add(btnCancelar, gbc);

        // BOTÓN REGISTRAR
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setPreferredSize(new Dimension(130, 34));
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 13));

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnRegistrar, gbc);

        // ACCIONES
        btnCancelar.addActionListener(e -> dispose());
        btnRegistrar.addActionListener(e -> registrarUsuario());
    }

    // ---------------- LÓGICA DE REGISTRO ----------------
    private void registrarUsuario() {

        String username = txtUsername.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());

        //COMPROBACIONES 
        
        // 1. CAMPOS VACÍOS
        if (username.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. EMAIL VÁLIDO
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            JOptionPane.showMessageDialog(this,
                    "El email no tiene un formato válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. USERNAME EXISTENTE
        if (usuarioDAO.fetchByUsername(username) != null) {
            JOptionPane.showMessageDialog(this,
                    "El username ya existe",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. EMAIL EXISTENTE 
        if (usuarioDAO.fetchByEmail(email) != null) {
            JOptionPane.showMessageDialog(this,
                    "El email ya está registrado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 5. CREAR USUARIO
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPasswordHash(PasswordService.hashPassword(password));

        usuarioDAO.insert(u);

        JOptionPane.showMessageDialog(this,
                "Usuario registrado correctamente",
                "Registro",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}