package com.udh.sops.model;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static Alimentacion getAlimentacionEjemplo(Mascota mascota) {
        if ("Perro".equals(mascota.getTipo())) {
            return new Alimentacion("alimento seco para perros", "1 porcion por comida",
                    "08:00 y 18:00", "come en su plato del patio.");
        }
        if ("Gato".equals(mascota.getTipo())) {
            return new Alimentacion("alimento seco para gatos", "1 porcion por comida",
                    "08:00, 14:00 y 20:00", "prefiere comer en un lugar tranquilo.");
        }
        return null;
    }

    public static List<RegistroMedico> getHistorialEjemplo(Mascota mascota) {
        List<RegistroMedico> lista = new ArrayList<>();
        lista.add(new RegistroMedico("12/11/2026", "control de tratamiento",
                "veterinaria patitas", "seguimiento de " + mascota.getNombre()
                + ". se reviso su evolucion y se agendo el siguiente control."));
        lista.add(new RegistroMedico("05/11/2026", "visita veterinaria",
                "veterinaria patitas", "revision general de " + mascota.getNombre()
                + " y control de peso."));
        return lista;
    }

    public static List<Vacuna> getVacunasEjemplo(Mascota mascota) {
        List<Vacuna> lista = new ArrayList<>();
        if ("Perro".equals(mascota.getTipo())) {
            lista.add(new Vacuna("polivalente canina", 1, "10/06/2026", "10/06/2027", true));
            lista.add(new Vacuna("antirrabica", 1, "20/07/2026", "20/07/2027", true));
            lista.add(new Vacuna("refuerzo de ejemplo", 2, "20/12/2026", "20/12/2027", false));
        } else if ("Gato".equals(mascota.getTipo())) {
            lista.add(new Vacuna("triple felina", 1, "12/05/2026", "12/05/2027", true));
            lista.add(new Vacuna("antirrabica", 1, "18/08/2026", "18/08/2027", true));
            lista.add(new Vacuna("refuerzo de ejemplo", 2, "18/12/2026", "18/12/2027", false));
        }
        return lista;
    }

    public static List<Mascota> getMascotasEjemplo() {
        List<Mascota> lista = new ArrayList<>();
        lista.add(new Mascota("Firulais", "Perro", "Golden Retriever", "3 años", "Macho", true));
        lista.add(new Mascota("Michi", "Gato", "Siames", "2 años", "Hembra", true));
        lista.add(new Mascota("Bobby", "Perro", "Pastor Alemán", "5 años", "Macho", false));
        lista.add(new Mascota("Luna", "Gato", "Angora", "1 año", "Hembra", false));
        lista.add(new Mascota("Rocky", "Perro", "Bulldog", "4 años", "Macho", true));
        return lista;
    }
}
