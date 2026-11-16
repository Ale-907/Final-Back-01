package app.persistence.mapper;

import app.domain.Cliente;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteMapper implements RowMapper<Cliente> {
    @Override
    public Cliente mapRow(ResultSet resultSet) throws SQLException {
        return new Cliente(resultSet.getString("cedula"), resultSet.getString("nombre"), resultSet.getInt("puntos"));
    }
}
