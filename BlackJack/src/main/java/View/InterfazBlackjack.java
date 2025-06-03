package View;

import entidades.Carta;
import entidades.Jugador;
import entidades.ManoJugador;
import entidades.Partida;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import Controller.BlackjackService;
import Controller.BlackjackService.ManoJugadorService;
import Controller.JugadorService;
import Controller.PartidaService;
import java.util.stream.Collectors;

public class InterfazBlackjack extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    // Conexion con la base de datos
    private BlackjackService blackjackService = new BlackjackService();
    private ManoJugadorService manoJugadorService = new ManoJugadorService();
    private JugadorService jugadorService;
    private PartidaService partidaService;

    // Componentes login
    private JTextField txtUsuarioLogin;
    private JPasswordField txtPasswordLogin;
    private JCheckBox chkMostrarContraseñaLogin;

    // Componentes registro
    private JTextField txtUsuarioRegistro;
    private JPasswordField txtPasswordRegistro;
    private JCheckBox chkMostrarContraseñaRegistro;

    // Componentes juego
    private ManoJugador manoActual;
    private Jugador jugadorActual;
    private Partida partidaActual;
    private ArrayList<Integer> manoJugador;
    private ArrayList<Integer> manoBanca;
    private JButton btnIniciarSesion, btnRegistrarse, btnSalirInicio,
            btnNuevaPartida, btnContinuar, btnSalirMenuJuego,
            btnApostar, btnPedirCarta, btnPasar, btnGuardarPartida;

    private JLabel lblCartasJugador, lblSumaJugador, lblApuesta, lblDinero;
    private JLabel lblCartasBanca, lblSumaBanca;
    private int dineroJugador = 100; // Dinero inicial jugador
    private int apuestaActual = 0; // Apuesta actual

    public InterfazBlackjack() {
        // Inicializar servicios
        blackjackService = new BlackjackService();
        jugadorService = new JugadorService();
        partidaService = new PartidaService();
        manoJugadorService = new ManoJugadorService(); // Si la tienes definida

        // Configuracion de la ventana
        setTitle("Blackjack");
        setSize(800, 600);  // Tamaño fijo en lugar de pantalla completa
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuracion principal con CardLayout para cambiar pantallas
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        add(panelPrincipal);

        // Creación de las pantallas
        crearMenuInicio();
        crearPantallaLogin();
        crearPantallaRegistro();
        crearMenuJuego();
        crearMesaJuego();

        // Mostrar pantalla inicial
        cardLayout.show(panelPrincipal, "Inicio");
    }

    // ---Creacion de Menus con sus contenidos---
    
    // Menu inicial con opciones Iniciar Sesion, Registrarse y Salir
    private void crearMenuInicio() {
        // Se carga el GIF de fondo "fondo1.gif" y se crea el panel auxiliar para el fondo.
        ImageIcon fondoIcon = new ImageIcon(getClass().getResource("/fondo1.gif"));
        PanelDeFondo backgroundPanel = new PanelDeFondo(fondoIcon.getImage());
        backgroundPanel.setLayout(new BorderLayout());

        // Se crea el panel original con GridLayout para los botones
        JPanel panelInicio = new JPanel(new GridLayout(3, 1, 10, 10));
        panelInicio.setOpaque(false); // Importante para ver el fondo

        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnRegistrarse = new JButton("Registrarse");
        btnSalirInicio = new JButton("Salir");

        // Ajustamos el tamaño y estilo de los botones para que se vea bien el fondo
        Font btnFont = new Font("Arial", Font.BOLD, 36);
        btnIniciarSesion.setFont(btnFont);
        btnRegistrarse.setFont(btnFont);
        btnSalirInicio.setFont(btnFont);

        // Hacer los botones semitransparentes para que no tapen el fondo
        btnIniciarSesion.setOpaque(false);
        btnIniciarSesion.setContentAreaFilled(false);
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));

        btnRegistrarse.setOpaque(false);
        btnRegistrarse.setContentAreaFilled(false);
        btnRegistrarse.setForeground(Color.WHITE);
        btnRegistrarse.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));

        btnSalirInicio.setOpaque(false);
        btnSalirInicio.setContentAreaFilled(false);
        btnSalirInicio.setForeground(Color.WHITE);
        btnSalirInicio.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));

        panelInicio.add(btnIniciarSesion);
        panelInicio.add(btnRegistrarse);
        panelInicio.add(btnSalirInicio);

        // Se coloca el panel de botones en el centro del BackgroundPanel, para que se vea el fondo completo.
        backgroundPanel.add(panelInicio, BorderLayout.CENTER);

        // Se anade el BackgroundPanel al panel principal con su etiqueta.
        panelPrincipal.add(backgroundPanel, "Inicio");

        // Acciones sin cambios en la logica
        btnIniciarSesion.addActionListener(e -> cardLayout.show(panelPrincipal, "Login"));
        btnRegistrarse.addActionListener(e -> cardLayout.show(panelPrincipal, "Registro"));
        btnSalirInicio.addActionListener(e -> System.exit(0));
    }

    // Pantalla de login con usuario, contraseña y boton aceptar
    private void crearPantallaLogin() {
        JPanel loginPanel = new JPanel(new GridLayout(7, 1, 5, 5));

        txtUsuarioLogin = new JTextField();
        txtPasswordLogin = new JPasswordField();
        chkMostrarContraseñaLogin = new JCheckBox("Mostrar Contraseña");
        JButton btnAceptarLogin = new JButton("Aceptar");
        JButton btnCancelarLogin = new JButton("Cancelar");

        loginPanel.add(new JLabel("Usuario:"));
        loginPanel.add(txtUsuarioLogin);
        loginPanel.add(new JLabel("Contraseña:"));
        loginPanel.add(txtPasswordLogin);
        loginPanel.add(chkMostrarContraseñaLogin);
        loginPanel.add(btnAceptarLogin);
        loginPanel.add(btnCancelarLogin);

        panelPrincipal.add(loginPanel, "Login");

        // Mostrar u ocultar contraseña
        chkMostrarContraseñaLogin.addActionListener(e -> {
            txtPasswordLogin.setEchoChar(chkMostrarContraseñaLogin.isSelected() ? (char) 0 : '*');
        });

        // Validacion antes de permitir el inicio de sesion
        btnAceptarLogin.addActionListener(e -> {
            String usuario = txtUsuarioLogin.getText().trim();
            String contraseña = new String(txtPasswordLogin.getPassword()).trim();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes completar ambos campos para iniciar sesión.");
                return;
            }

            Jugador jugador = blackjackService.buscarJugadorPorNombre(usuario);
            if (jugador != null && jugador.getContraseña().equals(contraseña)) {
                dineroJugador = jugador.getDinero();
                lblDinero.setText("Dinero disponible: " + dineroJugador);
                cardLayout.show(panelPrincipal, "MenuJuego");
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        });

        // Accion del boton cancelar
        btnCancelarLogin.addActionListener(e -> {
            // Limpia los campos antes de volver al menu de inicio
            txtUsuarioLogin.setText("");
            txtPasswordLogin.setText("");

            cardLayout.show(panelPrincipal, "Inicio");
        });
    }

    // Pantalla de registro con usuario, contraseña y boton aceptar
    private void crearPantallaRegistro() {
        JPanel registroPanel = new JPanel(new GridLayout(7, 1, 5, 5)); // Mantengo el formato sin cambios

        txtUsuarioRegistro = new JTextField();
        txtPasswordRegistro = new JPasswordField();
        chkMostrarContraseñaRegistro = new JCheckBox("Mostrar Contraseña");
        JButton btnAceptarRegistro = new JButton("Aceptar");
        JButton btnCancelarRegistro = new JButton("Cancelar");

        registroPanel.add(new JLabel("Usuario:"));
        registroPanel.add(txtUsuarioRegistro);
        registroPanel.add(new JLabel("Contraseña:"));
        registroPanel.add(txtPasswordRegistro);
        registroPanel.add(chkMostrarContraseñaRegistro);
        registroPanel.add(btnAceptarRegistro);
        registroPanel.add(btnCancelarRegistro);

        panelPrincipal.add(registroPanel, "Registro");

        // Mostrar u ocultar contraseña
        chkMostrarContraseñaRegistro.addActionListener(e -> {
            txtPasswordRegistro.setEchoChar(chkMostrarContraseñaRegistro.isSelected() ? (char) 0 : '*');
        });

        // Validacion antes de permitir el registro
        btnAceptarRegistro.addActionListener(e -> {
            String usuario = txtUsuarioRegistro.getText().trim();
            String contraseña = new String(txtPasswordRegistro.getPassword()).trim();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes completar ambos campos para registrarte.");
                return;
            }

            if (blackjackService.buscarJugadorPorNombre(usuario) != null) {
                JOptionPane.showMessageDialog(this, "El usuario ya existe. Elige otro.");
                return;
            }

            // Se crea y guarda el nuevo usuario sin cambiar el flujo visual
            Jugador nuevoJugador = new Jugador(usuario, contraseña, 100);
            try {
                blackjackService.guardarJugador(nuevoJugador);
                JOptionPane.showMessageDialog(this, "Registro exitoso. Por favor inicia sesión.");
                cardLayout.show(panelPrincipal, "Inicio");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario.");
            }
        });

        // Accion del boton cancelar
        btnCancelarRegistro.addActionListener(e -> {
            // Limpia los campos antes de volver al menu de inicio
            txtUsuarioRegistro.setText("");
            txtPasswordRegistro.setText("");

            cardLayout.show(panelPrincipal, "Inicio");
        });

    }

    // Menu del Juego
    private void crearMenuJuego() {
        // Se carga el GIF de fondo "fondo2.gif" y se crea un MyBackgroundPanel
        ImageIcon fondoIcon = new ImageIcon(getClass().getResource("/fondo2.gif"));
        PanelDeFondo backgroundPanel = new PanelDeFondo(fondoIcon.getImage());
        backgroundPanel.setLayout(new BorderLayout());

        // Se crea el panel original (con GridLayout) para los botones y se configura como transparente
        JPanel menuJuego = new JPanel(new GridLayout(3, 1, 10, 10));
        menuJuego.setOpaque(false);

        // Se crean los botones
        btnNuevaPartida = new JButton("Nueva Partida");
        btnContinuar = new JButton("Continuar Partida");
        btnSalirMenuJuego = new JButton("Salir");

        // Ajustamos el estilo de los botones: fuente grande, texto blanco, sin relleno y con borde semitransparente
        Font btnFont = new Font("Arial", Font.BOLD, 36);
        btnNuevaPartida.setFont(btnFont);
        btnContinuar.setFont(btnFont);
        btnSalirMenuJuego.setFont(btnFont);

        btnNuevaPartida.setOpaque(false);
        btnNuevaPartida.setContentAreaFilled(false);
        btnNuevaPartida.setForeground(Color.WHITE);
        btnNuevaPartida.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));

        btnContinuar.setOpaque(false);
        btnContinuar.setContentAreaFilled(false);
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));

        btnSalirMenuJuego.setOpaque(false);
        btnSalirMenuJuego.setContentAreaFilled(false);
        btnSalirMenuJuego.setForeground(Color.WHITE);
        btnSalirMenuJuego.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));

        // Se añaden los botones al panel con GridLayout
        menuJuego.add(btnNuevaPartida);
        menuJuego.add(btnContinuar);
        menuJuego.add(btnSalirMenuJuego);

        // Se añade el panel de botones al centro del BackgroundPanel
        backgroundPanel.add(menuJuego, BorderLayout.CENTER);
        panelPrincipal.add(backgroundPanel, "MenuJuego");

        // Lógica sin modificar
        // Nueva partida: iniciar, mostrar mesa y hacer visible el botón de guardar
        btnNuevaPartida.addActionListener(e -> {
            iniciarNuevaPartida();
            cardLayout.show(panelPrincipal, "MesaJuego");
            JOptionPane.showMessageDialog(this, "Recuerda no pasarte de 21.");
            btnGuardarPartida.setVisible(true);
        });

        // Continuar partida: si existe, actualizar la interfaz y cargar la partida
        btnContinuar.addActionListener(e -> {
            Jugador jugador = blackjackService.buscarJugadorPorNombre(txtUsuarioLogin.getText());
            Partida partidaGuardada = blackjackService.buscarPartidaAbierta(jugador);

            // Si no se encuentra o la partida tiene saldo 0, se redirige de vuelta al menú.
            if (partidaGuardada == null || partidaGuardada.getDineroActual() <= 0) {
                JOptionPane.showMessageDialog(this, "Tu dinero actual es 0, crea una nueva partida.");
                cardLayout.show(panelPrincipal, "MenuJuego");
                return;
            }

            // Si existe partida, se carga normalmente:
            dineroJugador = partidaGuardada.getDineroActual();
            apuestaActual = 0;

            List<ManoJugador> manos = blackjackService.cargarManosDePartida(partidaGuardada.getIdPartida());
            manoJugador = new ArrayList<>();
            manoBanca = new ArrayList<>();

            // Recorremos todos los registros de ManoJugador que se han cargado.
            for (ManoJugador mano : manos) {
                // Obtiene la lista de enteros almacenada en la entidad.
                for (Integer cartaValor : mano.getManoJugador()) {
                    // Si el valor es mayor a 10, lo transforma a 10.
                    int valorCarta = cartaValor > 10 ? 10 : cartaValor;
                    if (mano.isEs_jugador()) {
                        manoJugador.add(valorCarta);
                    } else {
                        manoBanca.add(valorCarta);
                    }
                }
            }

            lblDinero.setText("Dinero disponible: " + dineroJugador);
            lblApuesta.setText("Apuesta: " + apuestaActual);
            lblCartasJugador.setText("Cartas jugador: " + manoJugador);
            actualizarSuma(manoJugador, lblSumaJugador);
            lblCartasBanca.setText("Cartas banca: " + manoBanca);
            actualizarSuma(manoBanca, lblSumaBanca);

            cardLayout.show(panelPrincipal, "MesaJuego");
            btnGuardarPartida.setVisible(true);
        });

        // Salir vuelve al menu inicio
        btnSalirMenuJuego.addActionListener(e -> cardLayout.show(panelPrincipal, "Inicio"));
    }

    // ---Creacion de la mesa del juego y sus componentes para que funcione---
    
    // Pantalla principal del juego: muestra cartas, suma, dinero y botones de accion
    private void crearMesaJuego() {
        // Se carga el GIF de fondo ("/fondo.gif") y se crea un MyBackgroundPanel.
        // Se usa URL para verificar que la imagen exista.
        URL gifURL = getClass().getResource("/fondo.gif");
        PanelDeFondo mesaPanel;
        if (gifURL == null) {
            System.err.println("❌No se encontró el GIF en /fondo.gif");
            mesaPanel = new PanelDeFondo(null);
        } else {
            ImageIcon fondoIcon = new ImageIcon(gifURL);
            mesaPanel = new PanelDeFondo(fondoIcon.getImage());
        }
        mesaPanel.setLayout(new BorderLayout(20, 20)); // Espacio entre componentes

        // Panel izquierdo con la información (cartas, suma, dinero, apuesta)
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false); // Para que se vea el fondo
        panelInfo.setBorder(BorderFactory.createEmptyBorder(100, 20, 20, 20));

        lblDinero = new JLabel("Dinero disponible: " + dineroJugador);
        lblApuesta = new JLabel("Apuesta: " + apuestaActual);
        lblCartasJugador = new JLabel("Cartas jugador: []");
        lblSumaJugador = new JLabel("Suma jugador: 0");
        lblCartasBanca = new JLabel("Cartas banca: []");
        lblSumaBanca = new JLabel("Suma banca: 0");

        // Se ajustan los colores y fuente para que las etiquetas se vean sobre el fondo
        Font infoFont = new Font("Arial", Font.BOLD, 24);
        Color labelColor = Color.WHITE;
        lblDinero.setFont(infoFont);
        lblApuesta.setFont(infoFont);
        lblCartasJugador.setFont(infoFont);
        lblSumaJugador.setFont(infoFont);
        lblCartasBanca.setFont(infoFont);
        lblSumaBanca.setFont(infoFont);
        lblDinero.setForeground(labelColor);
        lblApuesta.setForeground(labelColor);
        lblCartasJugador.setForeground(labelColor);
        lblSumaJugador.setForeground(labelColor);
        lblCartasBanca.setForeground(labelColor);
        lblSumaBanca.setForeground(labelColor);

        panelInfo.add(lblDinero);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblApuesta);
        panelInfo.add(Box.createVerticalStrut(30));
        panelInfo.add(lblCartasJugador);
        panelInfo.add(lblSumaJugador);
        panelInfo.add(Box.createVerticalStrut(20));
        panelInfo.add(lblCartasBanca);
        panelInfo.add(lblSumaBanca);

        // Panel derecho con los botones (dispuestos verticalmente y centrados)
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false); // Para mostrar el fondo
        panelBotones.setBorder(BorderFactory.createEmptyBorder(100, 20, 20, 20));

        btnApostar = new JButton("Elegir apuesta");
        btnPedirCarta = new JButton("Pedir carta");
        btnPasar = new JButton("Plantarse");
        btnGuardarPartida = new JButton("Guardar partida");

        Dimension btnSize = new Dimension(200, 50);
        Font btnFont = new Font("Arial", Font.BOLD, 20);
        JButton[] botones = {btnApostar, btnPedirCarta, btnPasar, btnGuardarPartida};
        for (JButton btn : botones) {
            btn.setFont(btnFont);
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Se configuran los botones para ser semitransparentes (se ve el fondo)
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));
            panelBotones.add(btn);
            panelBotones.add(Box.createVerticalStrut(10));
        }

        btnGuardarPartida.setVisible(false);

        // Se anaden el panel de información (en el centro) y el de botones (al este)
        mesaPanel.add(panelInfo, BorderLayout.CENTER);
        mesaPanel.add(panelBotones, BorderLayout.EAST);

        // Se anade el MyBackgroundPanel (mesaPanel) al panelPrincipal del CardLayout
        panelPrincipal.add(mesaPanel, "MesaJuego");

        // Listeners para los botones (se conserva la lógica original)
        btnApostar.addActionListener(e -> elegirApuesta());
        btnPedirCarta.addActionListener(e -> {
            if (apuestaActual <= 0) {
                JOptionPane.showMessageDialog(this, "Debes elegir una apuesta antes de jugar.");
                return;
            }
            pedirCartaJugador();
        });
        btnPasar.setText("Plantarse");
        btnPasar.addActionListener(e -> {
            if (apuestaActual <= 0) {
                JOptionPane.showMessageDialog(this, "Debes elegir una apuesta antes de jugar.");
                return;
            }
            turnoBanca();
        });

        btnGuardarPartida.addActionListener(e -> guardarPartida());
    }

    // Metodo para iniciar nueva partida: limpiar manos y pedir apuesta
    private void iniciarNuevaPartida() {
        manoJugador = new ArrayList<>();
        manoBanca = new ArrayList<>();
        apuestaActual = 0;
        dineroJugador = 100;

        lblDinero.setText("Dinero disponible: " + dineroJugador);
        lblApuesta.setText("Apuesta: " + apuestaActual);

        lblCartasJugador.setText("Cartas jugador: []");
        lblSumaJugador.setText("Suma jugador: 0");
        lblCartasBanca.setText("Cartas banca: []");
        lblSumaBanca.setText("Suma banca: 0");

        btnGuardarPartida.setVisible(false);

        // Inicializamos manoActual para que permanezca durante toda la partida
        manoActual = new ManoJugador();
        manoActual.setJugador(jugadorActual);
        manoActual.setPartida(partidaActual);
        manoActual.setEs_jugador(true);
        manoActual.setIdCartas(new ArrayList<>()); // Asegurar que la lista esté vacía al inicio
    }

    // Mostrar dialogo para elegir apuesta, validar cantidad y actualizar
    private void elegirApuesta() {
        JDialog dialog = new JDialog(this, "Elegir apuesta", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        JPanel panelInput = new JPanel();
        panelInput.add(new JLabel("Cantidad a apostar:"));
        JTextField txtApuesta = new JTextField(10);
        panelInput.add(txtApuesta);

        JPanel panelBotones = new JPanel();
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        dialog.add(panelInput, BorderLayout.CENTER);
        dialog.add(panelBotones, BorderLayout.SOUTH);

        btnAceptar.addActionListener(e -> {
            try {
                int apuesta = Integer.parseInt(txtApuesta.getText());
                if (apuesta > 0 && apuesta <= dineroJugador) {
                    apuestaActual = apuesta;
                    dineroJugador -= apuesta;
                    lblApuesta.setText("Apuesta: " + apuestaActual);
                    lblDinero.setText("Dinero disponible: " + dineroJugador);
                    btnGuardarPartida.setVisible(true);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Apuesta inválida o saldo insuficiente.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese un número válido.");
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // Inicia reparto de cartas 
    private void pedirCartaJugador() {
        // Se obtiene el objeto Carta (en lugar de solo int)
        Carta cartaObtenida = blackjackService.obtenerCartaAleatoria();
        if (cartaObtenida == null) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener una carta.");
            return;
        }

        // Obtener el valor de la carta (reduciendo a 10 si procede)
        int valor = cartaObtenida.getValor();
        if (valor > 10) {
            valor = 10;
        }

        // Agregar el valor a la lista del jugador (para mostrar en la interfaz)
        manoJugador.add(valor);
        lblCartasJugador.setText("Cartas jugador: " + manoJugador);
        actualizarSuma(manoJugador, lblSumaJugador);

        // Asegurar que manoActual se mantenga durante toda la partida
        if (manoActual == null) {
            System.out.println("DEBUG: manoActual es null, inicializando nueva instancia.");
            manoActual = new ManoJugador();
            manoActual.setJugador(jugadorActual);
            manoActual.setPartida(partidaActual);
            manoActual.setEs_jugador(true);
            manoActual.setIdCartas(new ArrayList<>());
        }

        // Agregar el ID de la carta a la lista de manoActual (acumulando correctamente)
        manoActual.getIdCartas().add(cartaObtenida.getIdCarta());
        System.out.println("DEBUG - Lista de cartas acumuladas en manoActual: " + manoActual.getIdCartas());

        int suma = calcularSuma(manoJugador);
        if (suma > 21) {
            JOptionPane.showMessageDialog(this, "¡Te pasaste de 21! Perdiste " + apuestaActual + " fichas.");
            apuestaActual = 0;
            lblApuesta.setText("Apuesta: 0");

            // Si el jugador se queda sin dinero, se guarda la partida y se reinicia el juego
            if (dineroJugador <= 0) {
                JOptionPane.showMessageDialog(this, "No tienes más dinero para apostar. Juego terminado.");
                guardarManoDelJugador(); // Guardar los datos antes de salir

                // Redirige al menú principal para forzar la creación de una nueva partida.
                cardLayout.show(panelPrincipal, "MenuJuego");
                btnGuardarPartida.setVisible(false);
            } else {
                reiniciarManos();
                lblDinero.setText("Dinero disponible: " + dineroJugador);
                btnGuardarPartida.setVisible(true);
            }
        }
    }

    // Pedir cartas desde el service
    private int pedirCartaDesdeService() {
        Carta carta = blackjackService.obtenerCartaAleatoria();
        int valor = carta.getValor();

        // Si el valor es mayor a 10 (J, Q, K), devuelve 10
        if (valor > 10) {
            valor = 10;
        }
        return valor;
    }

    // Manejo de la Banca
    private void turnoBanca() {
        // Reinicia la mano de la banca.
        manoBanca = new ArrayList<>();

        // Reparte cartas para la banca mientras la suma sea menor a 17.
        while (calcularSuma(manoBanca) < 17) {
            manoBanca.add(pedirCartaDesdeService());
        }

        lblCartasBanca.setText("Cartas banca: " + manoBanca);
        actualizarSuma(manoBanca, lblSumaBanca);

        // Calcula sumas
        int sumaJugador = calcularSuma(manoJugador);
        int sumaBanca = calcularSuma(manoBanca);

        // Decide resultado 
        if (sumaJugador > 21) {
            JOptionPane.showMessageDialog(this, "¡Te pasaste de 21! Perdiste.");
        } else if (sumaBanca > 21) {
            JOptionPane.showMessageDialog(this, "La banca se pasó de 21. ¡Ganaste!");
            dineroJugador += apuestaActual * 2;
        } else if (sumaBanca >= sumaJugador) {
            JOptionPane.showMessageDialog(this, "La banca ganó. Perdiste la apuesta.");
        } else {
            JOptionPane.showMessageDialog(this, "¡Ganaste!");
            dineroJugador += apuestaActual * 2;
        }

        // Actualiza la interfaz
        lblDinero.setText("Dinero disponible: " + dineroJugador);
        apuestaActual = 0;
        lblApuesta.setText("Apuesta: 0");
        btnGuardarPartida.setVisible(true);

        if (partidaActual != null) {
            guardarManoDelJugador();
        }

        // Reiniciar manos para la siguiente ronda
        reiniciarManos();

        // Si se quedo sin dinero, finalizar partida
        if (dineroJugador <= 0) {
            JOptionPane.showMessageDialog(this, "No tienes más dinero para apostar. Juego terminado.");
            blackjackService.finalizarPartidaAbierta(jugadorActual);
            cardLayout.show(panelPrincipal, "MenuJuego");
            btnGuardarPartida.setVisible(false);
        }
    }
    
    // Calculo de total de puntos
    private int calcularSuma(ArrayList<Integer> mano) {
        int suma = 0;
        int ases = 0;
        for (int carta : mano) {
            suma += carta;
            if (carta == 1) {
                ases++;
            }
        }
        while (ases > 0 && suma + 10 <= 21) {
            suma += 10;
            ases--;
        }
        return suma;
    }

    // En caso de victoria se actualiza el total
    private void actualizarSuma(ArrayList<Integer> mano, JLabel lblSuma) {
        lblSuma.setText("Suma: " + calcularSuma(mano));
    }

    // Reinicio de mano de cartas 
    private void reiniciarManos() {
        manoJugador.clear();
        manoBanca.clear();
        lblCartasJugador.setText("Cartas jugador: []");
        lblSumaJugador.setText("Suma jugador: 0");
        lblCartasBanca.setText("Cartas banca: []");
        lblSumaBanca.setText("Suma banca: 0");
    }

    // Guardar partida
    private void guardarPartida() {
        String nombreUsuario = txtUsuarioLogin.getText();
        Jugador jugador = jugadorService.buscarJugadorPorNombre(nombreUsuario);
        if (jugador == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para guardar la partida.");
            return;
        }

        int dineroAntes = jugador.getDinero();
        int dineroDespues = dineroJugador; // 'dineroJugador' es la variable de la interfaz
        int dineroCambiado = dineroDespues - dineroAntes;

        try {
            List<Integer> manoJugadorValores = new ArrayList<>(manoJugador);
            List<Integer> manoBancaValores = new ArrayList<>(manoBanca);

            // Guarda la partida completa (este metodo lo tienes en tu blackjackService o lo puedes integrar)
            blackjackService.guardarPartidaCompleta(jugador, dineroCambiado,
                    manoJugadorValores, manoBancaValores, true);

            // Asigna el jugador y la partida actuales
            this.jugadorActual = jugador;
            // Recupera la partida abierta para el jugador
            this.partidaActual = partidaService.buscarPartidaAbierta(jugador);
            if (this.partidaActual == null) {
                JOptionPane.showMessageDialog(this, "No se encontró una partida activa.");
                return;
            }

            // Guarda la mano del jugador
            guardarManoDelJugador();

            JOptionPane.showMessageDialog(this, "Partida guardada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la partida:\n" + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Quieres salir al menú o continuar jugando?\n"
                + "Sí: Salir al menú\nNo: Continuar jugando",
                "Continuar o salir",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION) {
            cardLayout.show(panelPrincipal, "MenuJuego");
            btnGuardarPartida.setVisible(false);
            reiniciarManos();
        }
    }

    // Guardar mano del jugador
    private void guardarManoDelJugador() {
        if (jugadorActual == null || partidaActual == null) {
            return;
        }

        System.out.println("DEBUG: jugadorActual = " + jugadorActual);
        System.out.println("DEBUG: partidaActual = " + partidaActual);
        System.out.println("DEBUG: manoJugador = " + manoJugador);

        ManoJugador mano = new ManoJugador();
        mano.setManoJugador(new ArrayList<>(manoJugador));
        mano.setJugador(jugadorActual);
        mano.setPartida(partidaActual);
        mano.setEs_jugador(true);

        // Obtener la carta aleatoria usando el método en BlackjackService.
        Carta cartaAleatoria = blackjackService.obtenerCartaAleatoria();
        if (cartaAleatoria == null) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener una carta aleatoria.");
            return;
        }

        // Agregar el ID de la carta a la lista idCartas
        mano.getIdCartas().add(cartaAleatoria.getIdCarta());

        System.out.println("DEBUG: Objeto ManoJugador a persistir = " + mano);
        manoJugadorService.guardarMano(mano);
    }

    // Finalizar partida
    private void finalizarPartida() {
        if (manoActual.getIdCartas().isEmpty()) {
            System.out.println("ERROR: No hay cartas registradas para guardar.");
            return;
        }

        // Antes de guardar, verificamos la cadena generada por el convertidor
        String cadenaCartas = manoActual.getIdCartas().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println("DEBUG - Cadena generada para id_carta: " + cadenaCartas);

        manoJugadorService.guardarMano(manoActual);
    }

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazBlackjack interfaz = new InterfazBlackjack();
            interfaz.setVisible(true);
        });
    }
}
