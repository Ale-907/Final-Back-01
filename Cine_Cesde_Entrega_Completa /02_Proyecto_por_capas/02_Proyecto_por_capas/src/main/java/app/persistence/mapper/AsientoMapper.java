package app.persistence.mapper;
import app.domain.Asiento;
import java.sql.ResultSet;
import java.sql.SQLException;
public class AsientoMapper implements RowMapper<Asiento> {
    public Asiento mapRow(ResultSet result) throws SQLException {
        return new Asiento(result.getInt("sala_numero"), result.getString("codigo"));
    }
}
