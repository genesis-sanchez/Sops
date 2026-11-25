package com.udh.sops.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface MedicamentoDao {
    @Insert
    long insertar(Medicamento medicamento);

    @Query("SELECT * FROM medicamentos WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<Medicamento> obtenerPorMascota(long mascotaId);

    @Query("SELECT * FROM medicamentos WHERE id = :id")
    Medicamento obtenerPorId(long id);

    @Update
    int actualizar(Medicamento medicamento);

    @Delete
    int eliminar(Medicamento medicamento);
}
