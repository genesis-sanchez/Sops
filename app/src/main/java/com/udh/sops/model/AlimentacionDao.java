package com.udh.sops.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AlimentacionDao {
    @Insert
    long insertar(Alimentacion alimentacion);

    @Query("SELECT * FROM alimentacion WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<Alimentacion> obtenerPorMascota(long mascotaId);

    @Query("SELECT * FROM alimentacion WHERE id = :id")
    Alimentacion obtenerPorId(long id);

    @Update
    int actualizar(Alimentacion alimentacion);

    @Delete
    int eliminar(Alimentacion alimentacion);
}
