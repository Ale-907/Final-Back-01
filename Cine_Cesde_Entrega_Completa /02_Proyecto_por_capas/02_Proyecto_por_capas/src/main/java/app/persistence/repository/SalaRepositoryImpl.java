package app.persistence.repository;
import app.domain.Sala;
import app.service.outputport.SalaRepositoryPort;
import java.util.ArrayList;
import java.util.List;
public class SalaRepositoryImpl implements SalaRepositoryPort {
    private final List<Sala> salas = new ArrayList<>();
    public Sala saveSala(Sala sala) { salas.add(sala); return sala; }
    public Sala selectSalaById(int numero) { for (Sala sala : salas) if (sala.getNumeroSala() == numero) return sala; return null; }
    public List<Sala> selectAll() { return new ArrayList<>(salas); }
    public Sala updateSala(Sala sala) { return selectSalaById(sala.getNumeroSala()); }
    public boolean deleteSala(int numero) { Sala sala = selectSalaById(numero); return sala != null && salas.remove(sala); }
}
