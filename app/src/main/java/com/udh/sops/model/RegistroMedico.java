package com.udh.sops.model;

public class RegistroMedico {
    private final String fecha;
    private final String motivo;
    private final String veterinario;
    private final String observaciones;

    public RegistroMedico(String fecha, String motivo, String veterinario, String observaciones) {
        this.fecha = fecha;
        this.motivo = motivo;
        this.veterinario = veterinario;
        this.observaciones = observaciones;
    }

    public String getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public String getVeterinario() { return veterinario; }
    public String getObservaciones() { return observaciones; }
}
