package com.udh.sops.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface MascotaDao {
    @Insert
    long insertar(Mascota mascota);

    @Query("SELECT * FROM mascotas ORDER BY id DESC")
    List<Mascota> obtenerTodas();

    @Query("SELECT * FROM mascotas WHERE id = :id")
    Mascota obtenerPorId(long id);

    @Update
    int actualizar(Mascota mascota);

    @Delete
    int eliminar(Mascota mascota);
}
