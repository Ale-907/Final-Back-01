package app.persistence.mapper;
import app.domain.Sala;
import app.domain.Asiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SalaMapper implements RowMapper<Sala> {
    private final Connection connection;
    private final RowMapper<Asiento> asientoMapper;
    public SalaMapper(Connection connection, RowMapper<Asiento> asientoMapper) { this.connection = connection; this.asientoMapper = asientoMapper; }
    public Sala mapRow(ResultSet result) throws SQLException {
        int numero = result.getInt("numero");
        Sala sala = new Sala(numero, 0, result.getString("tipo"));
        List<Asiento> asientos = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM asiento WHERE sala_numero=? ORDER BY codigo")) {
            ps.setInt(1, numero);
            try (ResultSet rows = ps.executeQuery()) { while (rows.next()) asientos.add(asientoMapper.mapRow(rows)); }
        }
        sala.setAsientos(asientos);
        return sala;
    }
}
