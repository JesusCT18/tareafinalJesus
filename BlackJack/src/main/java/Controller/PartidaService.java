package Controller;

import entidades.Jugador;
import entidades.Partida;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

public class PartidaService {

    private EntityManagerFactory emf;

    public PartidaService() {
        emf = Persistence.createEntityManagerFactory("blackjackPU");
    }
    
    
    // Busca una partida abierta (en curso) para el jugador especificado.
    public Partida buscarPartidaAbierta(Jugador jugador) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Partida> query = em.createQuery(
                    "SELECT p FROM Partida p WHERE p.jugador = :jugador AND p.estado = 'En curso'", Partida.class);
            query.setParameter("jugador", jugador);
            List<Partida> resultados = query.getResultList();
            if (!resultados.isEmpty()) {
                return resultados.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }
    
    // Guarda una nueva partida.
    public Partida guardarPartida(Partida partida) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(partida);
            tx.commit();
            return partida;
        } catch (Exception e) {
            if (tx.isActive()){
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Puedes agregar otros metodos relacionados con Partida (actualizar, eliminar, etc.).
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

