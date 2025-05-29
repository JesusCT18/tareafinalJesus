package Controller;

import entidades.Carta;
import javax.persistence.*;
import java.util.List;

public class CartaRepositorio {

    private EntityManagerFactory emf;
    private EntityManager em;

    public CartaRepositorio() {
        emf = Persistence.createEntityManagerFactory("blackjackPU");
        em = emf.createEntityManager();
    }
    
    // Metodo publico para obtener el EntityManager, requerido para consultas nativas
    public EntityManager getEntityManager() {
        return em;
    }

    // Crear una carta
    public void crearCarta(Carta carta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(carta);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // Leer una carta por su ID (por ejemplo, id_carta)
    public Carta obtenerCartaPorId(int id) {
        return em.find(Carta.class, id);
    }

    // Leer todas las cartas
    public List<Carta> obtenerTodasLasCartas() {
        TypedQuery<Carta> query = em.createQuery("SELECT c FROM Carta c", Carta.class);
        return query.getResultList();
    }

    // Actualizar una carta existente
    public void actualizarCarta(Carta carta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(carta);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // Eliminar una carta por su ID
    public void eliminarCarta(int id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Carta carta = em.find(Carta.class, id);
            if (carta != null) {
                em.remove(carta);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // Cerrar las conexiones al EntityManager y EntityManagerFactory
    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
