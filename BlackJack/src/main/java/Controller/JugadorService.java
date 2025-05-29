package Controller;

import entidades.Jugador;
import java.util.List;
import javax.persistence.*;

public class JugadorService {

    private EntityManagerFactory emf;

    public JugadorService() {
        emf = Persistence.createEntityManagerFactory("blackjackPU");
    }

    
    // Obtiene un jugador a partir de su ID. 
    public Jugador obtenerJugadorPorId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Jugador.class, id);
        } finally {
            em.close();
        }
    }

    
    // Busca un jugador por nombre. Se asume que el nombre es unico o se retorna el primero.
    public Jugador buscarJugadorPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Jugador> query = em.createQuery(
                    "SELECT j FROM Jugador j WHERE j.nombre = :nombre", Jugador.class);
            query.setParameter("nombre", nombre);
            List<Jugador> resultados = query.getResultList();
            if (!resultados.isEmpty()) {
                return resultados.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    // Puedes agregar otros metodos segun lo necesites (por ejemplo, guardar o actualizar un Jugador)
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
