package entidades;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partida") // usa el nombre exacto de la columna en la BD
    private int idPartida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total_apostado", nullable = false)
    private int totalApostado;

    @Column(name = "total_ganado", nullable = false)
    private int totalGanado = 0;

    @Column(name = "total_perdido", nullable = false)
    private int totalPerdido = 0;

    @Column(name = "dinero_actual", nullable = false)
    private int dineroActual = 0;

    @Column(name = "estado", nullable = false)
    private String estado = "En curso";

    public Partida() {
    }

    public Partida(int idPartida, Jugador jugador, LocalDateTime fecha, int totalApostado) {
        this.idPartida = idPartida;
        this.jugador = jugador;
        this.fecha = fecha;
        this.totalApostado = totalApostado;
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

    public int getTotalApostado() {
        return totalApostado;
    }

    public void setTotalApostado(int totalApostado) {
        this.totalApostado = totalApostado;
    }

    public int getTotalGanado() {
        return totalGanado;
    }

    public void setTotalGanado(int totalGanado) {
        this.totalGanado = totalGanado;
    }

    public int getTotalPerdido() {
        return totalPerdido;
    }

    public void setTotalPerdido(int totalPerdido) {
        this.totalPerdido = totalPerdido;
    }

    public int getDineroActual() {
        return dineroActual;
    }

    public void setDineroActual(int dineroActual) {
        this.dineroActual = dineroActual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Partida{" + "idPartida=" + idPartida + ", jugador=" + jugador + ", fecha=" + fecha + ", totalApostado=" + totalApostado + ", totalGanado=" + totalGanado + ", totalPerdido=" + totalPerdido + ", dineroActual=" + dineroActual + ", estado=" + estado + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.idPartida;
        hash = 11 * hash + Objects.hashCode(this.jugador);
        hash = 11 * hash + Objects.hashCode(this.fecha);
        hash = 11 * hash + this.totalApostado;
        hash = 11 * hash + this.totalGanado;
        hash = 11 * hash + this.totalPerdido;
        hash = 11 * hash + this.dineroActual;
        hash = 11 * hash + Objects.hashCode(this.estado);
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
        if (this.totalApostado != other.totalApostado) {
            return false;
        }
        if (this.totalGanado != other.totalGanado) {
            return false;
        }
        if (this.totalPerdido != other.totalPerdido) {
            return false;
        }
        if (this.dineroActual != other.dineroActual) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        if (!Objects.equals(this.jugador, other.jugador)) {
            return false;
        }
        return Objects.equals(this.fecha, other.fecha);
    }

}
