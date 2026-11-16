package app.persistence.repository;

import app.domain.Pelicula;
import app.persistence.mapper.RowMapper;
import app.service.outputport.PeliculaRepositoryPort;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaRepositoryDB implements PeliculaRepositoryPort {
    private final Connection connection;
    private final RowMapper<Pelicula> mapper;
    public PeliculaRepositoryDB(Connection connection, RowMapper<Pelicula> mapper) { this.connection = connection; this.mapper = mapper; }
    private int execute(String sql, Object... values) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) statement.setObject(i + 1, values[i]);
            return statement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error de base de datos: " + e.getMessage(), e); }
    }
    private List<Pelicula> query(String sql, Object... values) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) statement.setObject(i + 1, values[i]);
            try (ResultSet result = statement.executeQuery()) {
                List<Pelicula> peliculas = new ArrayList<>();
                while (result.next()) peliculas.add(mapper.mapRow(result));
                return peliculas;
            }
        } catch (SQLException e) { throw new RuntimeException("Error de base de datos: " + e.getMessage(), e); }
    }
    public Pelicula savePelicula(Pelicula pelicula) { execute("INSERT INTO pelicula VALUES (?,?,?,?)", pelicula.getId(), pelicula.getTitulo(), pelicula.getDuracionMinutos(), pelicula.getClasificacion().name()); return pelicula; }
    public Pelicula selectPeliculaById(int id) { List<Pelicula> found = query("SELECT * FROM pelicula WHERE id=?", id); return found.isEmpty() ? null : found.get(0); }
    public List<Pelicula> selectAll() { return query("SELECT * FROM pelicula"); }
    public Pelicula updatePelicula(Pelicula pelicula) { return execute("UPDATE pelicula SET titulo=?,duracion=?,clasificacion=? WHERE id=?", pelicula.getTitulo(), pelicula.getDuracionMinutos(), pelicula.getClasificacion().name(), pelicula.getId()) == 0 ? null : pelicula; }
    public boolean deletePelicula(int id) { return execute("DELETE FROM pelicula WHERE id=?", id) > 0; }
}
