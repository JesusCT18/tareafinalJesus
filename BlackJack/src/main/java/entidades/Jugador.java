package entidades;

import entidades.Partida;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "Jugadores") // Define el nombre de la tabla en la base de datos
public class Jugador {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El valor se genera automaticamente
    @Column(name = "id_jugador")
    private Integer idJugador;

    @Column(nullable = false) // Campo obligatorio
    private String nombre;

    @Column(nullable = false) // Campo obligatorio
    private String contraseña; // Clave del jugador

    @Column(nullable = false) // Campo obligatorio
    private int dinero; // Dinero actual del jugador

    // Relacion uno a muchos con la entidad Partida
    // Un jugador puede tener muchas partidas
    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Partida> partidas;

    // Constructor vacio requerido por JPA
    public Jugador() {
        this.dinero = 100; // Valor por defecto inicial
        this.partidas = new ArrayList<>(); // Inicializa la lista para evitar errores nulos
    }

    // Constructor con parametros (sin ID porque se genera automaticamente)
    public Jugador(String nombre, String contraseña, int dinero) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.dinero = dinero;
    }

    // Getters y Setters

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    // Representacion en texto del objeto Jugador
    @Override
    public String toString() {
        return "Jugador{" + "idJugador=" + idJugador + ", nombre=" + nombre + ", contraseña=" + contraseña + ", dinero=" + dinero + ", partidas=" + partidas + '}';
    }

    // Metodo hashCode para identificar objetos unicos
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.idJugador;
        hash = 23 * hash + Objects.hashCode(this.nombre);
        hash = 23 * hash + Objects.hashCode(this.contraseña);
        hash = 23 * hash + this.dinero;
        hash = 23 * hash + Objects.hashCode(this.partidas);
        return hash;
    }

    // Metodo equals para comparar dos objetos Jugador
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
        final Jugador other = (Jugador) obj;
        if (this.idJugador != other.idJugador) {
            return false;
        }
        if (this.dinero != other.dinero) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.contraseña, other.contraseña)) {
            return false;
        }
        return Objects.equals(this.partidas, other.partidas);
    }
}

