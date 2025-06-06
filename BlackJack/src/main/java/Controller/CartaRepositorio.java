package Controller;

import entidades.Carta;
import javax.persistence.*;
import java.util.List;

public class CartaRepositorio {

    private EntityManagerFactory emf;

    public CartaRepositorio() {
        emf = Persistence.createEntityManagerFactory("blackjackPU");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Crear una carta
    public void crearCarta(Carta carta) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(carta);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Obtener una carta por ID
    public Carta obtenerCartaPorId(int id) {
        EntityManager em = getEntityManager();
        Carta carta = em.find(Carta.class, id);
        em.close();
        return carta;
    }

    // Obtener todas las cartas
    public List<Carta> obtenerTodasLasCartas() {
        EntityManager em = getEntityManager();
        TypedQuery<Carta> query = em.createQuery("SELECT c FROM Carta c", Carta.class);
        List<Carta> cartas = query.getResultList();
        em.close();
        return cartas;
    }

    // Actualizar una carta existente
    public void actualizarCarta(Carta carta) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(carta);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Eliminar una carta por su ID
    public void eliminarCarta(int id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Carta carta = em.find(Carta.class, id);
            if (carta != null) em.remove(carta);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

