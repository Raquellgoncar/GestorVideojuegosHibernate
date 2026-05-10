package Vista;

import Controlador.InsertarVideojuegoController;
import Modelo.DAO.Imp.PlataformaDAO_imp;
import Modelo.DAO.PlataformaDAO;
import Modelo.Plataforma;
import Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.util.ResourceBundle;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Diálogo para buscar videojuegos en el catálogo de la API de RAWG.
 * <p>
 * El usuario elige una plataforma del desplegable (cargado desde la BD),
 * escribe el nombre del juego y obtiene una lista de resultados. Al
 * seleccionar uno, se abre el formulario de inserción con título, género
 * e imagen ya rellenos automáticamente.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class BuscarEnCatalogoDialog extends JDialog {

    /** Clave de la API de RAWG */
    private static final String API_KEY = "ca16f499093f44299894ea4b477fa1ca";

    /** URL base de la API de RAWG */
    private static final String BASE_URL = "https://api.rawg.io/api/games";

    /** Usuario autenticado */
    private Usuario usuario;

    /** Ventana padre */
    private ModificarVideojuegosVista padre;

    /** Desplegable de plataformas */
    private JComboBox<Plataforma> cmbPlataforma;

    /** Campo de búsqueda */
    private JTextField txtBuscar;

    /** Lista de resultados */
    private JList<String> listaResultados;

    /** Modelo de la lista */
    private DefaultListModel<String> modeloLista;

    /** Botones */
    private JButton btnBuscar, btnSeleccionar, btnCancelar;

    /** Datos cacheados de los juegos encontrados */
    private List<JSONObject> juegosCacheados = new ArrayList<>();

    /** Recursos de internacionalización */
    private ResourceBundle texts;

    /**
     * Constructor del diálogo de búsqueda en catálogo.
     *
     * @param padre   ventana padre
     * @param usuario usuario autenticado
     */
    public BuscarEnCatalogoDialog(ModificarVideojuegosVista padre, Usuario usuario) {
        super(padre, true);
        this.padre = padre;
        this.usuario = usuario;
        this.texts = ResourceBundle.getBundle("i18n.messages");

        setTitle(texts.getString("catalog.title"));
        setSize(560, 480);
        setLocationRelativeTo(padre);
        setResizable(false);

        initComponents();
    }

    /**
     * Inicializa y organiza los componentes gráficos del diálogo.
     */
    private void initComponents() {

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        root.setBackground(new Color(25, 15, 45));
        setContentPane(root);

        /* ===== TÍTULO ===== */
        JLabel lblTitulo = new JLabel(texts.getString("catalog.title"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Showcard Gothic", Font.PLAIN, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        root.add(lblTitulo, BorderLayout.NORTH);

        /* ===== PANEL DE BÚSQUEDA ===== */
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fuenteLabel = new Font("Segoe UI", Font.BOLD, 13);
        Color colorLabel = new Color(200, 180, 255);

        // Plataforma
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblPlat = new JLabel(texts.getString("catalog.platform"));
        lblPlat.setFont(fuenteLabel);
        lblPlat.setForeground(colorLabel);
        panelBusqueda.add(lblPlat, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        cmbPlataforma = new JComboBox<>();
        cmbPlataforma.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cargarPlataformas();
        panelBusqueda.add(cmbPlataforma, gbc);

        // Búsqueda
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblBuscar = new JLabel(texts.getString("catalog.name"));
        lblBuscar.setFont(fuenteLabel);
        lblBuscar.setForeground(colorLabel);
        panelBusqueda.add(lblBuscar, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.setPreferredSize(new Dimension(300, 30));
        panelBusqueda.add(txtBuscar, gbc);

        // Botón buscar
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        btnBuscar = crearBotonMorado("<html><font face='Segoe UI Symbol'>⌕</font>&nbsp;&nbsp;" + texts.getString("filter.search.button") + "</html>", new Dimension(160, 34));
        panelBusqueda.add(btnBuscar, gbc);

        root.add(panelBusqueda, BorderLayout.NORTH);

        /* ===== LISTA DE RESULTADOS ===== */
        modeloLista = new DefaultListModel<>();
        listaResultados = new JList<>(modeloLista);
        listaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaResultados.setBackground(new Color(45, 30, 70));
        listaResultados.setForeground(Color.WHITE);
        listaResultados.setSelectionBackground(new Color(110, 40, 180));
        listaResultados.setSelectionForeground(Color.WHITE);
        listaResultados.setFixedCellHeight(32);

        JScrollPane scroll = new JScrollPane(listaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(110, 40, 180)));
        root.add(scroll, BorderLayout.CENTER);

        /* ===== PANEL INFERIOR ===== */
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCancelar = new JButton(texts.getString("insert.cancel"));
        btnCancelar.setForeground(Color.LIGHT_GRAY);
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSeleccionar = crearBotonMorado(texts.getString("catalog.select"), new Dimension(200, 36));
        btnSeleccionar.setEnabled(false);

        panelSur.add(btnCancelar, BorderLayout.WEST);
        panelSur.add(btnSeleccionar, BorderLayout.EAST);
        root.add(panelSur, BorderLayout.SOUTH);

        /* ===== ACCIONES ===== */
        btnBuscar.addActionListener(e -> buscarJuegos());
        btnCancelar.addActionListener(e -> volverASeleccionModo());
        btnSeleccionar.addActionListener(e -> seleccionarJuego());

        // Activar botón seleccionar cuando hay selección
        listaResultados.addListSelectionListener(e -> {
            btnSeleccionar.setEnabled(listaResultados.getSelectedIndex() != -1);
        });

        // Buscar también con Enter en el campo de texto
        txtBuscar.addActionListener(e -> buscarJuegos());
    }

    /**
     * Cierra la busqueda en catalogo y vuelve al dialogo donde se elige
     * entre buscar en catalogo o introducir el juego manualmente.
     */
    private void volverASeleccionModo() {
        dispose();
        padre.setVisible(true);
        new SeleccionarModoInsertarDialog(padre, usuario).setVisible(true);
    }

    /**
     * Carga las plataformas desde la base de datos.
     */
    private void cargarPlataformas() {
        try {
            PlataformaDAO dao = new PlataformaDAO_imp();
            List<Plataforma> plataformas = dao.fetchAll();
            for (Plataforma p : plataformas) {
                cmbPlataforma.addItem(p);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar plataformas: " + e.getMessage());
        }
    }

    /**
     * Realiza la búsqueda de juegos en la API de RAWG.
     * <p>
     * Filtra por la plataforma seleccionada y el texto introducido.
     * Los resultados se muestran en la lista. Se ejecuta en un hilo
     * secundario para no bloquear la interfaz.
     * </p>
     */
    private void buscarJuegos() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, texts.getString("catalog.empty.search"));
            return;
        }

        Plataforma plataforma = (Plataforma) cmbPlataforma.getSelectedItem();
        String slug = plataforma != null ? plataforma.getRawgSlug() : null;

        modeloLista.clear();
        juegosCacheados.clear();
        btnSeleccionar.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnBuscar.setText(texts.getString("catalog.searching"));

        SwingWorker<List<JSONObject>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<JSONObject> doInBackground() throws Exception {
                return llamarAPI(busqueda, slug);
            }

            @Override
            protected void done() {
                try {
                    List<JSONObject> juegos = get();
                    if (juegos.isEmpty()) {
                        modeloLista.addElement(texts.getString("catalog.no.results"));
                    } else {
                        for (JSONObject j : juegos) {
                            juegosCacheados.add(j);
                            String nombre = j.getString("name");
                            int anio = 0;
                            if (!j.isNull("released") && !j.getString("released").isEmpty()) {
                                anio = Integer.parseInt(j.getString("released").substring(0, 4));
                            }
                            modeloLista.addElement(nombre + (anio > 0 ? "  (" + anio + ")" : ""));
                        }
                    }
                } catch (Exception e) {
                    modeloLista.addElement(texts.getString("catalog.error.api"));
                    e.printStackTrace();
                } finally {
                    btnBuscar.setEnabled(true);
                    btnBuscar.setText("<html><font face='Segoe UI Symbol'>⌕</font>&nbsp;&nbsp;" + texts.getString("filter.search.button") + "</html>");
                }
            }
        };

        worker.execute();
    }

    /**
     * Construye la URL y realiza la llamada HTTP a la API de RAWG.
     *
     * @param busqueda texto de búsqueda
     * @param slug     slug de la plataforma (puede ser null)
     * @return lista de objetos JSON con los juegos encontrados
     * @throws Exception si falla la conexión o el parseo
     */
    private List<JSONObject> llamarAPI(String busqueda, String slug) throws Exception {
        StringBuilder urlStr = new StringBuilder(BASE_URL);
        urlStr.append("?key=").append(API_KEY);
        urlStr.append("&search=").append(java.net.URLEncoder.encode(busqueda, "UTF-8"));
        urlStr.append("&page_size=15");
        if (slug != null && !slug.isEmpty()) {
            urlStr.append("&platforms=").append(obtenerIdPlataformaRawg(slug));
        }

        URL url = new URL(urlStr.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
        );
        StringBuilder respuesta = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            respuesta.append(linea);
        }
        br.close();

        JSONObject json = new JSONObject(respuesta.toString());
        JSONArray results = json.getJSONArray("results");

        List<JSONObject> juegos = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            juegos.add(results.getJSONObject(i));
        }
        return juegos;
    }

    /**
     * Obtiene el ID numérico de la plataforma en RAWG a partir de su slug.
     * <p>
     * RAWG usa IDs numéricos para filtrar por plataforma en la búsqueda,
     * no slugs. Este mapa cubre las plataformas del catálogo.
     * </p>
     *
     * @param slug slug de la plataforma
     * @return ID numérico de RAWG o cadena vacía si no se conoce
     */
    private String obtenerIdPlataformaRawg(String slug) {
        switch (slug) {
            case "pc":              return "4";
            case "playstation5":    return "187";
            case "playstation4":    return "18";
            case "playstation3":    return "16";
            case "playstation2":    return "15";
            case "playstation":     return "27";
            case "xbox-series-x":   return "186";
            case "xbox-one":        return "1";
            case "nintendo-switch": return "7";
            case "nintendo-switch-2": return ""; // aún no en RAWG
            case "nes":             return "83";
            case "nintendo-3ds":    return "8";
            case "psp":             return "17";
            case "ps-vita":         return "19";
            default:                return "";
        }
    }

    /**
     * Selecciona el juego elegido de la lista y abre el formulario de inserción
     * con los datos prerrellenados desde la API.
     */
    private void seleccionarJuego() {
        int idx = listaResultados.getSelectedIndex();
        if (idx < 0 || idx >= juegosCacheados.size()) return;

        JSONObject juego = juegosCacheados.get(idx);

        String titulo = juego.getString("name");

        Integer anio = null;
        if (!juego.isNull("released") && !juego.getString("released").isEmpty()) {
            try {
                anio = Integer.parseInt(juego.getString("released").substring(0, 4));
            } catch (NumberFormatException ignored) {}
        }

        String genero = "";
        if (!juego.isNull("genres") && juego.getJSONArray("genres").length() > 0) {
            genero = juego.getJSONArray("genres").getJSONObject(0).getString("name");
        }

        String imagenUrl = "";
        if (!juego.isNull("background_image")) {
            imagenUrl = juego.getString("background_image");
        }

        Plataforma plataforma = (Plataforma) cmbPlataforma.getSelectedItem();
        String nombrePlataforma = plataforma != null ? plataforma.getNombre() : "";

        dispose();

        InsertarVideojuegoVista vista = new InsertarVideojuegoVista(usuario, padre, true);
        vista.prerellenarDesdeAPI(titulo, nombrePlataforma, anio, genero, imagenUrl);
        vista.setVisible(true);
    }

    /**
     * Crea un botón con estilo morado.
     *
     * @param texto  texto del botón
     * @param tamaño tamaño del botón
     * @return botón configurado
     */
    private JButton crearBotonMorado(String texto, Dimension tamaño) {
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI Black", Font.PLAIN, 13));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(tamaño);
        return boton;
    }
}
