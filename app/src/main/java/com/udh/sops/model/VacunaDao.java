package com.udh.sops.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface VacunaDao {
    @Insert
    long insertar(Vacuna vacuna);

    @Query("SELECT * FROM vacunas ORDER BY id DESC")
    List<Vacuna> obtenerTodas();

    @Query("SELECT * FROM vacunas WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<Vacuna> obtenerPorMascota(long mascotaId);

    @Query("SELECT * FROM vacunas WHERE id = :id")
    Vacuna obtenerPorId(long id);

    @Update
    int actualizar(Vacuna vacuna);

    @Delete
    int eliminar(Vacuna vacuna);
}
