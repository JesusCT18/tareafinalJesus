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
        try {
            return em.createQuery(
                    "SELECT p FROM Partida p WHERE p.jugador = :jugador AND p.estado = 'En curso'",
                    Partida.class)
                    .setParameter("jugador", jugador)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Guardar partida completa con cartas y mano del jugador
    public void guardarPartidaCompleta(
            Jugador jugador,
            int dineroCambiado,
            List<Integer> manoJugadorValores,
            List<Integer> manoBancaValores,
            boolean crearNueva
    ) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            jugador = em.find(Jugador.class, jugador.getIdJugador());

            Partida partida;
            if (crearNueva) {
                // Si hay una partida abierta, se finaliza (o se marca de forma que no se encuentre al buscar una partida abierta)
                Partida abierta = buscarPartidaAbierta(jugador);
                if (abierta != null) {
                    abierta.setEstado("Finalizada");
                    em.merge(abierta);
                }
                // Se crea una nueva partida
                partida = new Partida();
                partida.setJugador(jugador);
                jugador.getPartidas().add(partida);
                partida.setFecha(LocalDateTime.now());
                partida.setEstado("En curso");
                partida.setDineroActual(jugador.getDinero());
                em.persist(partida);
                em.flush();  // Así se genera una nueva id
            } else {
                // Se busca la partida abierta; si no existe, se crea
                partida = buscarPartidaAbierta(jugador);
                if (partida == null) {
                    partida = new Partida();
                    partida.setJugador(jugador);
                    jugador.getPartidas().add(partida);
                    partida.setFecha(LocalDateTime.now());
                    partida.setEstado("En curso");
                    partida.setDineroActual(jugador.getDinero());
                    em.persist(partida);
                    em.flush();
                }
            }

            // Actualizamos el dinero actual (por ejemplo, con dineroCambiado)
            int dineroFinal = jugador.getDinero() + dineroCambiado;
            partida.setDineroActual(dineroFinal);
            jugador.setDinero(dineroFinal);

            em.merge(jugador);
            em.merge(partida);

            // Guardar cartas de la mano del jugador
            String jpqlCarta = "SELECT c FROM Carta c WHERE c.valor = :v";
            if (manoJugadorValores != null && !manoJugadorValores.isEmpty()) {
                for (int val : manoJugadorValores) {
                    List<Carta> cartasEncontradas = em.createQuery(jpqlCarta, Carta.class)
                            .setParameter("v", Math.min(val, 10))
                            .setMaxResults(1)
                            .getResultList();
                    if (cartasEncontradas.isEmpty()) {
                        throw new RuntimeException("No se encontró una carta con valor: " + Math.min(val, 10));
                    }
                    Carta carta = cartasEncontradas.get(0);
                    // Aquí se podría imprimir "Carta encontrada: " + carta para depuración.

                    ManoJugador mano = new ManoJugador();
                    mano.setPartida(partida);
                    mano.setJugador(jugador);
                    mano.setCarta(carta);
                    mano.setEs_jugador(true);
                    em.persist(mano);
                }
            }

            // Para la mano de la banca
            if (manoBancaValores != null && !manoBancaValores.isEmpty()) {
                for (int val : manoBancaValores) {
                    List<Carta> cartasEncontradas = em.createQuery(jpqlCarta, Carta.class)
                            .setParameter("v", Math.min(val, 10))
                            .setMaxResults(1)
                            .getResultList();
                    if (cartasEncontradas.isEmpty()) {
                        throw new RuntimeException("No se encontró una carta con valor: " + Math.min(val, 10));
                    }
                    Carta cartaBanca = cartasEncontradas.get(0);
                    System.out.println("Carta encontrada (banca): " + cartaBanca);

                    ManoJugador manoBanca = new ManoJugador();
                    manoBanca.setPartida(partida);
                    manoBanca.setJugador(jugador);
                    manoBanca.setCarta(cartaBanca);
                    manoBanca.setEs_jugador(false);  // Asegúrate de que el setter esté en camelCase y sea el correcto
                    em.persist(manoBanca);
                }
            }

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

    public void finalizarPartidaAbierta(Jugador jugador) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Partida partidaAbierta = buscarPartidaAbierta(jugador);
            if (partidaAbierta != null) {
                partidaAbierta.setEstado("Finalizada");
                partidaAbierta.setDineroActual(0);  // Se forza a guardar el saldo en 0.
                em.merge(partidaAbierta);
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
