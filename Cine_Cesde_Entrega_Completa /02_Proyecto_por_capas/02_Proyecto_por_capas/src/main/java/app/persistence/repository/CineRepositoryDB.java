package app.persistence.repository;
import app.domain.Cine;
import app.domain.Pelicula;
import app.persistence.mapper.RowMapper;
import app.service.outputport.CineRepositoryPort;
import java.sql.*;
import java.util.List;
public class CineRepositoryDB extends JdbcSupport implements CineRepositoryPort {
    private final RowMapper<Cine> mapper;
    public CineRepositoryDB(Connection connection, RowMapper<Cine> mapper) { super(connection); this.mapper = mapper; }
    private void catalogo(Cine cine) throws SQLException {
        execute("DELETE FROM cine_pelicula WHERE cine_id=?", cine.getId());
        for (Pelicula pelicula : cine.getPeliculas()) execute("INSERT INTO cine_pelicula VALUES (?,?)", cine.getId(), pelicula.getId());
    }
    public Cine saveCine(Cine cine) { return transaction(() -> { execute("INSERT INTO cine VALUES (?,?)", cine.getId(), cine.getNombre()); catalogo(cine); return cine; }); }
    public Cine selectCineById(int id) { try { return first(query("SELECT * FROM cine WHERE id=?", mapper, id)); } catch (SQLException e) { throw failure(e); } }
    public List<Cine> selectAll() { try { return query("SELECT * FROM cine", mapper); } catch (SQLException e) { throw failure(e); } }
    public Cine updateCine(Cine cine) { return transaction(() -> { if (execute("UPDATE cine SET nombre=? WHERE id=?", cine.getNombre(), cine.getId()) == 0) return null; catalogo(cine); return cine; }); }
    public boolean deleteCine(int id) { try { return execute("DELETE FROM cine WHERE id=?", id) > 0; } catch (SQLException e) { throw failure(e); } }
}
