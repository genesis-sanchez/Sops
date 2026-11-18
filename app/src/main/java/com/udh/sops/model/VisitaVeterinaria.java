package com.udh.sops.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "visitas_veterinarias",
        foreignKeys = @ForeignKey(entity = Mascota.class, parentColumns = "id",
                childColumns = "mascotaId", onDelete = ForeignKey.CASCADE),
        indices = {@Index("mascotaId")})
public class VisitaVeterinaria {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private final long mascotaId;
    private final String fecha;
    private final String motivo;
    private final String diagnostico;
    private final String tratamiento;
    private final String veterinario;
    private final String clinica;
    private final String peso;
    private final String proximaRevision;
    private final String observaciones;

    public VisitaVeterinaria(long mascotaId, String fecha, String motivo, String diagnostico,
                            String tratamiento, String veterinario, String clinica, String peso,
                            String proximaRevision, String observaciones) {
        this.mascotaId = mascotaId;
        this.fecha = fecha;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.veterinario = veterinario;
        this.clinica = clinica;
        this.peso = peso;
        this.proximaRevision = proximaRevision;
        this.observaciones = observaciones;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getMascotaId() { return mascotaId; }
    public String getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public String getDiagnostico() { return diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public String getVeterinario() { return veterinario; }
    public String getClinica() { return clinica; }
    public String getPeso() { return peso; }
    public String getProximaRevision() { return proximaRevision; }
    public String getObservaciones() { return observaciones; }
}
