package app.service.outputport;

import app.domain.Asiento;
import java.util.List;

public interface AsientoRepositoryPort {
    Asiento saveAsiento(Asiento objeto);
    Asiento selectAsientoById(String id);
    List<Asiento> selectAll();
    Asiento updateAsiento(Asiento objeto);
    default Asiento renombrarAsiento(String idAnterior, Asiento objeto) { return updateAsiento(objeto); }
    boolean deleteAsiento(String id);
}
