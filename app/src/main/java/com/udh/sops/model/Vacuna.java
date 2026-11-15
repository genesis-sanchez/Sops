package com.udh.sops.model;

public class Vacuna {
    private final String nombre;
    private final int dosis;
    private final String fecha;
    private final String proximaFecha;
    private final boolean aplicada;

    public Vacuna(String nombre, int dosis, String fecha, String proximaFecha, boolean aplicada) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.fecha = fecha;
        this.proximaFecha = proximaFecha;
        this.aplicada = aplicada;
    }

    public String getNombre() { return nombre; }
    public int getDosis() { return dosis; }
    public String getFecha() { return fecha; }
    public String getProximaFecha() { return proximaFecha; }
    public boolean isAplicada() { return aplicada; }
}
