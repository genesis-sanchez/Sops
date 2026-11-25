package com.udh.sops.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recordatorios",
        foreignKeys = @ForeignKey(entity = Mascota.class, parentColumns = "id",
                childColumns = "mascotaId", onDelete = ForeignKey.CASCADE),
        indices = @Index("mascotaId"))
public class Recordatorio {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private final long mascotaId;
    private final String titulo;
    private final String descripcion;
    private final long fechaHora;
    private final boolean activo;

    public Recordatorio(long mascotaId, String titulo, String descripcion, long fechaHora, boolean activo) {
        this.mascotaId = mascotaId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.activo = activo;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getMascotaId() { return mascotaId; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public long getFechaHora() { return fechaHora; }
    public boolean isActivo() { return activo; }
}
