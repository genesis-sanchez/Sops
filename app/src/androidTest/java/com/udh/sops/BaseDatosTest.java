package com.udh.sops;

import android.content.Context;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.MascotaDao;
import com.udh.sops.model.SopsDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.UUID;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BaseDatosTest {
    @Test
    public void guardarReabrirEditarYEliminarMascota() {
        Context contexto = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String nombreBase = "sops_prueba_" + UUID.randomUUID() + ".db";
        SopsDatabase base = Room.databaseBuilder(contexto, SopsDatabase.class, nombreBase).build();
        try {
            Mascota mascota = new Mascota("nube", "Gato", "mestizo", "2 años",
                    "Hembra", true, "15/11/2024", "4.5");
            long id = base.mascotaDao().insertar(mascota);
            assertTrue(id > 0);
            base.close();

            base = Room.databaseBuilder(contexto, SopsDatabase.class, nombreBase).build();
            MascotaDao dao = base.mascotaDao();
            Mascota guardada = dao.obtenerPorId(id);
            assertNotNull(guardada);
            assertEquals("nube", guardada.getNombre());
            assertEquals("15/11/2024", guardada.getFechaNacimiento());
            assertEquals("4.5", guardada.getPeso());
            assertTrue(guardada.isEsterilizado());
            assertEquals(1, dao.obtenerTodas().size());

            Mascota editada = new Mascota("luna", "Gato", "mestizo", "2 años",
                    "Hembra", true, "15/11/2024", "4.8");
            editada.setId(id);
            assertEquals(1, dao.actualizar(editada));
            assertEquals("luna", dao.obtenerPorId(id).getNombre());
            assertEquals("4.8", dao.obtenerPorId(id).getPeso());
            assertEquals(1, dao.eliminar(editada));
            assertNull(dao.obtenerPorId(id));
            assertTrue(dao.obtenerTodas().isEmpty());
        } finally {
            base.close();
            contexto.deleteDatabase(nombreBase);
        }
    }
}
