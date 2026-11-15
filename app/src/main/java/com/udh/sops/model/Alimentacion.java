package com.udh.sops.model;

public class Alimentacion {
    private final String alimento;
    private final String cantidad;
    private final String horarios;
    private final String observaciones;

    public Alimentacion(String alimento, String cantidad, String horarios, String observaciones) {
        this.alimento = alimento;
        this.cantidad = cantidad;
        this.horarios = horarios;
        this.observaciones = observaciones;
    }

    public String getAlimento() { return alimento; }
    public String getCantidad() { return cantidad; }
    public String getHorarios() { return horarios; }
    public String getObservaciones() { return observaciones; }
}
