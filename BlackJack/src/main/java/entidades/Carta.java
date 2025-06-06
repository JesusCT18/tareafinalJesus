package entidades;

import java.util.Objects;
import javax.persistence.*;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "Cartas") // Nombre de la tabla en la base de datos
public class Carta {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generacion automatica del ID
    @Column(name = "id_carta") // Mapeo del campo con la columna 'id_carta'
    private int id_carta;

    @Column(name = "nombre", nullable = false) // Campo 'nombre' no puede ser nulo
    private String nombre; // Ejemplo: 'As de Corazones'

    @Column(name = "valor", nullable = false) // Campo 'valor' no puede ser nulo
    private int valor; // Valor de la carta en el juego

    // Constructor por defecto (necesario para JPA)
    public Carta() {
    }

    // Constructor con parametros
    public Carta(String nombre, int valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    // Getters y setters

    public int getIdCarta() {
        return id_carta;
    }

    public void setIdCarta(int idCarta) {
        this.id_carta = idCarta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    // Representacion en texto de la carta
    @Override
    public String toString() {
        return "Carta{" + "idCarta=" + id_carta + ", nombre=" + nombre + ", valor=" + valor + '}';
    }

    // Metodo hashCode para colecciones
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id_carta;
        hash = 71 * hash + Objects.hashCode(this.nombre);
        hash = 71 * hash + this.valor;
        return hash;
    }

    // Metodo equals para comparar dos cartas
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
        final Carta other = (Carta) obj;
        if (this.id_carta != other.id_carta) {
            return false;
        }
        if (this.valor != other.valor) {
            return false;
        }
        return Objects.equals(this.nombre, other.nombre);
    }
}
