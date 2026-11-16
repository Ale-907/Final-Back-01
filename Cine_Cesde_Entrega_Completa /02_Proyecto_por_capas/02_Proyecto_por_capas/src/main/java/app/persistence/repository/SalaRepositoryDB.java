package app.persistence.repository;
import app.domain.Sala;
import app.domain.Asiento;
import app.persistence.mapper.RowMapper;
import app.service.outputport.SalaRepositoryPort;
import java.sql.*;
import java.util.List;
public class SalaRepositoryDB extends JdbcSupport implements SalaRepositoryPort {
    private final RowMapper<Sala> mapper;
    public SalaRepositoryDB(Connection connection, RowMapper<Sala> mapper) { super(connection); this.mapper = mapper; }
    private Sala guardar(Sala sala, boolean nuevo) {
        return transaction(() -> {
            if (nuevo) execute("INSERT INTO sala (numero,tipo) VALUES (?,?)", sala.getNumeroSala(), sala.getTipo());
            else {
                if (execute("UPDATE sala SET tipo=? WHERE numero=?", sala.getTipo(), sala.getNumeroSala()) == 0) return null;
                execute("DELETE FROM asiento WHERE sala_numero=?", sala.getNumeroSala());
            }
            for (Asiento asiento : sala.getAsientos()) execute("INSERT INTO asiento VALUES (?,?)", sala.getNumeroSala(), asiento.getCodigo());
            return sala;
        });
    }
    public Sala saveSala(Sala sala) { return guardar(sala, true); }
    public Sala selectSalaById(int id) { try { return first(query("SELECT * FROM sala WHERE numero=?", mapper, id)); } catch (SQLException e) { throw failure(e); } }
    public List<Sala> selectAll() { try { return query("SELECT * FROM sala", mapper); } catch (SQLException e) { throw failure(e); } }
    public Sala updateSala(Sala sala) { return guardar(sala, false); }
    public boolean deleteSala(int id) { try { return execute("DELETE FROM sala WHERE numero=?", id) > 0; } catch (SQLException e) { throw failure(e); } }
}
