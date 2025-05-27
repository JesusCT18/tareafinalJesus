package entidades;

import entidades.Jugador;
import entidades.Partida;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "ManoJugador")
public class ManoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mano")
    private int id_mano;
    
    @ManyToOne
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador; 

    @ManyToOne
    @JoinColumn(name = "id_partida", nullable = false)
    private Partida partida; 

    @ManyToOne
    @JoinColumn(name = "id_carta", nullable = false)
    private Carta carta;  
    
    @Column(name = "es_jugador", nullable = false)
    private boolean es_jugador;

    public ManoJugador() {
    }

    public ManoJugador(int id_mano, Jugador jugador, Partida partida, Carta carta, boolean es_jugador) {
        this.id_mano = id_mano;
        this.jugador = jugador;
        this.partida = partida;
        this.carta = carta;
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

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public boolean isEs_jugador() {
        return es_jugador;
    }

    public void setEs_jugador(boolean es_jugador) {
        this.es_jugador = es_jugador;
    }

    @Override
    public String toString() {
        return "ManoJugador{" + "id_mano=" + id_mano + ", jugador=" + jugador + ", partida=" + partida + ", carta=" + carta + ", es_jugador=" + es_jugador + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id_mano;
        hash = 29 * hash + Objects.hashCode(this.jugador);
        hash = 29 * hash + Objects.hashCode(this.partida);
        hash = 29 * hash + Objects.hashCode(this.carta);
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
        return Objects.equals(this.carta, other.carta);
    } 
}
