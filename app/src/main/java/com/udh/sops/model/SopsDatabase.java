package com.udh.sops.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.annotation.NonNull;

@Database(entities = {Mascota.class, Vacuna.class, VisitaVeterinaria.class, Alimentacion.class, Medicamento.class, Recordatorio.class},
        version = 8, exportSchema = true)
public abstract class SopsDatabase extends RoomDatabase {
    private static SopsDatabase instancia;

    public abstract MascotaDao mascotaDao();
    public abstract MedicamentoDao medicamentoDao();
    public abstract RecordatorioDao recordatorioDao();
    public abstract VacunaDao vacunaDao();
    public abstract VisitaVeterinariaDao visitaVeterinariaDao();
    public abstract AlimentacionDao alimentacionDao();

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

    public static final Migration MIGRACION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase base) {
            base.execSQL("CREATE TABLE IF NOT EXISTS visitas_veterinarias ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "mascotaId INTEGER NOT NULL, fecha TEXT, motivo TEXT, diagnostico TEXT, "
                    + "tratamiento TEXT, veterinario TEXT, clinica TEXT, peso TEXT, "
                    + "proximaRevision TEXT, observaciones TEXT, "
                    + "FOREIGN KEY(mascotaId) REFERENCES mascotas(id) "
                    + "ON UPDATE NO ACTION ON DELETE CASCADE)");
            base.execSQL("CREATE INDEX IF NOT EXISTS index_visitas_veterinarias_mascotaId "
                    + "ON visitas_veterinarias(mascotaId)");
        }
    };

    public static final Migration MIGRACION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase base) {
            base.execSQL("CREATE TABLE IF NOT EXISTS alimentacion ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "mascotaId INTEGER NOT NULL, nombreAlimento TEXT, tipoAlimento TEXT, "
                    + "cantidad TEXT, unidad TEXT, vecesAlDia INTEGER NOT NULL, horarios TEXT, "
                    + "fechaInicio TEXT, fechaFin TEXT, observaciones TEXT, activo INTEGER NOT NULL, "
                    + "FOREIGN KEY(mascotaId) REFERENCES mascotas(id) "
                    + "ON UPDATE NO ACTION ON DELETE CASCADE)");
            base.execSQL("CREATE INDEX IF NOT EXISTS index_alimentacion_mascotaId "
                    + "ON alimentacion(mascotaId)");
        }
    };

    public static final Migration MIGRACION_5_6 = new Migration(5, 6) {
        @Override public void migrate(@NonNull SupportSQLiteDatabase base) {
            base.execSQL("ALTER TABLE mascotas ADD COLUMN fotoUri TEXT");
        }
    };

    public static final Migration MIGRACION_6_7 = new Migration(6, 7) {
        @Override public void migrate(@NonNull SupportSQLiteDatabase base) {
            base.execSQL("CREATE TABLE IF NOT EXISTS medicamentos ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, mascotaId INTEGER NOT NULL, "
                    + "visitaId INTEGER, nombre TEXT, dosis TEXT, intervaloHoras INTEGER NOT NULL, "
                    + "fechaInicio TEXT, fechaFin TEXT, indicaciones TEXT, activo INTEGER NOT NULL, "
                    + "FOREIGN KEY(mascotaId) REFERENCES mascotas(id) ON UPDATE NO ACTION ON DELETE CASCADE, "
                    + "FOREIGN KEY(visitaId) REFERENCES visitas_veterinarias(id) ON UPDATE NO ACTION ON DELETE SET NULL)");
            base.execSQL("CREATE INDEX IF NOT EXISTS index_medicamentos_mascotaId ON medicamentos(mascotaId)");
            base.execSQL("CREATE INDEX IF NOT EXISTS index_medicamentos_visitaId ON medicamentos(visitaId)");
        }
    };

    public static final Migration MIGRACION_7_8 = new Migration(7, 8) {
        @Override public void migrate(@NonNull SupportSQLiteDatabase base) {
            base.execSQL("CREATE TABLE IF NOT EXISTS recordatorios ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, mascotaId INTEGER NOT NULL, "
                    + "titulo TEXT, descripcion TEXT, fechaHora INTEGER NOT NULL, activo INTEGER NOT NULL, "
                    + "FOREIGN KEY(mascotaId) REFERENCES mascotas(id) ON UPDATE NO ACTION ON DELETE CASCADE)");
            base.execSQL("CREATE INDEX IF NOT EXISTS index_recordatorios_mascotaId ON recordatorios(mascotaId)");
        }
    };

    public static synchronized SopsDatabase obtenerInstancia(Context contexto) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(contexto.getApplicationContext(),
                    SopsDatabase.class, "sops.db")
                    .addMigrations(MIGRACION_1_2, MIGRACION_2_3, MIGRACION_3_4, MIGRACION_4_5, MIGRACION_5_6, MIGRACION_6_7, MIGRACION_7_8).build();
        }
        return instancia;
    }
}
