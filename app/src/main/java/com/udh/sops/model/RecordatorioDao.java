package com.udh.sops.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface RecordatorioDao {
    @Insert
    long insertar(Recordatorio recordatorio);

    @Query("SELECT * FROM recordatorios WHERE mascotaId = :mascotaId ORDER BY fechaHora ASC")
    List<Recordatorio> obtenerPorMascota(long mascotaId);

    @Query("SELECT * FROM recordatorios WHERE id = :id")
    Recordatorio obtenerPorId(long id);

    @Query("SELECT * FROM recordatorios WHERE activo = 1 ORDER BY fechaHora ASC")
    List<Recordatorio> obtenerActivos();

    @Update
    int actualizar(Recordatorio recordatorio);

    @Delete
    int eliminar(Recordatorio recordatorio);
}
