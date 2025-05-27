package entidades;

import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "Cartas")
public class Carta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carta")
    private int id_carta;

    @Column(name = "nombre", nullable = false)
    private String nombre; // Ejemplo: 'As de Corazones'

    @Column(name = "valor", nullable = false)
    private int valor; // Valor de la carta en el juego

    public Carta() {
    }

    public Carta(String nombre, int valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

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

    @Override
    public String toString() {
        return "Carta{" + "idCarta=" + id_carta + ", nombre=" + nombre + ", valor=" + valor + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id_carta;
        hash = 71 * hash + Objects.hashCode(this.nombre);
        hash = 71 * hash + this.valor;
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
