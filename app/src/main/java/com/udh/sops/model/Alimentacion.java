package com.udh.sops.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "alimentacion",
        foreignKeys = @ForeignKey(entity = Mascota.class, parentColumns = "id",
                childColumns = "mascotaId", onDelete = ForeignKey.CASCADE),
        indices = {@Index("mascotaId")})
public class Alimentacion {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private final long mascotaId;
    private final String nombreAlimento;
    private final String tipoAlimento;
    private final String cantidad;
    private final String unidad;
    private final int vecesAlDia;
    private final String horarios;
    private final String fechaInicio;
    private final String fechaFin;
    private final String observaciones;
    private final boolean activo;

    @Ignore
    public Alimentacion(String alimento, String cantidad, String horarios, String observaciones) {
        this(0, alimento, null, cantidad, null, 0, horarios, null, null, observaciones, true);
    }

    public Alimentacion(long mascotaId, String nombreAlimento, String tipoAlimento,
                        String cantidad, String unidad, int vecesAlDia, String horarios,
                        String fechaInicio, String fechaFin, String observaciones, boolean activo) {
        this.mascotaId = mascotaId;
        this.nombreAlimento = nombreAlimento;
        this.tipoAlimento = tipoAlimento;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.vecesAlDia = vecesAlDia;
        this.horarios = horarios;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observaciones = observaciones;
        this.activo = activo;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getMascotaId() { return mascotaId; }
    public String getNombreAlimento() { return nombreAlimento; }
    public String getTipoAlimento() { return tipoAlimento; }
    public String getAlimento() { return nombreAlimento; }
    public String getCantidad() { return cantidad; }
    public String getUnidad() { return unidad; }
    public int getVecesAlDia() { return vecesAlDia; }
    public String getHorarios() { return horarios; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public String getObservaciones() { return observaciones; }
    public boolean isActivo() { return activo; }
}
