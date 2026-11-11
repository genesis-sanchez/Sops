package com.udh.sops.model;

import java.io.Serializable;

public class Mascota implements Serializable {
    private String nombre;
    private String tipo;
    private String raza;
    private String edad;
    private String sexo;
    private boolean esterilizado;

    public Mascota(String nombre, String tipo, String raza, String edad, String sexo, boolean esterilizado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.edad = edad;
        this.sexo = sexo;
        this.esterilizado = esterilizado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getRaza() {
        return raza;
    }

    public String getEdad() {
        return edad;
    }

    public String getSexo() {
        return sexo;
    }

    public boolean isEsterilizado() {
        return esterilizado;
    }
}
