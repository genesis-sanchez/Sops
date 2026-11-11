package com.udh.sops.model;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
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
