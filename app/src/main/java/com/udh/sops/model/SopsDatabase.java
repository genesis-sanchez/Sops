package com.udh.sops.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Mascota.class}, version = 1, exportSchema = true)
public abstract class SopsDatabase extends RoomDatabase {
    private static SopsDatabase instancia;

    public abstract MascotaDao mascotaDao();

    public static synchronized SopsDatabase obtenerInstancia(Context contexto) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(contexto.getApplicationContext(),
                    SopsDatabase.class, "sops.db").build();
        }
        return instancia;
    }
}
