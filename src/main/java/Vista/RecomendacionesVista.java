package Vista;

import Modelo.Usuario;
import javax.swing.*;
import java.util.ResourceBundle;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Vista que muestra recomendaciones de videojuegos desde la API de RAWG.
 * <p>
 * Presenta los resultados en tarjetas grandes que incluyen:
 * imagen de portada, título, año, género, puntuación RAWG,
 * descripción breve y enlace directo a la página del juego en RAWG.
 * </p>
 * <p>
 * El filtro puede ser por plataforma más jugada o por género más jugado
 * del usuario, según lo elegido en {@link RecomendarJuegoDialog}.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class RecomendacionesVista extends JDialog {

    /** Clave de la API de RAWG */
    private static final String API_KEY = "ca16f499093f44299894ea4b477fa1ca";

    /** URL base de la API de RAWG */
    private static final String BASE_URL = "https://api.rawg.io/api/games";

    /** Usuario autenticado */
    private Usuario usuario;

    /** Panel de tarjetas con scroll */
    private JPanel panelTarjetas;

    /** Tipo de filtro: "plataforma" o "genero" */
    private String tipoFiltro;

    /** Valor del filtro (nombre de plataforma o género) */
    private String valorFiltro;

    /** Etiqueta de estado de carga */
    private JLabel lblEstado;

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /**
     * Constructor de la vista de recomendaciones.
     *
     * @param padre       ventana padre
     * @param usuario     usuario autenticado
     * @param tipoFiltro  "plataforma" o "genero"
     * @param valorFiltro valor concreto del filtro
     */
    public RecomendacionesVista(JFrame padre, Usuario usuario,
            String tipoFiltro, String valorFiltro) {
        super(padre, true);
        this.usuario = usuario;
        this.tipoFiltro = tipoFiltro;
        this.valorFiltro = valorFiltro;
        this.texts = java.util.ResourceBundle.getBundle("i18n.messages");

        String titulo = tipoFiltro.equals("plataforma")
                ? texts.getString("rec.window.platform") + " " + valorFiltro
                : texts.getString("rec.window.genre") + " " + valorFiltro;

        setTitle(titulo);
        setSize(780, 650);
        setLocationRelativeTo(padre);
        setResizable(true);

        initComponents();
        cargarRecomendaciones();
    }

    /**
     * Inicializa los componentes gráficos de la ventana.
     */
    private void initComponents() {

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(new Color(20, 10, 40));
        setContentPane(root);

        /* ===== CABECERA ===== */
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.setBorder(BorderFactory.createEmptyBorder(20, 25, 5, 25));

        String subtitulo = tipoFiltro.equals("plataforma")
                ? texts.getString("rec.subtitle.platform") + " " + valorFiltro
                : texts.getString("rec.subtitle.genre") + " " + valorFiltro;

        JLabel lblTitulo = new JLabel(subtitulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 20));
        lblTitulo.setForeground(Color.WHITE);
        cabecera.add(lblTitulo, BorderLayout.CENTER);

        lblEstado = new JLabel(texts.getString("rec.loading"), SwingConstants.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblEstado.setForeground(new Color(180, 160, 220));
        cabecera.add(lblEstado, BorderLayout.SOUTH);

        root.add(cabecera, BorderLayout.NORTH);

        /* ===== PANEL DE TARJETAS ===== */
        panelTarjetas = new JPanel();
        panelTarjetas.setLayout(new BoxLayout(panelTarjetas, BoxLayout.Y_AXIS));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scroll = new JScrollPane(panelTarjetas);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        root.add(scroll, BorderLayout.CENTER);

        /* ===== BOTÓN CERRAR ===== */
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        JButton btnCerrar = new JButton(texts.getString("rec.close")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(180, 120, 255),
                        0, getHeight(), new Color(110, 40, 180)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI Black", Font.PLAIN, 13));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setOpaque(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(130, 36));
        btnCerrar.addActionListener(e -> dispose());
        panelSur.add(btnCerrar);
        root.add(panelSur, BorderLayout.SOUTH);
    }

    /**
     * Lanza la búsqueda en RAWG en un hilo secundario para no bloquear la UI.
     */
    private void cargarRecomendaciones() {
        SwingWorker<List<JSONObject>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<JSONObject> doInBackground() throws Exception {
                return llamarAPI();
            }

            @Override
            protected void done() {
                try {
                    List<JSONObject> juegos = get();
                    lblEstado.setText(juegos.isEmpty()
                            ? texts.getString("rec.not.found")
                            : juegos.size() + texts.getString("rec.found.suffix"));
                    for (JSONObject j : juegos) {
                        panelTarjetas.add(crearTarjeta(j));
                        panelTarjetas.add(Box.createRigidArea(new Dimension(0, 16)));
                    }
                    panelTarjetas.revalidate();
                    panelTarjetas.repaint();
                } catch (Exception e) {
                    lblEstado.setText(texts.getString("rec.error.api"));
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    /**
     * Realiza la llamada HTTP a la API de RAWG con el filtro correspondiente.
     *
     * @return lista de objetos JSON con los juegos recomendados
     * @throws Exception si falla la conexión o el parseo
     */
    private List<JSONObject> llamarAPI() throws Exception {
        StringBuilder urlStr = new StringBuilder(BASE_URL);
        urlStr.append("?key=").append(API_KEY);
        urlStr.append("&page_size=10");
        urlStr.append("&ordering=-rating");

        if (tipoFiltro.equals("plataforma")) {
            String id = obtenerIdPlataforma(valorFiltro);
            if (!id.isEmpty()) urlStr.append("&platforms=").append(id);
        } else {
            urlStr.append("&genres=")
                  .append(java.net.URLEncoder.encode(valorFiltro.toLowerCase(), "UTF-8"));
        }

        URL url = new URL(urlStr.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder respuesta = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) respuesta.append(linea);
        br.close();

        JSONArray results = new JSONObject(respuesta.toString()).getJSONArray("results");
        List<JSONObject> juegos = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) juegos.add(results.getJSONObject(i));
        return juegos;
    }

    /**
     * Crea una tarjeta visual para un juego recomendado.
     * <p>
     * Incluye imagen de portada (cargada en hilo secundario), título,
     * año, género, puntuación RAWG, descripción breve y enlace a rawg.io.
     * </p>
     *
     * @param juego objeto JSON del juego
     * @return panel con la tarjeta
     */
    private JPanel crearTarjeta(JSONObject juego) {

        String titulo = juego.optString("name", "Sin título");
        String anio = juego.optString("released", "").length() >= 4
                ? juego.optString("released").substring(0, 4) : "-";
        double rating = juego.optDouble("rating", 0.0);
        String imagenUrl = juego.optString("background_image", null);
        String slug = juego.optString("slug", "");
        String linkRawg = "https://rawg.io/games/" + slug;

        String generoStr = "-";
        if (!juego.isNull("genres") && juego.getJSONArray("genres").length() > 0) {
            List<String> genres = new ArrayList<>();
            JSONArray arr = juego.getJSONArray("genres");
            for (int i = 0; i < Math.min(arr.length(), 3); i++) {
                genres.add(arr.getJSONObject(i).getString("name"));
            }
            generoStr = String.join(", ", genres);
        }

        // Panel principal de la tarjeta
        JPanel tarjeta = new JPanel(new BorderLayout(14, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(45, 28, 75));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        // Panel imagen (izquierda)
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(180, 120));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        lblImagen.setOpaque(true);
        lblImagen.setBackground(new Color(30, 15, 55));
        lblImagen.setText(texts.getString("rec.img.loading"));
        lblImagen.setForeground(new Color(150, 130, 190));
        lblImagen.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        tarjeta.add(lblImagen, BorderLayout.WEST);

        // Cargar imagen en hilo secundario
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            String urlImagen = imagenUrl;
            SwingWorker<ImageIcon, Void> imgWorker = new SwingWorker<>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    URL url = new URL(urlImagen);
                    BufferedImage img = javax.imageio.ImageIO.read(url);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaled);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        ImageIcon icon = get();
                        if (icon != null) {
                            lblImagen.setIcon(icon);
                            lblImagen.setText(null);
                        }
                    } catch (Exception ignored) {}
                }
            };
            imgWorker.execute();
        }

        // Panel de contenido (derecha)
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        // Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(lblTitulo);
        contenido.add(Box.createRigidArea(new Dimension(0, 5)));

        // Año y género
        JLabel lblMeta = new JLabel(anio + "  •  " + generoStr);
        lblMeta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMeta.setForeground(new Color(180, 160, 220));
        lblMeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(lblMeta);
        contenido.add(Box.createRigidArea(new Dimension(0, 6)));

        // Puntuación RAWG
        JLabel lblRating = new JLabel(texts.getString("rec.rating.prefix") + " " + String.format("%.1f", rating) + " / 5");
        lblRating.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRating.setForeground(new Color(255, 195, 0));
        lblRating.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(lblRating);
        contenido.add(Box.createRigidArea(new Dimension(0, 8)));

        // Link a RAWG
        JLabel lblLink = new JLabel("<html><u>" + texts.getString("rec.more.info") + "</u></html>");
        lblLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLink.setForeground(new Color(150, 100, 255));
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(linkRawg));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RecomendacionesVista.this,
                            "No se pudo abrir el enlace:\n" + linkRawg);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lblLink.setForeground(new Color(200, 160, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblLink.setForeground(new Color(150, 100, 255));
            }
        });
        contenido.add(lblLink);

        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    /**
     * Obtiene el ID numérico de la plataforma en RAWG a partir de su nombre.
     *
     * @param nombre nombre de la plataforma
     * @return ID numérico como String, o vacío si no se conoce
     */
    private String obtenerIdPlataforma(String nombre) {
        switch (nombre) {
            case "PC":               return "4";
            case "PlayStation 5":    return "187";
            case "PlayStation 4":    return "18";
            case "PlayStation 3":    return "16";
            case "PlayStation 2":    return "15";
            case "PlayStation":      return "27";
            case "Xbox Series X/S":  return "186";
            case "Xbox One":         return "1";
            case "Nintendo Switch":  return "7";
            case "Nintendo":         return "83";
            case "Nintendo 3DS":     return "8";
            case "PSP":              return "17";
            case "PS Vita":          return "19";
            default:                 return "";
        }
    }
}
