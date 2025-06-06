package entidades;

import entidades.Jugador;
import entidades.Partida;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity // Declara que esta clase es una entidad de persistencia
@Table(name = "ManoJugador") // Asocia esta entidad con la tabla "ManoJugador"
public class ManoJugador {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generacion automatica del ID
    @Column(name = "id_mano")
    private int id_mano;

    @ManyToOne(optional = false) // Relacion muchos a uno con Jugador
    @JoinColumn(name = "id_jugador", nullable = false) // Clave foranea
    private Jugador jugador;

    @ManyToOne(optional = false) // Relacion muchos a uno con Partida
    @JoinColumn(name = "id_partida", nullable = false) // Clave foranea
    private Partida partida;

    // Lista de IDs de cartas asociadas a esta mano
    // Usa un convertidor para guardar la lista como una cadena en la BD
    @Column(name = "id_carta", length = 255)
    @Convert(converter = Controller.IntegerListConverter.class)
    private List<Integer> idCartas = new ArrayList<>();

    // Valores individuales de las cartas en la mano
    // Se mapea a una tabla secundaria
    @ElementCollection
    @CollectionTable(name = "mano_jugador_valores", joinColumns = @JoinColumn(name = "id_mano"))
    @Column(name = "valor")
    private List<Integer> manoJugador = new ArrayList<>();

    // Indica si esta mano pertenece al jugador (true) o a la banca (false)
    @Column(name = "es_jugador", nullable = false)
    private boolean es_jugador;

    // Constructor vacio requerido por JPA
    public ManoJugador() {
    }

    // Constructor con parametros
    public ManoJugador(int id_mano, Jugador jugador, Partida partida, boolean es_jugador) {
        this.id_mano = id_mano;
        this.jugador = jugador;
        this.partida = partida;
        this.es_jugador = es_jugador;
    }

    // Getters y Setters

    public int getId_mano() {
        return id_mano;
    }

    public void setId_mano(int id_mano) {
        this.id_mano = id_mano;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public List<Integer> getIdCartas() {
        return idCartas;
    }

    public void setIdCartas(List<Integer> idCartas) {
        this.idCartas = idCartas;
    }

    public List<Integer> getManoJugador() {
        return manoJugador;
    }

    public void setManoJugador(List<Integer> manoJugador) {
        this.manoJugador = manoJugador;
    }

    public boolean isEs_jugador() {
        return es_jugador;
    }

    public void setEs_jugador(boolean es_jugador) {
        this.es_jugador = es_jugador;
    }

    // Metodo para mostrar la informacion de la instancia como texto
    @Override
    public String toString() {
        return "ManoJugador{" + "id_mano=" + id_mano + ", jugador=" + jugador + ", partida=" + partida + ", idCartas=" + idCartas + ", manoJugador=" + manoJugador + ", es_jugador=" + es_jugador + '}';
    }

    // Metodo hashCode para comparar objetos por su contenido
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.id_mano;
        hash = 29 * hash + Objects.hashCode(this.jugador);
        hash = 29 * hash + Objects.hashCode(this.partida);
        hash = 29 * hash + Objects.hashCode(this.idCartas);
        hash = 29 * hash + Objects.hashCode(this.manoJugador);
        hash = 29 * hash + (this.es_jugador ? 1 : 0);
        return hash;
    }

    // Metodo equals para verificar si dos objetos ManoJugador son iguales
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ManoJugador other = (ManoJugador) obj;
        if (this.id_mano != other.id_mano) {
            return false;
        }
        if (this.es_jugador != other.es_jugador) {
            return false;
        }
        if (!Objects.equals(this.jugador, other.jugador)) {
            return false;
        }
        if (!Objects.equals(this.partida, other.partida)) {
            return false;
        }
        if (!Objects.equals(this.idCartas, other.idCartas)) {
            return false;
        }
        return Objects.equals(this.manoJugador, other.manoJugador);
    }

}

