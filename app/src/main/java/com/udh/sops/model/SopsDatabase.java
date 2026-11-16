package com.udh.sops.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.annotation.NonNull;

@Database(entities = {Mascota.class, Vacuna.class}, version = 3, exportSchema = true)
public abstract class SopsDatabase extends RoomDatabase {
    private static SopsDatabase instancia;

    public abstract MascotaDao mascotaDao();
    public abstract VacunaDao vacunaDao();

    public static final Migration MIGRACION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase base) {
            base.execSQL("CREATE TABLE IF NOT EXISTS vacunas ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "nombre TEXT, dosis INTEGER NOT NULL, "
                    + "fecha TEXT, proximaFecha TEXT, aplicada INTEGER NOT NULL, "
                    + "veterinario TEXT, observaciones TEXT, recordatorio INTEGER NOT NULL)");
        }
    };

    public static final Migration MIGRACION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase base) {
            base.execSQL("ALTER TABLE vacunas ADD COLUMN mascotaId INTEGER "
                    + "REFERENCES mascotas(id) ON UPDATE NO ACTION ON DELETE CASCADE");
            base.execSQL("CREATE INDEX IF NOT EXISTS index_vacunas_mascotaId ON vacunas(mascotaId)");
        }
    };

    public static synchronized SopsDatabase obtenerInstancia(Context contexto) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(contexto.getApplicationContext(),
                    SopsDatabase.class, "sops.db")
                    .addMigrations(MIGRACION_1_2, MIGRACION_2_3).build();
        }
        return instancia;
    }
}
