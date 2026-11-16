package app.service.inputport;

import app.domain.Sala;
import java.util.List;

public interface SalaServiceInterface {
    Sala createSala(int numero, int capacidad, String tipo);
    Sala selectSalaById(int numero);
    List<Sala> selectAll();
    Sala updateSala(int numero, int capacidad, String tipo);
    boolean deleteSala(int numero);
}
