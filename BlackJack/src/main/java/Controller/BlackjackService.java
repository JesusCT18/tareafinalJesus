package Controller;

import Controller.CartaRepositorio;
import entidades.Carta;
import entidades.ManoJugador;
import entidades.Jugador;
import entidades.Partida;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

public class BlackjackService {

    private EntityManagerFactory emf;
    private EntityManager em;

    public BlackjackService() {
        emf = Persistence.createEntityManagerFactory("blackjackPU");
        em = emf.createEntityManager();
    }

    // Campo para la instancia de CartaService 
    private CartaService cartaService = new CartaService();

    // Metodo publico que delega en CartaService
    public Carta obtenerCartaAleatoria() {
        return cartaService.obtenerCartaAleatoria();
    }

    // Metodos para Jugador
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

    public Jugador buscarJugadorPorId(int id) {
        return em.find(Jugador.class, id);
    }

    public Jugador buscarJugadorPorNombre(String nombre) {
        TypedQuery<Jugador> query = em.createQuery("SELECT j FROM Jugador j WHERE j.nombre = :nombre", Jugador.class);
        query.setParameter("nombre", nombre);
        List<Jugador> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public int obtenerDineroActualJugador(int idJugador) {
        Jugador jugador = buscarJugadorPorId(idJugador);
        return jugador != null ? jugador.getDinero() : 0;
    }

    // Metodos para Partida y ManoJugador 
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

    public void guardarPartidaCompleta(Jugador jugador, int dineroCambiado,
            List<Integer> manoJugadorValores, List<Integer> manoBancaValores, boolean crearNueva) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Recuperar el Jugador "gestionado" desde la BD
            Jugador managedJugador = em.find(Jugador.class, jugador.getIdJugador());
            if (managedJugador == null || managedJugador.getIdJugador() <= 0) {
                throw new RuntimeException("Error: Jugador no encontrado o sin ID válido.");
            }
            System.out.println("DEBUG: managedJugador id = " + managedJugador.getIdJugador());

            Partida partida = null;
            if (crearNueva) {
                // Cerrar partida abierta, si la hay
                Partida abierta = buscarPartidaAbierta(managedJugador);
                if (abierta != null) {
                    abierta.setEstado("Finalizada");
                    em.merge(abierta);
                }
                // Crear nueva partida
                partida = new Partida();
                partida.setJugador(managedJugador);
                managedJugador.getPartidas().add(partida);
                partida.setFecha(LocalDateTime.now());
                partida.setEstado("En curso");
                partida.setDineroActual(managedJugador.getDinero());

                em.persist(partida);
                em.flush();  // Forzar la generación del ID en Partida
                System.out.println("DEBUG: Nueva Partida persistida, flush() realizado. ID asignado: " + partida.getIdPartida());
                // Recuperar la partida con su ID generado
                partida = em.find(Partida.class, partida.getIdPartida());
            } else {
                partida = buscarPartidaAbierta(managedJugador);
                if (partida == null) {
                    partida = new Partida();
                    partida.setJugador(managedJugador);
                    managedJugador.getPartidas().add(partida);
                    partida.setFecha(LocalDateTime.now());
                    partida.setEstado("En curso");
                    partida.setDineroActual(managedJugador.getDinero());

                    em.persist(partida);
                    em.flush();
                    System.out.println("DEBUG: Nueva Partida (else) persistida, flush() realizado. ID asignado: " + partida.getIdPartida());
                    partida = em.find(Partida.class, partida.getIdPartida());
                }
            }

            // Verificar que Partida y Jugador tengan un ID válido (mayor a 0)
            if (partida == null || partida.getIdPartida() <= 0) {
                throw new RuntimeException("Error: La partida no tiene un ID válido.");
            }
            if (managedJugador == null || managedJugador.getIdJugador() <= 0) {
                throw new RuntimeException("Error: El jugador no tiene un ID válido.");
            }

            int dineroFinal = managedJugador.getDinero() + dineroCambiado;
            partida.setDineroActual(dineroFinal);
            managedJugador.setDinero(dineroFinal);

            em.merge(managedJugador);
            em.flush(); // Sincronizar cambios en Jugador
            managedJugador = em.find(Jugador.class, managedJugador.getIdJugador());
            System.out.println("DEBUG: Después de actualizar, managedJugador id = " + managedJugador.getIdJugador()
                    + ", Partida id = " + partida.getIdPartida());
            System.out.println("DEBUG: manoJugadorValores = " + manoJugadorValores);
            System.out.println("DEBUG: manoBancaValores = " + manoBancaValores);

            // Persistir ManoJugador para el jugador SOLO si la lista no está vacía
            if (manoJugadorValores != null && !manoJugadorValores.isEmpty()) {
                ManoJugador mano = new ManoJugador();
                mano.setPartida(partida);
                mano.setJugador(managedJugador);
                mano.setEs_jugador(true);
                mano.setManoJugador(manoJugadorValores);
                System.out.println("DEBUG: Persistiendo ManoJugador (jugador) con: " + manoJugadorValores);
                em.persist(mano);
            } else {
                System.out.println("DEBUG: No se persiste ManoJugador del jugador porque la lista está vacía.");
            }

            // Persistir ManoJugador para la banca SOLO si la lista no está vacía
            if (manoBancaValores != null && !manoBancaValores.isEmpty()) {
                ManoJugador manoBanca = new ManoJugador();
                manoBanca.setPartida(partida);
                manoBanca.setJugador(managedJugador);
                manoBanca.setEs_jugador(false);
                manoBanca.setManoJugador(manoBancaValores);
                System.out.println("DEBUG: Persistiendo ManoJugador (banca) con: " + manoBancaValores);
                em.persist(manoBanca);
            } else {
                System.out.println("DEBUG: No se persiste ManoJugador de la banca porque la lista está vacía.");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la partida completa: " + e.getMessage(), e);
        }
    }

    public Partida cargarPartidaCompleta(int idPartida) {
        return em.find(Partida.class, idPartida);
    }

    public List<ManoJugador> cargarManosDePartida(int idPartida) {
        TypedQuery<ManoJugador> queryManos = em.createQuery(
                "SELECT m FROM ManoJugador m WHERE m.partida.idPartida = :idPartida", ManoJugador.class);
        queryManos.setParameter("idPartida", idPartida);
        return queryManos.getResultList();
    }

    public List<Partida> listarPartidasPorJugador(int idJugador) {
        TypedQuery<Partida> query = em.createQuery(
                "SELECT p FROM Partida p WHERE p.jugador.idJugador = :idJugador ORDER BY p.fecha DESC", Partida.class);
        query.setParameter("idJugador", idJugador);
        return query.getResultList();
    }

    public void finalizarPartidaAbierta(Jugador jugador) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Partida partidaAbierta = buscarPartidaAbierta(jugador);
            if (partidaAbierta != null) {
                partidaAbierta.setEstado("Finalizada");
                partidaAbierta.setDineroActual(0);
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

    // Metodos CRUD para Carta 
    public class CartaService {

        private CartaRepositorio cartaRepositorio;

        public CartaService() {
            this.cartaRepositorio = new CartaRepositorio();
        }

        public void guardarCarta(Carta carta) {
            cartaRepositorio.crearCarta(carta);
        }

        public Carta obtenerCartaPorId(int id) {
            return cartaRepositorio.obtenerCartaPorId(id);
        }

        public List<Carta> obtenerTodasLasCartas() {
            return cartaRepositorio.obtenerTodasLasCartas();
        }

        public void actualizarCarta(Carta carta) {
            cartaRepositorio.actualizarCarta(carta);
        }

        public void eliminarCarta(int id) {
            cartaRepositorio.eliminarCarta(id);
        }

        public void cerrar() {
            cartaRepositorio.cerrar();
        }

        // Metodos para Operaciones con Cartas Aleatorias 
        public Carta obtenerCartaAleatoria() {
            EntityManager em = cartaRepositorio.getEntityManager();
            Query query = em.createNativeQuery("SELECT * FROM Cartas ORDER BY RAND() LIMIT 1", Carta.class);
            return (Carta) query.getSingleResult();
        }

        public List<Carta> obtenerCartasAleatorias(int cantidad) {
            EntityManager em = cartaRepositorio.getEntityManager();
            Query query = em.createNativeQuery("SELECT * FROM Cartas ORDER BY RAND() LIMIT :cantidad", Carta.class);
            query.setParameter("cantidad", cantidad);
            return query.getResultList();
        }
    }

    public static class ManoJugadorService {

        private ManoJugadorRepositorio manoJugadorRepo;

        public ManoJugadorService() {
            manoJugadorRepo = new ManoJugadorRepositorio();
        }

        public void guardarMano(ManoJugador mano) {
            manoJugadorRepo.guardarMano(mano);
        }

        public List<ManoJugador> cargarManosDePartida(int idPartida) {
            return manoJugadorRepo.cargarManosDePartida(idPartida);
        }

        public void actualizarMano(ManoJugador mano) {
            manoJugadorRepo.actualizarMano(mano);
        }

        public void eliminarMano(int id) {
            manoJugadorRepo.eliminarMano(id);
        }

        public void cerrar() {
            manoJugadorRepo.cerrar();
        }
    }

    // Cierre de conexiones 
    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
