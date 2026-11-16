package com.udh.sops.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacunas")
public class Vacuna {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private final String nombre;
    private final int dosis;
    private final String fecha;
    private final String proximaFecha;
    private final boolean aplicada;
    private final String veterinario;
    private final String observaciones;
    private final boolean recordatorio;

    @Ignore
    public Vacuna(String nombre, int dosis, String fecha, String proximaFecha, boolean aplicada) {
        this(nombre, dosis, fecha, proximaFecha, aplicada, null, null, false);
    }

    public Vacuna(String nombre, int dosis, String fecha, String proximaFecha,
                  boolean aplicada, String veterinario, String observaciones, boolean recordatorio) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.fecha = fecha;
        this.proximaFecha = proximaFecha;
        this.aplicada = aplicada;
        this.veterinario = veterinario;
        this.observaciones = observaciones;
        this.recordatorio = recordatorio;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public int getDosis() { return dosis; }
    public String getFecha() { return fecha; }
    public String getProximaFecha() { return proximaFecha; }
    public boolean isAplicada() { return aplicada; }
    public String getVeterinario() { return veterinario; }
    public String getObservaciones() { return observaciones; }
    public boolean isRecordatorio() { return recordatorio; }
}
