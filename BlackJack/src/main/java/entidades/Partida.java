package entidades;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPartida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String resultado;

    @Column(nullable = false)
    private int montoApostado;

    @Column(nullable = false)
    private int dineroCambiado;

    @Column(nullable = false)
    private String estado = "ABIERTA";  

    @Column(nullable = false)
    private int ultimaRonda = 0;   
    
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManoJugador> manosJugador;

    public Partida() {
    }

    public Partida(int idPartida, Jugador jugador, LocalDateTime fecha, String resultado, int montoApostado, int dineroCambiado, List<ManoJugador> manosJugador) {
        this.idPartida = idPartida;
        this.jugador = jugador;
        this.fecha = fecha;
        this.resultado = resultado;
        this.montoApostado = montoApostado;
        this.dineroCambiado = dineroCambiado;
        this.manosJugador = manosJugador;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public int getMontoApostado() {
        return montoApostado;
    }

    public void setMontoApostado(int montoApostado) {
        this.montoApostado = montoApostado;
    }

    public int getDineroCambiado() {
        return dineroCambiado;
    }

    public void setDineroCambiado(int dineroCambiado) {
        this.dineroCambiado = dineroCambiado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getUltimaRonda() {
        return ultimaRonda;
    }

    public void setUltimaRonda(int ultimaRonda) {
        this.ultimaRonda = ultimaRonda;
    }

    public List<ManoJugador> getManosJugador() {
        return manosJugador;
    }

    public void setManosJugador(List<ManoJugador> manosJugador) {
        this.manosJugador = manosJugador;
    }

    @Override
    public String toString() {
        return "Partida{" + "idPartida=" + idPartida + ", jugador=" + jugador + ", fecha=" + fecha + ", resultado=" + resultado + ", montoApostado=" + montoApostado + ", dineroCambiado=" + dineroCambiado + ", estado=" + estado + ", ultimaRonda=" + ultimaRonda + ", manosJugador=" + manosJugador + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.idPartida;
        hash = 53 * hash + Objects.hashCode(this.jugador);
        hash = 53 * hash + Objects.hashCode(this.fecha);
        hash = 53 * hash + Objects.hashCode(this.resultado);
        hash = 53 * hash + this.montoApostado;
        hash = 53 * hash + this.dineroCambiado;
        hash = 53 * hash + Objects.hashCode(this.estado);
        hash = 53 * hash + this.ultimaRonda;
        hash = 53 * hash + Objects.hashCode(this.manosJugador);
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
        final Partida other = (Partida) obj;
        if (this.idPartida != other.idPartida) {
            return false;
        }
        if (this.montoApostado != other.montoApostado) {
            return false;
        }
        if (this.dineroCambiado != other.dineroCambiado) {
            return false;
        }
        if (this.ultimaRonda != other.ultimaRonda) {
            return false;
        }
        if (!Objects.equals(this.resultado, other.resultado)) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        if (!Objects.equals(this.jugador, other.jugador)) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        return Objects.equals(this.manosJugador, other.manosJugador);
    }
}
