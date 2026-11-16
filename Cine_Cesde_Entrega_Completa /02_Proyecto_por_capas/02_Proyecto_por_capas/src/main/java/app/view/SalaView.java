package app.view;

import app.domain.Sala;
import app.service.inputport.SalaServiceInterface;
import app.service.validations.FormTypeValidator;

public class SalaView {
    private final SalaServiceInterface service;
    public SalaView(SalaServiceInterface service) { this.service = service; }
    public void createSala() {
        service.createSala(FormTypeValidator.intValidator("Numero de sala"),
                FormTypeValidator.intValidator("Cantidad de asientos (1 a 500)"),
                FormTypeValidator.stringValidator("Tipo (2D o 3D)").toUpperCase());
        System.out.println("Registro creado.");
    }
    public void selectAll() {
        for (Sala sala : service.selectAll()) mostrar(sala);
    }
    public void selectSalaById() {
        Sala sala = service.selectSalaById(FormTypeValidator.intValidator("Numero de sala"));
        if (sala == null) System.out.println("La sala no existe."); else mostrar(sala);
    }
    public void updateSala() {
        service.updateSala(FormTypeValidator.intValidator("Numero de sala"),
                FormTypeValidator.intValidator("Cantidad de asientos (1 a 500)"),
                FormTypeValidator.stringValidator("Tipo (2D o 3D)").toUpperCase());
        System.out.println("Registro actualizado.");
    }
    public void deleteSala() {
        System.out.println(service.deleteSala(FormTypeValidator.intValidator("Numero de sala"))
                ? "Registro eliminado." : "La sala no existe.");
    }
    private void mostrar(Sala sala) {
        System.out.println(sala.getNumeroSala() + " - " + sala.getTipo() + " - Capacidad: " + sala.getCapacidadAsientos());
        sala.getAsientos().forEach(a -> System.out.print(a.getCodigo() + " "));
        System.out.println();
    }
}
