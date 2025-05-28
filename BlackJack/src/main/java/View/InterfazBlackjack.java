package View;

import Controller.BlackjackService;
import entidades.Carta;
import entidades.Jugador;
import entidades.ManoJugador;
import entidades.Partida;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InterfazBlackjack extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    // Conexion con la base de datos
    private BlackjackService blackjackService;

    // Componentes login
    private JTextField txtUsuarioLogin;
    private JPasswordField txtPasswordLogin;
    private JCheckBox chkMostrarContraseñaLogin;

    // Componentes registro
    private JTextField txtUsuarioRegistro;
    private JPasswordField txtPasswordRegistro;
    private JCheckBox chkMostrarContraseñaRegistro;

    // Componentes juego
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
        blackjackService = new BlackjackService();
        setTitle("Blackjack");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuracion principal con CardLayout para cambiar pantallas
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        add(panelPrincipal);

        // Creacion de las pantallas
        crearMenuInicio();
        crearPantallaLogin();
        crearPantallaRegistro();
        crearMenuJuego();
        crearMesaJuego();

        // Mostrar pantalla inicial
        cardLayout.show(panelPrincipal, "Inicio");
    }

    // Menu inicial con opciones Iniciar Sesion, Registrarse y Salir
    private void crearMenuInicio() {
        JPanel panelInicio = new JPanel(new GridLayout(3, 1, 10, 10));

        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnRegistrarse = new JButton("Registrarse");
        btnSalirInicio = new JButton("Salir");

        panelInicio.add(btnIniciarSesion);
        panelInicio.add(btnRegistrarse);
        panelInicio.add(btnSalirInicio);

        panelPrincipal.add(panelInicio, "Inicio");

        // Cambiar pantallas segun boton pulsado
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

    private void crearMenuJuego() {
        JPanel menuJuego = new JPanel(new GridLayout(3, 1, 10, 10));

        btnNuevaPartida = new JButton("Nueva Partida");
        btnContinuar = new JButton("Continuar Partida");
        btnSalirMenuJuego = new JButton("Salir");

        menuJuego.add(btnNuevaPartida);
        menuJuego.add(btnContinuar);
        menuJuego.add(btnSalirMenuJuego);

        panelPrincipal.add(menuJuego, "MenuJuego");

        // Nueva partida: iniciar, mostrar mesa y boton guardar visible
        btnNuevaPartida.addActionListener(e -> {
            iniciarNuevaPartida();
            cardLayout.show(panelPrincipal, "MesaJuego");
            JOptionPane.showMessageDialog(this, "Recuerda no pasarte de 21.");
            btnGuardarPartida.setVisible(true);
        });

        // Continuar partida guardada, actualizar interfaz
        btnContinuar.addActionListener(e -> {
            Jugador jugador = blackjackService.buscarJugadorPorNombre(txtUsuarioLogin.getText());
            Partida partidaGuardada = blackjackService.buscarPartidaAbierta(jugador);

            // Si no se encuentra o si la partida tiene saldo 0, se redirige al menú.
            if (partidaGuardada == null || partidaGuardada.getDineroActual() <= 0) {
                JOptionPane.showMessageDialog(this, "Tu dinero actual es 0, crea una nueva partida.");
                cardLayout.show(panelPrincipal, "MenuJuego");
                return;
            }

            // Si la partida guardada tiene dinero > 0, se carga normalmente.
            dineroJugador = partidaGuardada.getDineroActual();
            apuestaActual = 0;

            List<ManoJugador> manos = blackjackService.cargarManosDePartida(partidaGuardada.getIdPartida());
            manoJugador = new ArrayList<>();
            manoBanca = new ArrayList<>();
            for (ManoJugador mano : manos) {
                int valorCarta = mano.getCarta().getValor() > 10 ? 10 : mano.getCarta().getValor();
                if (mano.isEs_jugador()) {
                    manoJugador.add(valorCarta);
                } else {
                    manoBanca.add(valorCarta);
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

    // Pantalla principal del juego: muestra cartas, suma, dinero y botones de accion
    private void crearMesaJuego() {
        JPanel mesaPanel = new JPanel(new BorderLayout(20, 20)); // espacio entre componentes

        // Panel izquierdo con info (cartas, suma, dinero, apuesta)
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // padding

        lblDinero = new JLabel("Dinero disponible: " + dineroJugador);
        lblApuesta = new JLabel("Apuesta: " + apuestaActual);
        lblCartasJugador = new JLabel("Cartas jugador: []");
        lblSumaJugador = new JLabel("Suma jugador: 0");
        lblCartasBanca = new JLabel("Cartas banca: []");
        lblSumaBanca = new JLabel("Suma banca: 0");

        // Agregar etiquetas con espaciadores
        panelInfo.add(lblDinero);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblApuesta);
        panelInfo.add(Box.createVerticalStrut(30));
        panelInfo.add(lblCartasJugador);
        panelInfo.add(lblSumaJugador);
        panelInfo.add(Box.createVerticalStrut(20));
        panelInfo.add(lblCartasBanca);
        panelInfo.add(lblSumaBanca);

        // Panel derecho con botones (alineados verticalmente y centrados)
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // padding

        btnApostar = new JButton("Elegir apuesta");
        btnPedirCarta = new JButton("Pedir carta");
        btnPasar = new JButton("Pasar");
        btnGuardarPartida = new JButton("Guardar partida");

        Dimension btnSize = new Dimension(150, 40);
        JButton[] botones = {btnApostar, btnPedirCarta, btnPasar, btnGuardarPartida};
        for (JButton btn : botones) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelBotones.add(btn);
            panelBotones.add(Box.createVerticalStrut(15));
        }

        btnGuardarPartida.setVisible(false);

        // Añadir paneles al panel principal
        mesaPanel.add(panelInfo, BorderLayout.CENTER);
        mesaPanel.add(panelBotones, BorderLayout.EAST);

        // Cargar el GIF desde resources (debe estar en src/main/resources/)
        URL gifURL = getClass().getResource("/fondo.gif");
        if (gifURL == null) {
            System.err.println("❌No se encontró el GIF en /fondo.gif");
        } else {
            ImageIcon gifIcon = new ImageIcon(gifURL);
            JLabel gifLabel = new JLabel(gifIcon);
            gifLabel.setPreferredSize(new Dimension(800, 300)); // Ajusta tamaño según espacio en blanco
            gifLabel.setOpaque(false); // Transparente por si acaso
            mesaPanel.add(gifLabel, BorderLayout.SOUTH);
        }

        panelPrincipal.add(mesaPanel, "MesaJuego");

        // Listeners para los botones
        btnApostar.addActionListener(e -> elegirApuesta());
        btnPedirCarta.addActionListener(e -> {
            if (apuestaActual <= 0) {
                JOptionPane.showMessageDialog(this, "Debes elegir una apuesta antes de jugar.");
                return;
            }
            pedirCartaJugador();
        });
        btnPasar.addActionListener(e -> {
            if (apuestaActual <= 0) {
                JOptionPane.showMessageDialog(this, "Debes elegir una apuesta antes de jugar.");
                return;
            }
            turnoBanca();
        });
        btnGuardarPartida.addActionListener(e -> guardarPartida()); // solo visible tras iniciar o continuar partida
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

    // Inicia reparto de cartas (2 jugador, 1 banca), muestra cartas y suma jugador
    private void pedirCartaJugador() {
        int carta = pedirCartaDesdeService();
        manoJugador.add(carta);
        lblCartasJugador.setText("Cartas jugador: " + manoJugador);
        actualizarSuma(manoJugador, lblSumaJugador);

        int suma = calcularSuma(manoJugador);
        if (suma > 21) {
            JOptionPane.showMessageDialog(this, "¡Te pasaste de 21! Perdiste " + apuestaActual + " fichas.");
            apuestaActual = 0;
            lblApuesta.setText("Apuesta: 0");

            // Si se pasó de 21, se verifica si el jugador se quedó sin dinero.
            if (dineroJugador <= 0) {
                JOptionPane.showMessageDialog(this, "No tienes más dinero para apostar. Juego terminado.");

                // Recupera el jugador y guarda la partida automáticamente con saldo 0.
                Jugador jugador = blackjackService.buscarJugadorPorNombre(txtUsuarioLogin.getText());
                blackjackService.finalizarPartidaAbierta(jugador);

                // Redirige al menú principal para forzar la creación de una nueva partida.
                cardLayout.show(panelPrincipal, "MenuJuego");
                btnGuardarPartida.setVisible(false);
            } else {
                // Si aún tiene dinero, reinicia las manos para continuar el juego.
                reiniciarManos();
                lblDinero.setText("Dinero disponible: " + dineroJugador);
                btnGuardarPartida.setVisible(true);
            }
        }
    }

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

        int sumaJugador = calcularSuma(manoJugador);
        int sumaBanca = calcularSuma(manoBanca);

        // Evaluación del resultado de la ronda.
        if (sumaBanca > 21) {
            JOptionPane.showMessageDialog(this, "La banca se pasó de 21. ¡Ganaste!");
            dineroJugador += apuestaActual * 2;
        } else if (sumaBanca >= sumaJugador) {
            JOptionPane.showMessageDialog(this, "La banca ganó. Perdiste la apuesta.");
        } else {
            JOptionPane.showMessageDialog(this, "¡Ganaste!");
            dineroJugador += apuestaActual * 2;
        }

        lblDinero.setText("Dinero disponible: " + dineroJugador);
        apuestaActual = 0;
        lblApuesta.setText("Apuesta: 0");
        btnGuardarPartida.setVisible(true);

        reiniciarManos();

        lblDinero.setText("Dinero disponible: " + dineroJugador);
        btnGuardarPartida.setVisible(true);

        // Si el jugador se queda sin dinero, guardamos la partida automaticamente con saldo 0.
        if (dineroJugador <= 0) {
            dineroJugador = 0;
            lblDinero.setText("Dinero disponible: " + dineroJugador);
            JOptionPane.showMessageDialog(this, "No tienes más dinero para apostar. Juego terminado.");

            Jugador jugador = blackjackService.buscarJugadorPorNombre(txtUsuarioLogin.getText());
            // Guarda automaticamente la partida con saldo 0 y la finaliza.
            blackjackService.finalizarPartidaAbierta(jugador);

            // Redirige al menu principal para forzar la creacion de una nueva partida.
            cardLayout.show(panelPrincipal, "MenuJuego");
            btnGuardarPartida.setVisible(false);
            return;
        }
    }

    private String calcularResultado() {
        int sumaJugador = calcularSuma(manoJugador);
        int sumaBanca = calcularSuma(manoBanca);

        if (sumaJugador > 21) {
            return "Perdió"; // jugador se pasó
        } else if (sumaBanca > 21) {
            return "Ganó"; // banca se pasó
        } else if (sumaJugador > sumaBanca) {
            return "Ganó";
        } else if (sumaJugador == sumaBanca) {
            return "Empate";
        } else {
            return "Perdió";
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
        Jugador jugador = blackjackService.buscarJugadorPorNombre(nombreUsuario);
        if (jugador == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para guardar la partida.");
            return;
        }

        int dineroAntes = jugador.getDinero();
        int dineroDespues = dineroJugador;
        int dineroCambiado = dineroDespues - dineroAntes;

        try {
            List<Integer> manoJugadorValores = new ArrayList<>(manoJugador);
            List<Integer> manoBancaValores = new ArrayList<>(manoBanca);

            // Aquí pasamos true para Nueva Partida, de forma que se cree una nueva instancia.
            blackjackService.guardarPartidaCompleta(jugador, dineroCambiado,
                    manoJugadorValores, manoBancaValores, true);

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

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazBlackjack interfaz = new InterfazBlackjack();
            interfaz.setVisible(true);
        });
    }
}
