package entidades;

import entidades.Jugador;
import entidades.Partida;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "ManoJugador")
public class ManoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mano")
    private int id_mano;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_partida", nullable = false)
    private Partida partida;

    // Eliminamos el anterior "Carta carta;" y definimos una colección:
    @Column(name = "id_carta", length = 255)
    @Convert(converter = Controller.IntegerListConverter.class)
    private List<Integer> idCartas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "mano_jugador_valores", joinColumns = @JoinColumn(name = "id_mano"))
    @Column(name = "valor")
    private List<Integer> manoJugador = new ArrayList<>();

    @Column(name = "es_jugador", nullable = false)
    private boolean es_jugador;

    public ManoJugador() {
    }

    public ManoJugador(int id_mano, Jugador jugador, Partida partida, boolean es_jugador) {
        this.id_mano = id_mano;
        this.jugador = jugador;
        this.partida = partida;
        this.es_jugador = es_jugador;
    }

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

    @Override
    public String toString() {
        return "ManoJugador{" + "id_mano=" + id_mano + ", jugador=" + jugador + ", partida=" + partida + ", idCartas=" + idCartas + ", manoJugador=" + manoJugador + ", es_jugador=" + es_jugador + '}';
    }

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
