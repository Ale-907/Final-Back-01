import app.domain.*;
import app.domain.enums.ClasificacionEnum;
import app.persistence.repository.*;
import app.service.*;
import app.service.inputport.*;
import app.service.outputport.*;
import app.service.helpers.DatosRelacionados;

public class PruebaCapas {
    private static int comprobaciones = 0;
    private static void revisar(boolean condicion, String mensaje) {
        comprobaciones++;
        if (!condicion) { throw new AssertionError(mensaje); }
    }
    private static void rechazar(Runnable accion, String mensaje) {
        boolean rechazo = false;
        try { accion.run(); } catch (IllegalArgumentException e) { rechazo = true; }
        revisar(rechazo, mensaje);
    }
    public static void main(String[] args) {
        ClienteRepositoryPort clienteRepository = new ClienteRepositoryImpl();
        PeliculaRepositoryPort peliculaRepository = new PeliculaRepositoryImpl();
        ComboComidaRepositoryPort comboComidaRepository = new ComboComidaRepositoryImpl();
        CineRepositoryPort cineRepository = new CineRepositoryImpl();
        SalaRepositoryPort salaRepository = new SalaRepositoryImpl();
        TicketRepositoryPort ticketRepository = new TicketRepositoryImpl();
        DatosRelacionados datos = new DatosRelacionados(clienteRepository, peliculaRepository,
                comboComidaRepository, cineRepository, salaRepository, ticketRepository);
        AsientoRepositoryPort asientoRepository = new AsientoRepositoryImpl(datos);
        ClienteServiceInterface cliente = new ClienteServiceImpl(clienteRepository, datos);
        PeliculaServiceInterface pelicula = new PeliculaServiceImpl(peliculaRepository, datos);
        ComboComidaServiceInterface comboComida = new ComboComidaServiceImpl(comboComidaRepository, datos);
        CineServiceInterface cine = new CineServiceImpl(cineRepository, datos);
        SalaServiceInterface salas = new SalaServiceImpl(salaRepository, datos);
        TicketServiceInterface ticket = new TicketServiceImpl(ticketRepository, datos);
        AsientoServiceInterface asiento = new AsientoServiceImpl(asientoRepository, datos);

        // CRUD de cliente y reglas basicas.
        Cliente objetoCliente = new Cliente();
        objetoCliente.setCedula("99"); objetoCliente.setNombre("Bean"); objetoCliente.setPuntosCesde(2);
        revisar(objetoCliente.getNombre().equals("Bean") && objetoCliente.getPuntosCesde() == 2, "Constructor vacio y accesores");
        objetoCliente = cliente.createCliente("101", "Laura");
        revisar(cliente.selectAll().size() == 1, "Crear y listar cliente");
        revisar(cliente.selectClienteById("101") == objetoCliente, "Buscar cliente");
        cliente.updateCliente("101", "Laura Actualizada");
        revisar(objetoCliente.getNombre().equals("Laura Actualizada"), "Actualizar sin romper referencias");
        rechazar(() -> cliente.createCliente("101", "Otra"), "Cedula repetida");
        rechazar(() -> cliente.createCliente("", "Otra"), "Cedula vacia");
        rechazar(() -> cliente.updateCliente("999", "Otra"), "Actualizar registro inexistente");
        revisar(!cliente.deleteCliente("999"), "Eliminar inexistente");
        cliente.selectAll().clear();
        revisar(cliente.selectAll().size() == 1, "Listado no permite vaciar almacenamiento");

        Pelicula objetoPelicula = new Pelicula();
        objetoPelicula.setId(1); objetoPelicula.setTitulo("Temporal"); objetoPelicula.setDuracionMinutos(90);
        objetoPelicula.setClasificacion(ClasificacionEnum.TODO_PUBLICO);
        revisar(objetoPelicula.getTitulo().equals("Temporal"), "Pelicula POJO");
        objetoPelicula = pelicula.createPelicula(1, "Ejemplo", 100, ClasificacionEnum.TODO_PUBLICO);
        revisar(pelicula.selectPeliculaById(1) == objetoPelicula, "Buscar pelicula");
        pelicula.updatePelicula(1, "Ejemplo actualizado", 120, ClasificacionEnum.MAYORES_12);
        revisar(objetoPelicula.getDuracionMinutos() == 120, "Actualizar pelicula");
        rechazar(() -> pelicula.createPelicula(2, "Invalida", 0, ClasificacionEnum.TODO_PUBLICO), "Duracion positiva");
        rechazar(() -> pelicula.createPelicula(2, "Invalida", 100, null), "Enum obligatorio");
        revisar(pelicula.selectAll().size() == 1, "Listar peliculas");

        comboComida.createComboComida(1, "Pareja", 28000);
        comboComida.updateComboComida(1, "Pareja nueva", 28000);
        revisar(comboComida.selectComboComidaById(1).getNombreCombo().equals("Pareja nueva"), "Actualizar combo");
        revisar(comboComida.selectAll().size() == 1, "Listar combos");
        rechazar(() -> comboComida.createComboComida(2, "Mal", -1), "Precio positivo");
        rechazar(() -> comboComida.createComboComida(2, "Mal", Double.NaN), "Rechazar NaN");
        rechazar(() -> comboComida.createComboComida(2, "Mal", Double.POSITIVE_INFINITY), "Rechazar infinito");

        cine.createCine(1, "Cesde");
        cine.agregarPelicula(1, 1); cine.agregarPelicula(1, 1);
        revisar(cine.selectCineById(1).getPeliculas().size() == 1, "Catalogo no repite pelicula");
        revisar(cine.selectCineById(1).getPeliculas().get(0) == objetoPelicula, "Agregacion de pelicula externa");
        cine.updateCine(1, "Cesde actualizado");
        revisar(cine.selectCineById(1).getPeliculas().size() == 1, "Actualizar cine conserva catalogo");
        revisar(cine.selectAll().size() == 1, "Listar cines");
        rechazar(() -> pelicula.deletePelicula(1), "No eliminar pelicula del catalogo");
        rechazar(() -> cine.agregarPelicula(1, 99), "No asociar pelicula inexistente");

        salas.createSala(1, 12, "2D");
        salas.createSala(2, 8, "3D");
        Sala sala = datos.buscarSala(1);
        revisar(sala.getAsientos().size() == 12, "Composicion al crear sala");
        revisar(datos.buscarSala(2).getAsientos().size() == 8, "Capacidad sala 3D");
        revisar(sala.obtenerTipoPantalla().contains("2D"), "Polimorfismo 2D");
        revisar(datos.buscarSala(2).obtenerTipoPantalla().contains("3D"), "Polimorfismo 3D");
        revisar(sala.getAsientos().get(0) != datos.buscarSala(2).getAsientos().get(0), "Cada sala crea asientos propios");
        sala.getAsientos().clear();
        revisar(sala.getAsientos().size() == 12, "No borrar composicion desde listado");
        rechazar(() -> salas.createSala(1, 8, "3D"), "Numero de sala unico entre tipos");
        rechazar(() -> salas.createSala(3, 0, "2D"), "Capacidad positiva");
        rechazar(() -> salas.createSala(3, 501, "2D"), "Limite de capacidad");
        rechazar(() -> salas.createSala(3, 10, "4D"), "Tipo de sala invalido");
        salas.updateSala(1, 12, "2D");
        salas.updateSala(2, 8, "3D");
        revisar(salas.selectSalaById(1).getTipo().equals("2D"), "Actualizar 2D");
        revisar(salas.selectSalaById(2).getTipo().equals("3D"), "Actualizar 3D");
        revisar(salas.selectAll().size() == 2, "Listar ambos tipos");

        Asiento nuevo = asiento.createAsiento(1, "VIP");
        revisar(nuevo == datos.buscarAsiento(1, "VIP"), "Asiento nuevo pertenece a sala");
        revisar(datos.buscarSala(1).getCapacidadAsientos() == 13, "Agregar asiento actualiza capacidad");
        asiento.updateAsiento("1:VIP", "VIP2");
        revisar(asiento.selectAsientoById("1:VIP") == null && asiento.selectAsientoById("1:VIP2") != null, "Renombrar asiento");
        rechazar(() -> asiento.createAsiento(1, "S1"), "No repetir codigo dentro de sala");
        rechazar(() -> asiento.createAsiento(99, "S1"), "Asiento exige sala existente");
        revisar(asiento.selectAll().size() == 21, "Listar asientos de ambas salas");
        revisar(asiento.deleteAsiento("1:VIP2"), "Eliminar asiento");
        revisar(datos.buscarSala(1).getCapacidadAsientos() == 12, "Eliminar actualiza capacidad");


        Ticket compra = ticket.createTicket(1, "101", 1, 1, "S1", 1, 18000);
        revisar(compra.calcularTotal() == 46000, "Total con combo");
        revisar(compra.getCliente() == objetoCliente && compra.getPelicula() == objetoPelicula, "Referencias reales del ticket");
        revisar(objetoCliente.getPuntosCesde() == 46, "Puntos al crear ticket");
        revisar(compra.getAsiento() == datos.buscarAsiento(1, "S1"), "Asiento de la sala elegida");
        rechazar(() -> ticket.createTicket(2, "101", 1, 1, "S1", 0, 18000), "No duplicar asiento vendido");
        rechazar(() -> ticket.createTicket(2, "999", 1, 1, "S2", 0, 18000), "Cliente inexistente");
        rechazar(() -> ticket.createTicket(2, "101", 99, 1, "S2", 0, 18000), "Pelicula inexistente");
        rechazar(() -> ticket.createTicket(2, "101", 1, 99, "S2", 0, 18000), "Sala inexistente");
        rechazar(() -> ticket.createTicket(2, "101", 1, 1, "S99", 0, 18000), "Asiento inexistente");
        rechazar(() -> ticket.createTicket(2, "101", 1, 1, "S2", 99, 18000), "Combo inexistente");
        rechazar(() -> ticket.createTicket(2, "101", 1, 1, "S2", -1, 18000), "Combo negativo");
        revisar(ticket.selectAll().size() == 1, "Creaciones invalidas no guardan tickets");
        rechazar(() -> cliente.deleteCliente("101"), "Cliente usado no se elimina");
        rechazar(() -> pelicula.deletePelicula(1), "Pelicula usada no se elimina");
        rechazar(() -> comboComida.deleteComboComida(1), "Combo usado no se elimina");
        rechazar(() -> comboComida.updateComboComida(1, "Cambio", 1), "Precio de compra conserva valor");
        rechazar(() -> salas.deleteSala(1), "Sala usada no se elimina");
        rechazar(() -> salas.updateSala(1, 2, "2D"), "No regenerar asientos usados");
        rechazar(() -> asiento.deleteAsiento("1:S1"), "Asiento usado no se elimina");
        rechazar(() -> asiento.updateAsiento("1:S1", "X"), "Asiento usado no se renombra");
        revisar(objetoCliente.getPuntosCesde() == 46, "Fallos no cambian puntos");
        ticket.updateTicket(1, "101", 1, 2, "S2", 0, 15000);
        revisar(ticket.selectTicketById(1).calcularTotal() == 15000, "Actualizar ticket sin combo");
        revisar(objetoCliente.getPuntosCesde() == 15, "Recalcular puntos al actualizar");
        ticket.createTicket(2, "101", 1, 1, "S1", 0, 18000);
        revisar(objetoCliente.getPuntosCesde() == 33, "Acumular puntos de dos compras");
        revisar(ticket.deleteTicket(1), "Eliminar ticket");
        revisar(objetoCliente.getPuntosCesde() == 18, "Puntos de compras que permanecen");
        revisar(ticket.deleteTicket(2), "Eliminar segundo ticket");
        revisar(objetoCliente.getPuntosCesde() == 0, "Puntos vuelven a cero sin compras");
        revisar(datos.buscarAsiento(1, "S1") != null, "Eliminar ticket conserva sala y asiento");
        revisar(cliente.deleteCliente("101"), "Eliminar cliente ya sin tickets");
        cine.retirarPelicula(1, 1);
        revisar(objetoPelicula.getTitulo().equals("Ejemplo actualizado"), "Retirar de catalogo no destruye pelicula");
        revisar(pelicula.deletePelicula(1), "Eliminar pelicula sin referencias");
        revisar(comboComida.deleteComboComida(1), "Eliminar combo sin tickets");
        revisar(cine.deleteCine(1), "Eliminar cine");
        revisar(salas.deleteSala(1), "Eliminar sala 2D");
        revisar(asiento.selectAsientoById("1:S1") == null, "Composicion: asientos dejan de estar gestionados al retirar sala");
        revisar(salas.deleteSala(2), "Eliminar sala 3D");
        revisar(asiento.selectAll().isEmpty(), "No quedan asientos huerfanos en repositorio");
        System.out.println("OK: " + comprobaciones + " comprobaciones del proyecto por capas.");
    }
}
