package app.persistence.repository;
import app.domain.Ticket;
import app.persistence.mapper.RowMapper;
import app.service.outputport.TicketRepositoryPort;
import java.sql.*;
import java.util.List;
public class TicketRepositoryDB extends JdbcSupport implements TicketRepositoryPort {
    private final RowMapper<Ticket> mapper;
    public TicketRepositoryDB(Connection connection, RowMapper<Ticket> mapper) { super(connection); this.mapper = mapper; }
    private Object[] values(Ticket x) { return new Object[]{x.getCliente().getCedula(), x.getPelicula().getId(), x.getSala().getNumeroSala(), x.getAsiento().getCodigo(), x.getCombo() == null ? null : x.getCombo().getId(), x.getPrecio(), x.getId()}; }
    public Ticket saveTicket(Ticket x) { try { execute("INSERT INTO ticket (cliente_id,pelicula_id,sala_numero,asiento_codigo,combo_id,precio,id) VALUES (?,?,?,?,?,?,?)", values(x)); return x; } catch (SQLException e) { throw failure(e); } }
    public Ticket selectTicketById(int id) { try { return first(query("SELECT * FROM ticket WHERE id=?", mapper, id)); } catch (SQLException e) { throw failure(e); } }
    public List<Ticket> selectAll() { try { return query("SELECT * FROM ticket", mapper); } catch (SQLException e) { throw failure(e); } }
    public Ticket updateTicket(Ticket x) { try { return execute("UPDATE ticket SET cliente_id=?,pelicula_id=?,sala_numero=?,asiento_codigo=?,combo_id=?,precio=? WHERE id=?", values(x)) == 0 ? null : x; } catch (SQLException e) { throw failure(e); } }
    public boolean deleteTicket(int id) { try { return execute("DELETE FROM ticket WHERE id=?", id) > 0; } catch (SQLException e) { throw failure(e); } }
}
