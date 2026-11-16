import app.domain.*;
import app.domain.enums.ClasificacionEnum;
import app.persistence.repository.CineRepositoryDB;
import app.persistence.repository.SalaRepositoryDB;
import app.persistence.repository.AsientoRepositoryDB;
import app.persistence.repository.TicketRepositoryDB;
import app.persistence.repository.ClienteRepositoryDB;
import app.persistence.repository.PeliculaRepositoryDB;
import app.persistence.repository.ComboComidaRepositoryDB;
import app.persistence.dbconnection.DataBaseConnection;
import app.persistence.mapper.ClienteMapper;
import app.persistence.mapper.PeliculaMapper;
import app.persistence.mapper.ComboComidaMapper;
import app.persistence.mapper.AsientoMapper;
import app.persistence.mapper.SalaMapper;
import app.persistence.mapper.CineMapper;
import app.persistence.mapper.TicketMapper;
import app.service.*;
import app.service.helpers.DatosRelacionados;

/** Ejecutar exclusivamente contra una base vacia y desechable. */
public class PruebaJdbc {
    private static void check(boolean ok, String msg) { if (!ok) throw new AssertionError(msg); }
    public static void main(String[] args) {
        var connection = DataBaseConnection.getInstance().getConnection();
        var clientes = new ClienteRepositoryDB(connection, new ClienteMapper());
        var peliculas = new PeliculaRepositoryDB(connection, new PeliculaMapper());
        var combos = new ComboComidaRepositoryDB(connection, new ComboComidaMapper());
        var asientoMapper = new AsientoMapper();
        var cines = new CineRepositoryDB(connection, new CineMapper(connection, new PeliculaMapper()));
        var salas = new SalaRepositoryDB(connection, new SalaMapper(connection, asientoMapper));
        var asientos = new AsientoRepositoryDB(connection, asientoMapper);
        var tickets = new TicketRepositoryDB(connection, new TicketMapper(clientes, peliculas, salas, combos));
        var datos = new DatosRelacionados(clientes,peliculas,combos,cines,salas,tickets);
        var cs = new ClienteServiceImpl(clientes,datos); var ps = new PeliculaServiceImpl(peliculas,datos);
        var bs = new ComboComidaServiceImpl(combos,datos); var cine = new CineServiceImpl(cines,datos);
        var sala = new SalaServiceImpl(salas,datos);
        var as = new AsientoServiceImpl(asientos,datos); var ts = new TicketServiceImpl(tickets,datos);
        if (args.length > 0 && args[0].equals("crear")) {
            cs.createCliente("101","Laura"); ps.createPelicula(1,"Pelicula",90,ClasificacionEnum.TODO_PUBLICO);
            bs.createComboComida(1,"Combo",28000); cine.createCine(1,"Cine"); cine.agregarPelicula(1,1); cine.agregarPelicula(1,1);
            sala.createSala(1,2,"2D"); sala.createSala(2,2,"3D");
            as.createAsiento(1,"VIP"); as.updateAsiento("1:VIP","VIP2");
            ts.createTicket(1,"101",1,1,"VIP2",1,18000);
            check(cs.selectClienteById("101").getPuntosCesde()==46,"puntos creados");
            System.out.println("JDBC: datos creados");
        } else {
            check(cine.selectCineById(1).getPeliculas().size()==1,"catalogo persistido");
            check(sala.selectSalaById(1).getAsientos().size()==3,"asientos persistidos");
            check(sala.selectSalaById(2).getTipo().equals("3D"),"sala 3D persistida");
            check(tickets.selectTicketById(1).getAsiento().getCodigo().equals("VIP2"),"relacion ticket");
            check(cs.selectClienteById("101").getPuntosCesde()==46,"puntos persistidos");
            ts.updateTicket(1,"101",1,2,"S1",0,15000);
            check(cs.selectClienteById("101").getPuntosCesde()==15,"puntos actualizados");
            check(ts.deleteTicket(1),"borrar ticket"); check(cs.selectClienteById("101").getPuntosCesde()==0,"puntos tras borrar");
            cine.retirarPelicula(1,1); check(cine.selectCineById(1).getPeliculas().isEmpty(),"retirar pelicula");
            check(as.deleteAsiento("1:VIP2"),"borrar asiento");
            System.out.println("JDBC: relaciones, puntos y cambios verificados tras reinicio");
        }
    }
}
