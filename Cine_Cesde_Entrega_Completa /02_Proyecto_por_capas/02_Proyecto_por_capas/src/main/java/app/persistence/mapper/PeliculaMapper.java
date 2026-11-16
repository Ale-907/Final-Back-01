package app.persistence.mapper;

import app.domain.Pelicula;
import app.domain.enums.ClasificacionEnum;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeliculaMapper implements RowMapper<Pelicula> {
    @Override
    public Pelicula mapRow(ResultSet resultSet) throws SQLException {
        return new Pelicula(resultSet.getInt("id"), resultSet.getString("titulo"), resultSet.getInt("duracion"),
                ClasificacionEnum.valueOf(resultSet.getString("clasificacion")));
    }
}
