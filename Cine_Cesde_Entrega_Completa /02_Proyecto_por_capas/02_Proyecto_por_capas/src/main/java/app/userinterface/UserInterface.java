package app.userinterface;

import app.view.*;
import app.service.validations.FormTypeValidator;

public class UserInterface {
    private final ClienteView clienteView;
    private final PeliculaView peliculaView;
    private final ComboComidaView comboComidaView;
    private final CineView cineView;
    private final SalaView salaView;
    private final AsientoView asientoView;
    private final TicketView ticketView;

    public UserInterface(ClienteView clienteView, PeliculaView peliculaView, ComboComidaView comboComidaView, CineView cineView, SalaView salaView, AsientoView asientoView, TicketView ticketView) {
        this.clienteView = clienteView;
        this.peliculaView = peliculaView;
        this.comboComidaView = comboComidaView;
        this.cineView = cineView;
        this.salaView = salaView;
        this.asientoView = asientoView;
        this.ticketView = ticketView;
    }
    public void menuInicio() {
        System.out.println("CINE CESDE - PROYECTO POR CAPAS");
        System.out.println("Los datos se guardan en la base de datos configurada.");
        System.out.println("Cada sala representa una funcion de ejemplo, sin fechas ni horarios.");
        try { menuApp(); }
        catch (IllegalStateException e) { System.out.println("Entrada finalizada. Programa cerrado."); }
    }
    public void menuApp() {
        while (true) {
            System.out.println("\nMENU PRINCIPAL");
            System.out.println("1. Cliente");
            System.out.println("2. Pelicula");
            System.out.println("3. ComboComida");
            System.out.println("4. Cine");
            System.out.println("5. Sala");
            System.out.println("6. Asiento");
            System.out.println("7. Ticket");
            System.out.println("0. Salir");
            int opcion = FormTypeValidator.intValidator("Opcion");
            switch (opcion) {
                case 1: menuCliente(); break;
                case 2: menuPelicula(); break;
                case 3: menuComboComida(); break;
                case 4: menuCine(); break;
                case 5: menuSala(); break;
                case 6: menuAsiento(); break;
                case 7: menuTicket(); break;
                case 0: System.out.println("Hasta luego."); return;
                default: System.out.println("Seleccione una opcion valida.");
            }
        }
    }

    public void menuCliente() {
        while (true) {
            System.out.println("\nCliente\n1. Crear\n2. Listar todos\n3. Buscar por ID\n4. Actualizar\n5. Eliminar\n0. Volver");
            int opcion = FormTypeValidator.intValidator("Opcion");
            try {
                switch (opcion) {
                    case 1: clienteView.createCliente(); break;
                    case 2: clienteView.selectAll(); break;
                    case 3: clienteView.selectClienteById(); break;
                    case 4: clienteView.updateCliente(); break;
                    case 5: clienteView.deleteCliente(); break;
                    case 0: return;
                    default: System.out.println("Seleccione una opcion valida.");
                }
            } catch (IllegalArgumentException e) { System.out.println("Aviso: " + e.getMessage()); }
        }
    }

    public void menuPelicula() {
        while (true) {
            System.out.println("\nPelicula\n1. Crear\n2. Listar todos\n3. Buscar por ID\n4. Actualizar\n5. Eliminar\n0. Volver");
            int opcion = FormTypeValidator.intValidator("Opcion");
            try {
                switch (opcion) {
                    case 1: peliculaView.createPelicula(); break;
                    case 2: peliculaView.selectAll(); break;
                    case 3: peliculaView.selectPeliculaById(); break;
                    case 4: peliculaView.updatePelicula(); break;
                    case 5: peliculaView.deletePelicula(); break;
                    case 0: return;
                    default: System.out.println("Seleccione una opcion valida.");
                }
            } catch (IllegalArgumentException e) { System.out.println("Aviso: " + e.getMessage()); }
        }
    }

    public void menuComboComida() {
        while (true) {
            System.out.println("\nComboComida\n1. Crear\n2. Listar todos\n3. Buscar por ID\n4. Actualizar\n5. Eliminar\n0. Volver");
            int opcion = FormTypeValidator.intValidator("Opcion");
            try {
                switch (opcion) {
                    case 1: comboComidaView.createComboComida(); break;
                    case 2: comboComidaView.selectAll(); break;
                    case 3: comboComidaView.selectComboComidaById(); break;
                    case 4: comboComidaView.updateComboComida(); break;
                    case 5: comboComidaView.deleteComboComida(); break;
                    case 0: return;
                    default: System.out.println("Seleccione una opcion valida.");
                }
            } catch (IllegalArgumentException e) { System.out.println("Aviso: " + e.getMessage()); }
        }
    }

    public void menuCine() {
        while (true) {
            System.out.println("\nCine\n1. Crear\n2. Listar todos\n3. Buscar por ID\n4. Actualizar\n5. Eliminar\n6. Agregar pelicula\n7. Retirar pelicula\n0. Volver");
            int opcion = FormTypeValidator.intValidator("Opcion");
            try {
                switch (opcion) {
                    case 1: cineView.createCine(); break;
                    case 2: cineView.selectAll(); break;
                    case 3: cineView.selectCineById(); break;
                    case 4: cineView.updateCine(); break;
                    case 5: cineView.deleteCine(); break;
                    case 6: cineView.agregarPelicula(); break;
                    case 7: cineView.retirarPelicula(); break;
                    case 0: return;
                    default: System.out.println("Seleccione una opcion valida.");
                }
            } catch (IllegalArgumentException e) { System.out.println("Aviso: " + e.getMessage()); }
        }
    }

    public void menuSala() {
        while (true) {
            System.out.println("\nSala\n1. Crear\n2. Listar todos\n3. Buscar por ID\n4. Actualizar\n5. Eliminar\n0. Volver");
            int opcion = FormTypeValidator.intValidator("Opcion");
            try {
                switch (opcion) {
                    case 1: salaView.createSala(); break;
                    case 2: salaView.selectAll(); break;
                    case 3: salaView.selectSalaById(); break;
                    case 4: salaView.updateSala(); break;
                    case 5: salaView.deleteSala(); break;
                    case 0: return;
                    default: System.out.println("Seleccione una opcion valida.");
                }
            } catch (IllegalArgumentException e) { System.out.println("Aviso: " + e.getMessage()); }
        }
    }

    public void menuAsiento() {
        while (true) {
            System.out.println("\nAsiento\n1. Crear\n2. Listar todos\n3. Buscar por ID\n4. Actualizar\n5. Eliminar\n0. Volver");
            int opcion = FormTypeValidator.intValidator("Opcion");
            try {
                switch (opcion) {
                    case 1: asientoView.createAsiento(); break;
                    case 2: asientoView.selectAll(); break;
                    case 3: asientoView.selectAsientoById(); break;
                    case 4: asientoView.updateAsiento(); break;
                    case 5: asientoView.deleteAsiento(); break;
                    case 0: return;
                    default: System.out.println("Seleccione una opcion valida.");
                }
            } catch (IllegalArgumentException e) { System.out.println("Aviso: " + e.getMessage()); }
        }
    }

    public void menuTicket() {
        while (true) {
            System.out.println("\nTicket\n1. Crear\n2. Listar todos\n3. Buscar por ID\n4. Actualizar\n5. Eliminar\n0. Volver");
            int opcion = FormTypeValidator.intValidator("Opcion");
            try {
                switch (opcion) {
                    case 1: ticketView.createTicket(); break;
                    case 2: ticketView.selectAll(); break;
                    case 3: ticketView.selectTicketById(); break;
                    case 4: ticketView.updateTicket(); break;
                    case 5: ticketView.deleteTicket(); break;
                    case 0: return;
                    default: System.out.println("Seleccione una opcion valida.");
                }
            } catch (IllegalArgumentException e) { System.out.println("Aviso: " + e.getMessage()); }
        }
    }
}
