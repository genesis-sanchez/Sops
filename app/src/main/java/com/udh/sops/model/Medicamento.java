package com.udh.sops.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicamentos",
        foreignKeys = {
                @ForeignKey(entity = Mascota.class, parentColumns = "id", childColumns = "mascotaId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = VisitaVeterinaria.class, parentColumns = "id", childColumns = "visitaId",
                        onDelete = ForeignKey.SET_NULL)
        }, indices = {@Index("mascotaId"), @Index("visitaId")})
public class Medicamento {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private final long mascotaId;
    private final Long visitaId;
    private final String nombre;
    private final String dosis;
    private final int intervaloHoras;
    private final String fechaInicio;
    private final String fechaFin;
    private final String indicaciones;
    private final boolean activo;

    public Medicamento(long mascotaId, Long visitaId, String nombre, String dosis, int intervaloHoras, String fechaInicio, String fechaFin, String indicaciones, boolean activo) {
        this.mascotaId = mascotaId;
        this.visitaId = visitaId;
        this.nombre = nombre;
        this.dosis = dosis;
        this.intervaloHoras = intervaloHoras;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.indicaciones = indicaciones;
        this.activo = activo;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getMascotaId() { return mascotaId; }
    public Long getVisitaId() { return visitaId; }
    public String getNombre() { return nombre; }
    public String getDosis() { return dosis; }
    public int getIntervaloHoras() { return intervaloHoras; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public String getIndicaciones() { return indicaciones; }
    public boolean isActivo() { return activo; }
}
