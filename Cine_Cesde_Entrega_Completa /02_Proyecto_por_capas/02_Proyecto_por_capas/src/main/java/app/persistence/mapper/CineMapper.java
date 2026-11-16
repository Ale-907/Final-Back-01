package app.persistence.mapper;
import app.domain.Cine;
import app.domain.Pelicula;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CineMapper implements RowMapper<Cine> {
    private final Connection connection;
    private final RowMapper<Pelicula> peliculaMapper;
    public CineMapper(Connection connection, RowMapper<Pelicula> peliculaMapper) { this.connection = connection; this.peliculaMapper = peliculaMapper; }
    public Cine mapRow(ResultSet result) throws SQLException {
        Cine cine = new Cine(result.getInt("id"), result.getString("nombre"));
        List<Pelicula> peliculas = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT p.* FROM pelicula p JOIN cine_pelicula cp ON cp.pelicula_id=p.id WHERE cp.cine_id=?")) {
            ps.setInt(1, cine.getId());
            try (ResultSet rows = ps.executeQuery()) { while (rows.next()) peliculas.add(peliculaMapper.mapRow(rows)); }
        }
        cine.setPeliculas(peliculas);
        return cine;
    }
}
