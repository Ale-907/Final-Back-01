package app.service;

import app.domain.*;
import app.service.inputport.ComboComidaServiceInterface;
import app.service.outputport.ComboComidaRepositoryPort;
import app.service.helpers.DatosRelacionados;
import app.service.validations.Validaciones;
import java.util.List;

public class ComboComidaServiceImpl implements ComboComidaServiceInterface {
    private final ComboComidaRepositoryPort repository;
    private final DatosRelacionados datos;

    public ComboComidaServiceImpl(ComboComidaRepositoryPort repository, DatosRelacionados datos) {
        this.repository = repository;
        this.datos = datos;
    }
    @Override
    public ComboComida createComboComida(int id, String nombreCombo, double precio) {
        Validaciones.positivo(id, "id");
        Validaciones.texto(nombreCombo, "nombreCombo");
        Validaciones.positivo(precio, "precio");
        if (precio > 1000000) { throw new IllegalArgumentException("Maximo $1.000.000 en este ejercicio."); }
        Validaciones.libre(repository.selectComboComidaById(id) != null, "El identificador ya existe.");
        
        
        ComboComida objeto = new ComboComida(id, nombreCombo, precio);
        repository.saveComboComida(objeto);
        
        return objeto;
    }
    @Override
    public ComboComida selectComboComidaById(int id) { return repository.selectComboComidaById(id); }

    @Override
    public List<ComboComida> selectAll() { return repository.selectAll(); }

    @Override
    public ComboComida updateComboComida(int id, String nombreCombo, double precio) {
        Validaciones.positivo(id, "id");
        Validaciones.texto(nombreCombo, "nombreCombo");
        Validaciones.positivo(precio, "precio");
        if (precio > 1000000) { throw new IllegalArgumentException("Maximo $1.000.000 en este ejercicio."); }
        ComboComida objeto = repository.selectComboComidaById(id);
        Validaciones.existe(objeto, "El registro no existe.");
        Validaciones.libre(datos.comboEnUso(id), "No cambie un combo que ya aparece en tickets.");
        
        objeto.setNombreCombo(nombreCombo);
        objeto.setPrecio(precio);
        repository.updateComboComida(objeto);
        
        return objeto;
    }
    @Override
    public boolean deleteComboComida(int id) {
        Validaciones.libre(datos.comboEnUso(id), "El combo tiene tickets registrados.");
        boolean eliminado = repository.deleteComboComida(id);
        
        return eliminado;
    }
}
