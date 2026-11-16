package app.persistence.repository;

import app.domain.ComboComida;
import app.persistence.mapper.RowMapper;
import app.service.outputport.ComboComidaRepositoryPort;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComboComidaRepositoryDB implements ComboComidaRepositoryPort {
    private final Connection connection;
    private final RowMapper<ComboComida> mapper;
    public ComboComidaRepositoryDB(Connection connection, RowMapper<ComboComida> mapper) { this.connection = connection; this.mapper = mapper; }
    private int execute(String sql, Object... values) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) statement.setObject(i + 1, values[i]);
            return statement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error de base de datos: " + e.getMessage(), e); }
    }
    private List<ComboComida> query(String sql, Object... values) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) statement.setObject(i + 1, values[i]);
            try (ResultSet result = statement.executeQuery()) {
                List<ComboComida> combos = new ArrayList<>();
                while (result.next()) combos.add(mapper.mapRow(result));
                return combos;
            }
        } catch (SQLException e) { throw new RuntimeException("Error de base de datos: " + e.getMessage(), e); }
    }
    public ComboComida saveComboComida(ComboComida combo) { execute("INSERT INTO combo VALUES (?,?,?)", combo.getId(), combo.getNombreCombo(), combo.getPrecio()); return combo; }
    public ComboComida selectComboComidaById(int id) { List<ComboComida> found = query("SELECT * FROM combo WHERE id=?", id); return found.isEmpty() ? null : found.get(0); }
    public List<ComboComida> selectAll() { return query("SELECT * FROM combo"); }
    public ComboComida updateComboComida(ComboComida combo) { return execute("UPDATE combo SET nombre=?,precio=? WHERE id=?", combo.getNombreCombo(), combo.getPrecio(), combo.getId()) == 0 ? null : combo; }
    public boolean deleteComboComida(int id) { return execute("DELETE FROM combo WHERE id=?", id) > 0; }
}
