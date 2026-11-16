package app.service.outputport;

import app.domain.Sala;
import java.util.List;

public interface SalaRepositoryPort {
    Sala saveSala(Sala sala);
    Sala selectSalaById(int numero);
    List<Sala> selectAll();
    Sala updateSala(Sala sala);
    boolean deleteSala(int numero);
}
