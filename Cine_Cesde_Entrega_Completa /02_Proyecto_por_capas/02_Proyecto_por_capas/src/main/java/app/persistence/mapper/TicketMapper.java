package app.persistence.mapper;
import app.domain.*;
import app.service.outputport.*;
import java.sql.ResultSet;
import java.sql.SQLException;
public class TicketMapper implements RowMapper<Ticket> {
    private final ClienteRepositoryPort clientes;
    private final PeliculaRepositoryPort peliculas;
    private final SalaRepositoryPort salas;
    private final ComboComidaRepositoryPort combos;
    public TicketMapper(ClienteRepositoryPort clientes, PeliculaRepositoryPort peliculas, SalaRepositoryPort salas, ComboComidaRepositoryPort combos) {
        this.clientes = clientes; this.peliculas = peliculas; this.salas = salas; this.combos = combos;
    }
    public Ticket mapRow(ResultSet result) throws SQLException {
        Cliente cliente = clientes.selectClienteById(result.getString("cliente_id"));
        Pelicula pelicula = peliculas.selectPeliculaById(result.getInt("pelicula_id"));
        Sala sala = salas.selectSalaById(result.getInt("sala_numero"));
        String codigo = result.getString("asiento_codigo");
        Asiento asiento = null;
        for (Asiento item : sala.getAsientos()) if (item.getCodigo().equals(codigo)) { asiento = item; break; }
        int comboId = result.getInt("combo_id");
        ComboComida combo = result.wasNull() ? null : combos.selectComboComidaById(comboId);
        return new Ticket(result.getInt("id"), cliente, pelicula, sala, asiento, combo, result.getDouble("precio"));
    }
}
