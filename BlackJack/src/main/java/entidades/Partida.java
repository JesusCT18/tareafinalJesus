package entidades;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity // Declara que esta clase es una entidad de la base de datos
@Table(name = "Partidas") // Asocia esta entidad con la tabla "Partidas"
public class Partida {

    @Id // Identificador unico (clave primaria)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se genera automaticamente
    @Column(name = "id_partida")
    private Integer idPartida;

    @ManyToOne(fetch = FetchType.LAZY) // Muchas partidas pueden pertenecer a un mismo jugador
    @JoinColumn(name = "id_jugador", nullable = false) // Clave foranea hacia Jugador
    private Jugador jugador;

    @Column(name = "dinero_actual", nullable = false) // Dinero disponible en esta partida
    private int dineroActual;

    @Column(name = "fecha", nullable = false) // Fecha y hora en que se jugo la partida
    private LocalDateTime fecha;

    // Estado de la partida: "En curso", "Finalizada", etc.
    @Column(name = "estado", nullable = false)
    private String estado = "En curso"; // Valor por defecto

    // Constructor vacio requerido por JPA
    public Partida() {
    }

    // Constructor con parametros
    public Partida(int idPartida, Jugador jugador, int dineroActual, LocalDateTime fecha) {
        this.idPartida = idPartida;
        this.jugador = jugador;
        this.dineroActual = dineroActual;
        this.fecha = fecha;
    }

    // Getters y setters

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

    public int getDineroActual() {
        return dineroActual;
    }

    public void setDineroActual(int dineroActual) {
        this.dineroActual = dineroActual;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Representacion en texto del objeto Partida
    @Override
    public String toString() {
        return "Partida{" + "idPartida=" + idPartida + ", jugador=" + jugador + ", dineroActual=" + dineroActual + ", fecha=" + fecha + ", estado=" + estado + '}';
    }

    // Codigo hash para comparar objetos eficientemente
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.idPartida;
        hash = 89 * hash + Objects.hashCode(this.jugador);
        hash = 89 * hash + this.dineroActual;
        hash = 89 * hash + Objects.hashCode(this.fecha);
        hash = 89 * hash + Objects.hashCode(this.estado);
        return hash;
    }

    // Metodo para comparar si dos partidas son iguales
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

