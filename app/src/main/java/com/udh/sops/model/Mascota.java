package com.udh.sops.model;

import java.io.Serializable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "mascotas")
public class Mascota implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String nombre;
    private String tipo;
    private String raza;
    private String edad;
    private String sexo;
    private boolean esterilizado;
    private String fechaNacimiento;
    private String peso;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public Mascota(String nombre, String tipo, String raza, String edad, String sexo,
                   boolean esterilizado, String fechaNacimiento, String peso) {
        this(nombre, tipo, raza, edad, sexo, esterilizado);
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
    }

    public String getFechaNacimiento() { return fechaNacimiento; }

    public String getPeso() { return peso; }

    @Ignore
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
