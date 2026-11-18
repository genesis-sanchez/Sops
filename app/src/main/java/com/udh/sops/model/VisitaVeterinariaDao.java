package com.udh.sops.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface VisitaVeterinariaDao {
    @Insert
    long insertar(VisitaVeterinaria visita);

    @Query("SELECT * FROM visitas_veterinarias WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<VisitaVeterinaria> obtenerPorMascota(long mascotaId);

    @Query("SELECT * FROM visitas_veterinarias WHERE id = :id")
    VisitaVeterinaria obtenerPorId(long id);

    @Update
    int actualizar(VisitaVeterinaria visita);

    @Delete
    int eliminar(VisitaVeterinaria visita);
}
