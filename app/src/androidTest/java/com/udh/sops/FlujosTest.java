package com.udh.sops;

import android.content.Context;
import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.udh.sops.controller.*;
import com.udh.sops.model.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class FlujosTest {
    private Context contexto() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    private void abrir(Class<?> pantalla) {
        Intent intent = new Intent(contexto(), pantalla);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, DataSource.getMascotasEjemplo().get(0));
        InstrumentationRegistry.getInstrumentation().startActivitySync(intent);
    }

    private void escribir(int id, String texto) {
        onView(withId(id)).perform(scrollTo(), replaceText(texto), closeSoftKeyboard());
    }

    private void elegir(int id, String texto) {
        onView(withId(id)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(texto))).perform(click());
    }

    @Test public void navegarDesdeInicio() {
        abrir(MainActivity.class);
        onView(withId(R.id.btnVerMascotas)).perform(scrollTo(), click());
        onView(withText("Firulais")).perform(click());
        onView(withId(R.id.tvNombreDetalle)).check(matches(withText("Firulais")));
        onView(withId(R.id.btnVacunasMascota)).perform(scrollTo(), click());
        onView(withId(R.id.tvMascotaVacunas)).check(matches(withText("Vacunas de Firulais")));
        onView(withId(R.id.btnRegistrarVacuna)).perform(click());
        onView(withId(R.id.etNombreVacuna)).check(matches(isDisplayed()));
        pressBack();
        pressBack();
        onView(withId(R.id.btnHistorialMascota)).perform(scrollTo(), click());
        onView(withId(R.id.btnRegistrarVisita)).perform(click());
        onView(withId(R.id.etFechaVisita)).check(matches(isDisplayed()));
        pressBack();
        pressBack();
        onView(withId(R.id.btnAlimentacionMascota)).perform(scrollTo(), click());
        onView(withId(R.id.btnRegistrarAlimentacion)).perform(click());
        onView(withId(R.id.etAlimento)).check(matches(isDisplayed()));
        openActionBarOverflowOrOptionsMenu(contexto());
        onView(withText(R.string.consejos_titulo)).perform(click());
        onView(withText(R.string.consejos_perros)).check(matches(isDisplayed()));
        openActionBarOverflowOrOptionsMenu(contexto());
        onView(withText(R.string.acerca_titulo)).perform(click());
        onView(withText(R.string.acerca_lema)).check(matches(isDisplayed()));
        openActionBarOverflowOrOptionsMenu(contexto());
        onView(withText(R.string.menu_inicio)).perform(click());
        onView(withId(R.id.tvBienvenida)).check(matches(isDisplayed()));
    }

    @Test public void validarRegistroMascotaVacio() {
        abrir(RegistroMascotaActivity.class);
        onView(withId(R.id.btnGuardarMascota)).perform(scrollTo(), click());
        onView(withId(R.id.etNombre)).check(matches(hasErrorText(contexto().getString(R.string.error_obligatorio))));
        onView(withId(R.id.etRaza)).check(matches(hasErrorText(contexto().getString(R.string.error_obligatorio))));
    }

    @Test public void enviarVacuna() {
        abrir(RegistroVacunaActivity.class);
        onView(withId(R.id.btnEnviarVacuna)).perform(scrollTo(), click());
        onView(withId(R.id.etNombreVacuna)).check(matches(hasErrorText(contexto().getString(R.string.error_obligatorio))));
        escribir(R.id.etNombreVacuna, "antirrabica");
        elegir(R.id.spDosis, "1");
        escribir(R.id.etFechaAplicacion, "10/11/2026");
        escribir(R.id.etProximaFecha, "10/11/2027");
        escribir(R.id.etVeterinario, "veterinaria patitas");
        onView(withId(R.id.btnEnviarVacuna)).perform(scrollTo(), click());
        onView(withText(R.string.vacuna_confirmacion_titulo)).check(matches(isDisplayed()));
        onView(withId(android.R.id.message)).check(matches(withText(containsString("Firulais"))));
    }

    @Test public void registrarMascotaYConsultarSusPantallas() {
        abrir(RegistroMascotaActivity.class);
        escribir(R.id.etNombre, "nube");
        elegir(R.id.spEspecie, "Perro");
        escribir(R.id.etRaza, "mestizo");
        escribir(R.id.etPeso, "0");
        onView(withId(R.id.rbHembra)).perform(scrollTo(), click());
        onView(withId(R.id.btnFechaNacimiento)).perform(scrollTo(), click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.btnGuardarMascota)).perform(scrollTo(), click());
        onView(withId(R.id.etPeso)).check(matches(hasErrorText(contexto().getString(R.string.error_peso))));
        escribir(R.id.etPeso, "4.5");
        onView(withId(R.id.btnGuardarMascota)).perform(scrollTo(), click());
        onView(withId(R.id.tvNombreDetalle)).check(matches(withText("nube")));
        onView(withId(R.id.btnVacunasMascota)).perform(scrollTo(), click());
        onView(withId(R.id.tvSinVacunas)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.btnHistorialMascota)).perform(scrollTo(), click());
        onView(withId(R.id.tvSinHistorial)).check(matches(isDisplayed()));
        onView(withId(R.id.btnRegistrarVisita)).perform(click());
        onView(withId(R.id.tvMascotaVisita)).check(matches(withText(containsString("nube"))));
        pressBack();
        pressBack();
        onView(withId(R.id.btnAlimentacionMascota)).perform(scrollTo(), click());
        onView(withId(R.id.tvSinAlimentacion)).check(matches(isDisplayed()));
        onView(withId(R.id.btnRegistrarAlimentacion)).perform(click());
        onView(withId(R.id.tvMascotaRegistroAlimentacion)).check(matches(withText(containsString("nube"))));
    }

    @Test public void enviarVisita() {
        abrir(RegistroVisitaActivity.class);
        onView(withId(R.id.btnEnviarVisita)).perform(scrollTo(), click());
        onView(withId(R.id.etDiagnostico)).check(matches(hasErrorText(contexto().getString(R.string.error_obligatorio))));
        escribir(R.id.etFechaVisita, "15/11/2026");
        elegir(R.id.spMotivoVisita, "revision general");
        escribir(R.id.etDiagnostico, "revision de rutina");
        escribir(R.id.etTratamiento, "sin tratamiento");
        escribir(R.id.etVeterinarioVisita, "veterinaria patitas");
        onView(withId(R.id.btnEnviarVisita)).perform(scrollTo(), click());
        onView(withText(R.string.resumen_visita)).check(matches(isDisplayed()));
    }

    @Test public void enviarAlimentacion() {
        abrir(RegistroAlimentacionActivity.class);
        onView(withId(R.id.btnEnviarAlimentacion)).perform(scrollTo(), click());
        onView(withId(R.id.etAlimento)).check(matches(hasErrorText(contexto().getString(R.string.error_obligatorio))));
        escribir(R.id.etAlimento, "alimento seco");
        escribir(R.id.etCantidadAlimento, "1 porcion");
        escribir(R.id.etHorariosAlimento, "08:00 y 18:00");
        elegir(R.id.spFrecuenciaAlimento, "diaria");
        onView(withId(R.id.btnEnviarAlimentacion)).perform(scrollTo(), click());
        onView(withText(R.string.resumen_alimentacion)).check(matches(isDisplayed()));
    }
}
