package Controller;

import entidades.ManoJugador;
import java.util.List;
import javax.persistence.*;

public class ManoJugadorRepositorio {

    private EntityManagerFactory emf;
    private EntityManager em;

    public ManoJugadorRepositorio() {
        emf = Persistence.createEntityManagerFactory("blackjackPU");
        em = emf.createEntityManager();
    }

    // Guardar una mano de jugador
    public void guardarMano(ManoJugador mano) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(mano);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // Obtener las manos de una partida
    public List<ManoJugador> cargarManosDePartida(int idPartida) {
        TypedQuery<ManoJugador> query = em.createQuery(
                "SELECT m FROM ManoJugador m WHERE m.partida.idPartida = :idPartida", ManoJugador.class
        );
        query.setParameter("idPartida", idPartida);
        return query.getResultList();
    }

    // Actualizar una mano (si lo necesitas)
    public void actualizarMano(ManoJugador mano) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(mano);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // Eliminar una mano (por su id, por ejemplo)
    public void eliminarMano(int id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ManoJugador mano = em.find(ManoJugador.class, id);
            if (mano != null) {
                em.remove(mano);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // Cerrar conexiones
    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

