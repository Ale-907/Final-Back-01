package app.persistence.repository;

import app.domain.Cliente;
import app.persistence.mapper.RowMapper;
import app.service.outputport.ClienteRepositoryPort;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryDB implements ClienteRepositoryPort {
    private final Connection connection;
    private final RowMapper<Cliente> mapper;
    private static final String SELECT = "SELECT c.cedula,c.nombre,COALESCE((SELECT SUM(FLOOR((t.precio+COALESCE(b.precio,0))/1000)) FROM ticket t LEFT JOIN combo b ON b.id=t.combo_id WHERE t.cliente_id=c.cedula),0) puntos FROM cliente c";

    public ClienteRepositoryDB(Connection connection, RowMapper<Cliente> mapper) {
        this.connection = connection;
        this.mapper = mapper;
    }
    private int execute(String sql, Object... values) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) statement.setObject(i + 1, values[i]);
            return statement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error de base de datos: " + e.getMessage(), e); }
    }
    private List<Cliente> query(String sql, Object... values) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) statement.setObject(i + 1, values[i]);
            try (ResultSet result = statement.executeQuery()) {
                List<Cliente> clientes = new ArrayList<>();
                while (result.next()) clientes.add(mapper.mapRow(result));
                return clientes;
            }
        } catch (SQLException e) { throw new RuntimeException("Error de base de datos: " + e.getMessage(), e); }
    }
    public Cliente saveCliente(Cliente cliente) { execute("INSERT INTO cliente VALUES (?,?)", cliente.getCedula(), cliente.getNombre()); return cliente; }
    public Cliente selectClienteById(String id) { List<Cliente> found = query(SELECT + " WHERE c.cedula=?", id); return found.isEmpty() ? null : found.get(0); }
    public List<Cliente> selectAll() { return query(SELECT); }
    public Cliente updateCliente(Cliente cliente) { return execute("UPDATE cliente SET nombre=? WHERE cedula=?", cliente.getNombre(), cliente.getCedula()) == 0 ? null : cliente; }
    public boolean deleteCliente(String id) { return execute("DELETE FROM cliente WHERE cedula=?", id) > 0; }
}
