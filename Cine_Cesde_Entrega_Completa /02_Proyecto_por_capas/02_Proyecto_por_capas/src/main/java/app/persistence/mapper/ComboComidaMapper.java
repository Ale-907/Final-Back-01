package app.persistence.mapper;

import app.domain.ComboComida;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComboComidaMapper implements RowMapper<ComboComida> {
    @Override
    public ComboComida mapRow(ResultSet resultSet) throws SQLException {
        return new ComboComida(resultSet.getInt("id"), resultSet.getString("nombre"), resultSet.getDouble("precio"));
    }
}
