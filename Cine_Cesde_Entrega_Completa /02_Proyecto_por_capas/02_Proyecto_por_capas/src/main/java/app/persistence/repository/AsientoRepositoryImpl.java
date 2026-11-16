package app.persistence.repository;

import app.domain.*;
import app.service.outputport.AsientoRepositoryPort;
import app.service.helpers.DatosRelacionados;
import java.util.ArrayList;
import java.util.List;

public class AsientoRepositoryImpl implements AsientoRepositoryPort {
    private final DatosRelacionados datos;
    public AsientoRepositoryImpl(DatosRelacionados datos) { this.datos = datos; }

    @Override
    public Asiento saveAsiento(Asiento objeto) {
        Sala sala = datos.buscarSala(objeto.getNumeroSala());
        sala.agregarAsiento(objeto);
        return datos.buscarAsiento(objeto.getNumeroSala(), objeto.getCodigo());
    }
    @Override
    public Asiento selectAsientoById(String id) {
        for (Asiento asiento : selectAll()) { if (asiento.getId().equals(id)) { return asiento; } }
        return null;
    }
    @Override
    public List<Asiento> selectAll() {
        List<Asiento> asientos = new ArrayList<>();
        for (Sala sala : datos.obtenerSalas()) { asientos.addAll(sala.getAsientos()); }
        return asientos;
    }
    @Override
    public Asiento updateAsiento(Asiento objeto) {
        return selectAsientoById(objeto.getId());
    }
    @Override
    public boolean deleteAsiento(String id) {
        Asiento asiento = selectAsientoById(id);
        if (asiento == null) { return false; }
        return datos.buscarSala(asiento.getNumeroSala()).retirarAsiento(asiento.getCodigo());
    }
}
