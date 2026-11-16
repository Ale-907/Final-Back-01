package app.configuration;

import app.persistence.repository.*;
import app.persistence.dbconnection.DataBaseConnection;
import app.persistence.mapper.ClienteMapper;
import app.persistence.mapper.PeliculaMapper;
import app.persistence.mapper.ComboComidaMapper;
import app.persistence.mapper.AsientoMapper;
import app.persistence.mapper.SalaMapper;
import app.persistence.mapper.CineMapper;
import app.persistence.mapper.TicketMapper;
import java.sql.Connection;
import app.service.*;
import app.service.inputport.*;
import app.service.outputport.*;
import app.service.helpers.DatosRelacionados;
import app.view.*;
import app.userinterface.UserInterface;

public class Config {
    public static UserInterface createUserInterface() {
        Connection connection = DataBaseConnection.getInstance().getConnection();
        ClienteRepositoryPort clienteRepository = new ClienteRepositoryDB(connection, new ClienteMapper());
        PeliculaRepositoryPort peliculaRepository = new PeliculaRepositoryDB(connection, new PeliculaMapper());
        ComboComidaRepositoryPort comboComidaRepository = new ComboComidaRepositoryDB(connection, new ComboComidaMapper());
        AsientoMapper asientoMapper = new AsientoMapper();
        CineRepositoryPort cineRepository = new CineRepositoryDB(connection, new CineMapper(connection, new PeliculaMapper()));
        SalaRepositoryPort salaRepository = new SalaRepositoryDB(connection, new SalaMapper(connection, asientoMapper));
        AsientoRepositoryPort asientoRepository = new AsientoRepositoryDB(connection, asientoMapper);
        TicketRepositoryPort ticketRepository = new TicketRepositoryDB(connection, new TicketMapper(clienteRepository, peliculaRepository, salaRepository, comboComidaRepository));
        DatosRelacionados datos = new DatosRelacionados(clienteRepository, peliculaRepository,
                comboComidaRepository, cineRepository, salaRepository, ticketRepository);
        ClienteServiceInterface clienteService = new ClienteServiceImpl(clienteRepository, datos);
        PeliculaServiceInterface peliculaService = new PeliculaServiceImpl(peliculaRepository, datos);
        ComboComidaServiceInterface comboComidaService = new ComboComidaServiceImpl(comboComidaRepository, datos);
        CineServiceInterface cineService = new CineServiceImpl(cineRepository, datos);
        SalaServiceInterface salaService = new SalaServiceImpl(salaRepository, datos);
        AsientoServiceInterface asientoService = new AsientoServiceImpl(asientoRepository, datos);
        TicketServiceInterface ticketService = new TicketServiceImpl(ticketRepository, datos);

        ClienteView clienteView = new ClienteView(clienteService);
        PeliculaView peliculaView = new PeliculaView(peliculaService);
        ComboComidaView comboComidaView = new ComboComidaView(comboComidaService);
        CineView cineView = new CineView(cineService);
        SalaView salaView = new SalaView(salaService);
        AsientoView asientoView = new AsientoView(asientoService);
        TicketView ticketView = new TicketView(ticketService);
        return new UserInterface(clienteView, peliculaView, comboComidaView, cineView, salaView, asientoView, ticketView);
    }
}
