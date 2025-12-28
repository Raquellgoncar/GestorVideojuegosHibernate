/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.DAO.FavoritoDAO;
import Modelo.DAO.Imp.FavoritoDAO_imp;
import Modelo.DAO.Imp.VideojuegoDAO_imp;
import Modelo.DAO.VideojuegoDAO;
import Modelo.Videojuego;
import Modelo.Favorito;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Raquel
 */
public class ActualizarVideojuegoDialog extends JDialog {

    private JTextField txtTitulo, txtPlataforma, txtAnio, txtValoracion;
    private JToggleButton btnFavorito;
    private JButton btnAceptar, btnCancelar;

    private Usuario usuario;
    private Videojuego videojuego;
    private boolean eraFavorito;

    private VideojuegoDAO videojuegoDAO = new VideojuegoDAO_imp();
    private FavoritoDAO favoritoDAO = new FavoritoDAO_imp();

    public ActualizarVideojuegoDialog(
            JFrame parent,
            Usuario usuario,
            Videojuego videojuego,
            boolean esFavorito
    ) {
        super(parent, true);
        this.usuario = usuario;
        this.videojuego = videojuego;
        this.eraFavorito = esFavorito;

        setTitle("Actualizar videojuego");
        setSize(560, 580);
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();
        cargarDatos();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
        add(root, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 10, 14, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===== TÍTULO ===== */
        JLabel lblTitulo = new JLabel("ACTUALIZAR VIDEOJUEGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 34));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        root.add(lblTitulo, gbc);

        /* ===== NOMBRE DEL JUEGO ===== */
        JLabel lblNombre = new JLabel(videojuego.getTitulo(), SwingConstants.CENTER);
        lblNombre.setFont(new Font("Arial Black", Font.PLAIN, 22));
        lblNombre.setForeground(Color.DARK_GRAY);
        lblNombre.setBorder(BorderFactory.createEmptyBorder(6, 0, 10, 0));

        gbc.gridy++;
        root.add(lblNombre, gbc);

        gbc.gridwidth = 1;

        Font fuenteLabel = new Font("Arial", Font.BOLD, 15);

        /* ===== CAMPOS ===== */
        gbc.gridy++;

        JLabel lblT = new JLabel("Título:");
        lblT.setFont(fuenteLabel);
        root.add(lblT, gbc);

        txtTitulo = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblP = new JLabel("Plataforma:");
        lblP.setFont(fuenteLabel);
        root.add(lblP, gbc);

        txtPlataforma = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtPlataforma, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblA = new JLabel("Año:");
        lblA.setFont(fuenteLabel);
        root.add(lblA, gbc);

        txtAnio = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtAnio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblV = new JLabel("Valoración:");
        lblV.setFont(fuenteLabel);
        root.add(lblV, gbc);

        txtValoracion = crearCampoTexto();
        gbc.gridx = 1;
        root.add(txtValoracion, gbc);

        /* ===== FAVORITO ===== */
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JPanel panelFav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFav.setOpaque(false);

        btnFavorito = new JToggleButton();
        btnFavorito.setIcon(cargarIconoEscalado("/img/estrellagris.png", 42, 42));
        btnFavorito.setSelectedIcon(cargarIconoEscalado("/img/estrellaamarilla.png", 42, 42));
        btnFavorito.setBorderPainted(false);
        btnFavorito.setContentAreaFilled(false);
        btnFavorito.setFocusPainted(false);
        btnFavorito.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblFav = new JLabel("Favorito");
        lblFav.setFont(new Font("Arial", Font.BOLD, 14));

        panelFav.add(btnFavorito);
        panelFav.add(lblFav);

        root.add(panelFav, gbc);

        /* ===== PANEL INFERIOR ===== */
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 25, 18, 25));
        panelInferior.setOpaque(false);

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzq.setOpaque(false);

        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDer.setOpaque(false);

        btnCancelar = crearBotonMorado(
                "Cancelar",
                new Dimension(120, 32),
                e -> dispose()
        );

        btnAceptar = crearBotonMorado(
                "Aceptar",
                new Dimension(200, 38),
                e -> validarYActualizar()
        );

        panelIzq.add(btnCancelar);
        panelDer.add(btnAceptar);

        panelInferior.add(panelIzq, BorderLayout.WEST);
        panelInferior.add(panelDer, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(260, 34));
        return campo;
    }

    private void activarPlaceholderEditable(JTextField campo, String valorInicial) {

        campo.setText(valorInicial);
        campo.setForeground(Color.BLACK);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(valorInicial)) {
                    campo.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setText(valorInicial);
                }
            }
        });
    }

    private void cargarDatos() {

        activarPlaceholderEditable(txtTitulo, videojuego.getTitulo());
        activarPlaceholderEditable(txtPlataforma, videojuego.getPlataforma());
        activarPlaceholderEditable(txtAnio, String.valueOf(videojuego.getAnio()));
        activarPlaceholderEditable(
                txtValoracion,
                videojuego.getValoracion().toString()
        );

        btnFavorito.setSelected(eraFavorito);
    }

    private JButton crearBotonMorado(
            String texto,
            Dimension tamaño,
            ActionListener action
    ) {
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
        boton.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(tamaño);

        boton.addActionListener(action);
        return boton;
    }

    private void validarYActualizar() {

        if (txtTitulo.getText().trim().isEmpty()
                || txtPlataforma.getText().trim().isEmpty()
                || txtAnio.getText().trim().isEmpty()
                || txtValoracion.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Rellena todos los campos.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas guardar los cambios?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) return;

        try {
            videojuego.setTitulo(txtTitulo.getText().trim());
            videojuego.setPlataforma(txtPlataforma.getText().trim());
            videojuego.setAnio(Integer.parseInt(txtAnio.getText().trim()));
            videojuego.setValoracion(new BigDecimal(txtValoracion.getText().trim()));

            videojuegoDAO.update(videojuego);

            boolean ahoraFavorito = btnFavorito.isSelected();

            if (ahoraFavorito && !eraFavorito) {
                Favorito f = new Favorito();
                f.setUsuarioId(usuario);
                f.setVideojuegoId(videojuego);
                f.setFechaAnadido(new Date());
                favoritoDAO.insert(f);

            } else if (!ahoraFavorito && eraFavorito) {
                favoritoDAO.deleteByUsuarioYVideojuego(
                        usuario.getId(),
                        videojuego.getId()
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Videojuego actualizado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el videojuego.");
        }
    }

    private ImageIcon cargarIconoEscalado(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
