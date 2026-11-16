package app.persistence.repository;
import app.domain.Asiento;
import app.persistence.mapper.RowMapper;
import app.service.outputport.AsientoRepositoryPort;
import java.sql.*;
import java.util.List;
public class AsientoRepositoryDB extends JdbcSupport implements AsientoRepositoryPort {
    private final RowMapper<Asiento> mapper;
    public AsientoRepositoryDB(Connection connection, RowMapper<Asiento> mapper) { super(connection); this.mapper = mapper; }
    private Object[] parts(String id) { int p = id.indexOf(':'); if (p < 1) return null; try { return new Object[]{Integer.parseInt(id.substring(0,p)), id.substring(p+1)}; } catch (NumberFormatException e) { return null; } }
    public Asiento saveAsiento(Asiento x) { try { execute("INSERT INTO asiento VALUES (?,?)", x.getNumeroSala(), x.getCodigo()); return x; } catch (SQLException e) { throw failure(e); } }
    public Asiento selectAsientoById(String id) { Object[] p = parts(id); try { return p == null ? null : first(query("SELECT * FROM asiento WHERE sala_numero=? AND codigo=?", mapper, p)); } catch (SQLException e) { throw failure(e); } }
    public List<Asiento> selectAll() { try { return query("SELECT * FROM asiento", mapper); } catch (SQLException e) { throw failure(e); } }
    public Asiento updateAsiento(Asiento x) { return selectAsientoById(x.getId()); }
    public Asiento renombrarAsiento(String old, Asiento x) { Object[] p = parts(old); try { return p != null && execute("UPDATE asiento SET codigo=? WHERE sala_numero=? AND codigo=?", x.getCodigo(), p[0], p[1]) > 0 ? x : null; } catch (SQLException e) { throw failure(e); } }
    public boolean deleteAsiento(String id) { Object[] p = parts(id); try { return p != null && execute("DELETE FROM asiento WHERE sala_numero=? AND codigo=?", p) > 0; } catch (SQLException e) { throw failure(e); } }
}
