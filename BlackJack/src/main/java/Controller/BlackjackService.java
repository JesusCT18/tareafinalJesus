package Controller;

import entidades.Carta;
import entidades.ManoJugador;
import entidades.Jugador;
import entidades.Partida;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class BlackjackService {

    private EntityManagerFactory emf;
    private EntityManager em;

    public BlackjackService() {
        emf = Persistence.createEntityManagerFactory("blackjackPU");
        em = emf.createEntityManager();
    }

    // Guardar jugador
    public void guardarJugador(Jugador jugador) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(jugador);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // Buscar jugador por id
    public Jugador buscarJugadorPorId(int id) {
        return em.find(Jugador.class, id);
    }

    // Buscar jugador por nombre
    public Jugador buscarJugadorPorNombre(String nombre) {
        TypedQuery<Jugador> query = em.createQuery("SELECT j FROM Jugador j WHERE j.nombre = :nombre", Jugador.class);
        query.setParameter("nombre", nombre);
        List<Jugador> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    // Obtener dinero actual del jugador (desde entidad Jugador, no de Partida)
    public int obtenerDineroActualJugador(int idJugador) {
        Jugador jugador = buscarJugadorPorId(idJugador);
        return jugador != null ? jugador.getDinero() : 0;
    }

    public Partida buscarPartidaAbierta(Jugador jugador) {
        TypedQuery<Partida> query = em.createQuery(
                "SELECT p FROM Partida p WHERE p.jugador = :jugador AND p.estado = 'ABIERTA'",
                Partida.class
        );
        query.setParameter("jugador", jugador);
        List<Partida> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    // Guardar partida completa con cartas y mano del jugador
    public void guardarPartidaCompleta(
            Jugador jugador,
            int montoApostado,
            int dineroCambiado,
            List<Integer> manoJugadorValores,
            List<Integer> manoBancaValores
    ) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            jugador = em.find(Jugador.class, jugador.getIdJugador());

            // Buscar partida abierta
            Partida partida = buscarPartidaAbierta(jugador);

            if (partida == null) {
                // Si no hay partida abierta, crear una nueva
                partida = new Partida();
                partida.setJugador(jugador);
                jugador.getPartidas().add(partida);
                partida.setFecha(LocalDateTime.now());
                partida.setEstado("En curso");
                partida.setTotalApostado(0);
                partida.setTotalGanado(0);
                partida.setTotalPerdido(0);
                partida.setDineroActual(jugador.getDinero());
                em.persist(partida);
                em.flush(); // para generar idPartida
            }

            // Acumular valores en la partida existente
            partida.setTotalApostado(partida.getTotalApostado() + montoApostado);

            if (dineroCambiado >= 0) {
                partida.setTotalGanado(partida.getTotalGanado() + dineroCambiado);
            } else {
                partida.setTotalPerdido(partida.getTotalPerdido() + (-dineroCambiado));
            }

            int dineroFinal = jugador.getDinero() + dineroCambiado;
            partida.setDineroActual(dineroFinal);

            // Si quieres puedes cambiar el estado si ya se finalizó la partida, aquí lo dejo así:
            // partida.setEstado("Finalizada"); // solo si es el cierre de la partida
            // Guardar cartas (igual que antes)
            String jpqlCarta = "SELECT c FROM Carta c WHERE c.valor = :v";

            if (manoJugadorValores != null && !manoJugadorValores.isEmpty()) {
                for (int val : manoJugadorValores) {
                    Carta carta = em.createQuery(jpqlCarta, Carta.class)
                            .setParameter("v", Math.min(val, 10))
                            .setMaxResults(1)
                            .getSingleResult();

                    ManoJugador manoJugador = new ManoJugador();
                    manoJugador.setPartida(partida);
                    manoJugador.setJugador(jugador);
                    manoJugador.setCarta(carta);
                    manoJugador.setEs_jugador(true);
                    em.persist(manoJugador);
                }
            }

            if (manoBancaValores != null && !manoBancaValores.isEmpty()) {
                for (int val : manoBancaValores) {
                    Carta carta = em.createQuery(jpqlCarta, Carta.class)
                            .setParameter("v", Math.min(val, 10))
                            .setMaxResults(1)
                            .getSingleResult();

                    ManoJugador manoBanca = new ManoJugador();
                    manoBanca.setPartida(partida);
                    manoBanca.setJugador(jugador);
                    manoBanca.setCarta(carta);
                    manoBanca.setEs_jugador(false);
                    em.persist(manoBanca);
                }
            }

            jugador.setDinero(dineroFinal);
            em.merge(jugador);
            em.merge(partida);  // actualizar partida

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Partida cargarPartidaCompleta(int idPartida) {
        Partida partida = em.find(Partida.class, idPartida);
        // No asignamos listas de manos a la partida (porque no existe el atributo)
        return partida;
    }

    public List<ManoJugador> cargarManosDePartida(int idPartida) {
        TypedQuery<ManoJugador> queryManos = em.createQuery(
                "SELECT m FROM ManoJugador m WHERE m.partida.idPartida = :idPartida", ManoJugador.class);
        queryManos.setParameter("idPartida", idPartida);
        return queryManos.getResultList();
    }

    // Listar partidas de un jugador
    public List<Partida> listarPartidasPorJugador(int idJugador) {
        TypedQuery<Partida> query = em.createQuery(
                "SELECT p FROM Partida p WHERE p.jugador.idJugador = :idJugador ORDER BY p.fecha DESC", Partida.class);
        query.setParameter("idJugador", idJugador);
        return query.getResultList();
    }

    // Guardar carta
    public void guardarCarta(Carta carta) {
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

    // Guardar mano
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

    // Obtener una carta aleatoria 
    public Carta obtenerCartaAleatoria() {
        Query query = em.createNativeQuery("SELECT * FROM Cartas ORDER BY RAND() LIMIT 1", Carta.class);
        return (Carta) query.getSingleResult();
    }

    // Sacar cartas aleatorias
    public List<Carta> obtenerCartasAleatorias(int cantidad) {
        Query query = em.createNativeQuery("SELECT * FROM Cartas ORDER BY RAND() LIMIT :cantidad", Carta.class);
        query.setParameter("cantidad", cantidad);
        return query.getResultList();
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
