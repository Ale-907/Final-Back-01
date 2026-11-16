package app.service;

import app.domain.Sala;
import app.service.helpers.DatosRelacionados;
import app.service.inputport.SalaServiceInterface;
import app.service.outputport.SalaRepositoryPort;
import app.service.validations.Validaciones;
import java.util.List;

public class SalaServiceImpl implements SalaServiceInterface {
    private final SalaRepositoryPort repository;
    private final DatosRelacionados datos;
    public SalaServiceImpl(SalaRepositoryPort repository, DatosRelacionados datos) {
        this.repository = repository;
        this.datos = datos;
    }
    private void validar(int numero, int capacidad) {
        Validaciones.positivo(numero, "numeroSala");
        Validaciones.positivo(capacidad, "capacidadAsientos");
        if (capacidad > 500) throw new IllegalArgumentException("Maximo 500 asientos.");
    }
    public Sala createSala(int numero, int capacidad, String tipo) {
        validar(numero, capacidad);
        Validaciones.libre(repository.selectSalaById(numero) != null, "La sala ya existe.");
        return repository.saveSala(new Sala(numero, capacidad, tipo));
    }
    public Sala selectSalaById(int numero) { return repository.selectSalaById(numero); }
    public List<Sala> selectAll() { return repository.selectAll(); }
    public Sala updateSala(int numero, int capacidad, String tipo) {
        validar(numero, capacidad);
        Sala sala = repository.selectSalaById(numero);
        Validaciones.existe(sala, "La sala no existe.");
        Validaciones.libre(datos.salaEnUso(numero), "No modifique una sala con tickets registrados.");
        sala.setTipo(tipo);
        sala.setCapacidadAsientos(capacidad);
        return repository.updateSala(sala);
    }
    public boolean deleteSala(int numero) {
        Validaciones.libre(datos.salaEnUso(numero), "La sala tiene tickets registrados.");
        return repository.deleteSala(numero);
    }
}
