package Controller;

import entidades.Carta;
import entidades.ManoJugador;
import entidades.Jugador;
import entidades.Partida;
import entidades.Ronda;
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
            String resultado,
            int dineroCambiado,
            List<Integer> manoJugadorValores,
            List<Integer> manoBancaValores
    ) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1) Crear y persistir Partida
            Partida partida = new Partida();
            partida.setJugador(jugador);
            partida.setFecha(LocalDateTime.now());
            partida.setResultado(resultado);
            partida.setMontoApostado(montoApostado);
            partida.setDineroCambiado(dineroCambiado);
            partida.setEstado("ABIERTA");
            partida.setUltimaRonda(1);  // arrancamos en la ronda 1
            em.persist(partida);
            em.flush(); // para asegurarnos que partida tiene id

            // 2) Crear y persistir Ronda #1
            Ronda ronda = new Ronda();
            ronda.setPartida(partida);
            ronda.setNumeroRonda(1);
            em.persist(ronda);
            em.flush();

            // 3) Para cada valor de carta, buscar una Carta en la BD y persistir ManoJugador
            //    usando es_jugador = true para manoJugador, false para banca
            String jpqlCarta = "SELECT c FROM Carta c WHERE c.valor = :v";
            for (int val : manoJugadorValores) {
                Carta c = em.createQuery(jpqlCarta, Carta.class)
                        .setParameter("v", Math.min(val, 10))
                        .setMaxResults(1)
                        .getSingleResult();
                ManoJugador mj = new ManoJugador();
                mj.setPartida(partida);
                mj.setRonda(ronda);
                mj.setJugador(jugador);
                mj.setCarta(c);
                mj.setEs_jugador(true);
                em.persist(mj);
            }
            for (int val : manoBancaValores) {
                Carta c = em.createQuery(jpqlCarta, Carta.class)
                        .setParameter("v", Math.min(val, 10))
                        .setMaxResults(1)
                        .getSingleResult();
                ManoJugador mj = new ManoJugador();
                mj.setPartida(partida);
                mj.setRonda(ronda);
                mj.setJugador(jugador);
                mj.setCarta(c);
                mj.setEs_jugador(false);
                em.persist(mj);
            }

            // 4) Actualizar dinero del jugador
            jugador.setDinero(jugador.getDinero() + dineroCambiado);
            em.merge(jugador);

            // 5) Actualizar última ronda en Partida (ya fue la ronda 1)
            partida.setUltimaRonda(1);
            em.merge(partida);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // Cargar partida completa con mano y cartas, dado el id de la partida
    public Partida cargarPartidaCompleta(int idPartida) {
        Partida partida = em.find(Partida.class, idPartida);
        if (partida != null) {
            // Cargar todas las manos asociadas a la partida
            TypedQuery<ManoJugador> queryManos = em.createQuery(
                    "SELECT m FROM ManoJugador m WHERE m.partida.idPartida = :idPartida", ManoJugador.class);
            queryManos.setParameter("idPartida", idPartida);
            List<ManoJugador> manos = queryManos.getResultList();

            // Asignar las manos a la partida para que tenga todas las manos con cartas
            partida.setManosJugador(manos);  // O como se llame tu lista en la entidad Partida
        }
        return partida;
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
