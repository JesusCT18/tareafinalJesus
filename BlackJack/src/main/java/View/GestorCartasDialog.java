package View;

import Controller.BlackjackService.CartaService;
import entidades.Carta;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestorCartasDialog extends JDialog {

    // Servicio que maneja la logica de acceso a datos de cartas
    private CartaService cartaService;
    private DefaultListModel<String> modeloLista; // Modelo para la lista de cartas
    private JList<String> listaCartas; // Componente visual de la lista

    public GestorCartasDialog(Frame parent) {
        // Configura el dialogo modal con titulo
        super(parent, "Gestion de Cartas", true);
        cartaService = new CartaService();

        setSize(400, 500); // Tamano de la ventana
        setLayout(new BorderLayout()); // Layout principal
        setLocationRelativeTo(parent); // Centrado respecto al padre

        // Inicializa la lista de cartas y la muestra
        modeloLista = new DefaultListModel<>();
        listaCartas = new JList<>(modeloLista);
        actualizarListaCartas(); // Carga las cartas actuales

        // Panel con scroll para la lista
        JScrollPane scrollPane = new JScrollPane(listaCartas);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

        // Botones de accion
        JButton btnAgregar = new JButton("Agregar Carta");
        JButton btnActualizar = new JButton("Actualizar Carta");
        JButton btnEliminar = new JButton("Eliminar Carta");
        JButton btnCerrar = new JButton("Cerrar");

        // Se agregan los botones al panel inferior
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones de los botones
        btnAgregar.addActionListener(e -> agregarCarta());
        btnActualizar.addActionListener(e -> actualizarCarta());
        btnEliminar.addActionListener(e -> eliminarCarta());
        btnCerrar.addActionListener(e -> dispose()); // Cierra el dialogo
    }

    // Metodo para recargar la lista de cartas desde la base de datos
    private void actualizarListaCartas() {
        modeloLista.clear();
        List<Carta> cartas = cartaService.obtenerTodasLasCartas();
        for (Carta carta : cartas) {
            modeloLista.addElement(carta.getIdCarta() + " - " + carta.getNombre() + " (Valor: " + carta.getValor() + ")");
        }
    }

    // Metodo para agregar una nueva carta
    private void agregarCarta() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre de la carta:");
        String valorStr = JOptionPane.showInputDialog(this, "Ingrese el valor de la carta:");
        try {
            int valor = Integer.parseInt(valorStr);
            cartaService.guardarCarta(new Carta(nombre, valor)); // Guarda la carta
            actualizarListaCartas(); // Refresca la lista
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor invalido.");
        }
    }

    // Metodo para actualizar el valor de una carta existente
    private void actualizarCarta() {
        String idCartaStr = JOptionPane.showInputDialog(this, "Ingrese el ID de la carta a actualizar:");

        try {
            int idCarta = Integer.parseInt(idCartaStr);
            Carta carta = cartaService.obtenerCartaPorId(idCarta);

            if (carta == null) {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna carta con ese ID.");
                return;
            }

            // Preguntar al usuario si quiere modificar el nombre
            String nuevoNombre = JOptionPane.showInputDialog(this, "Nombre actual: " + carta.getNombre()
                    + "\nSi quieres cambiarlo, escribe el nuevo nombre.\nSi quieres dejarlo igual, deja el campo vacío y presiona Aceptar.");

            // Si el usuario deja el campo en blanco, mantiene el nombre actual
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                nuevoNombre = carta.getNombre();
            }

            // Preguntar por el nuevo valor de la carta
            String nuevoValorStr = JOptionPane.showInputDialog(this, "Valor actual: " + carta.getValor()
                    + "\nIngrese el nuevo valor.\nSi quieres dejarlo igual, deja el campo vacío y presiona Aceptar.");

            int nuevoValor;
            if (nuevoValorStr == null || nuevoValorStr.trim().isEmpty()) {
                nuevoValor = carta.getValor(); // Mantiene el valor actual si no introduce nada
            } else {
                nuevoValor = Integer.parseInt(nuevoValorStr);
            }

            // Se actualiza la carta con los nuevos valores
            carta.setNombre(nuevoNombre);
            carta.setValor(nuevoValor);
            cartaService.actualizarCarta(idCarta, nuevoNombre, nuevoValor);

            JOptionPane.showMessageDialog(this, "Carta actualizada correctamente.");
            actualizarListaCartas();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos. Asegúrate de ingresar un número válido para el ID y el valor.");
        }
    }

    // Metodo para eliminar una carta por ID
    private void eliminarCarta() {
        String idCartaStr = JOptionPane.showInputDialog(this, "Ingrese el ID de la carta a eliminar:");
        try {
            int idCarta = Integer.parseInt(idCartaStr);
            cartaService.eliminarCarta(idCarta); // Elimina la carta
            actualizarListaCartas(); // Refresca la lista
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID invalido.");
        }
    }
}
