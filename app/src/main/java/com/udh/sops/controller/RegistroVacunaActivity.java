package com.udh.sops.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.core.content.IntentCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.Vacuna;
import com.udh.sops.model.SopsDatabase;

public class RegistroVacunaActivity extends BaseActivity {
    public static final String EXTRA_EDITAR_VACUNA_ID = "com.udh.sops.extra.EDITAR_VACUNA_ID";
    private static final String ESTADO_EDICION_CARGADA = "edicionCargada";
    private EditText nombre, aplicacion, proxima, veterinario, observaciones;
    private Spinner dosis;
    private TextView error;
    private Mascota mascota;
    private long vacunaEditarId;
    private Vacuna vacunaEditar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vacunaEditarId = getIntent().getLongExtra(EXTRA_EDITAR_VACUNA_ID, 0);
        prepararPantalla(R.layout.activity_registro_vacuna, vacunaEditarId > 0
                ? R.string.editar_vacuna : R.string.registrar_vacuna);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.tvMascotaRegistroVacuna)).setText(
                getString(R.string.vacunas_de, mascota.getNombre()));
        nombre = findViewById(R.id.etNombreVacuna);
        aplicacion = findViewById(R.id.etFechaAplicacion);
        proxima = findViewById(R.id.etProximaFecha);
        veterinario = findViewById(R.id.etVeterinario);
        observaciones = findViewById(R.id.etObservacionesVacuna);
        dosis = findViewById(R.id.spDosis);
        error = findViewById(R.id.tvErrorVacuna);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dosis_vacuna, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosis.setAdapter(adapter);
        findViewById(R.id.btnEnviarVacuna).setOnClickListener(view -> enviar());
        if (vacunaEditarId > 0) {
            ((MaterialButton) findViewById(R.id.btnEnviarVacuna)).setText(R.string.guardar_cambios);
            findViewById(R.id.btnEnviarVacuna).setEnabled(false);
            ((TextView) findViewById(R.id.tvAvisoFormularioVacuna)).setText(R.string.editar_vacuna_aviso);
            cargarVacuna(savedInstanceState == null
                    || !savedInstanceState.getBoolean(ESTADO_EDICION_CARGADA));
        }
    }

    private void cargarVacuna(boolean rellenarCampos) {
        new Thread(() -> {
            try {
                Vacuna guardada = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .vacunaDao().obtenerPorId(vacunaEditarId);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (guardada == null || guardada.getMascotaId() == null
                            || guardada.getMascotaId() != mascota.getId()) {
                        Toast.makeText(this, R.string.vacuna_no_encontrada, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    vacunaEditar = guardada;
                    if (rellenarCampos) {
                        nombre.setText(guardada.getNombre());
                        aplicacion.setText(guardada.getFecha());
                        proxima.setText(guardada.getProximaFecha());
                        veterinario.setText(guardada.getVeterinario());
                        observaciones.setText(guardada.getObservaciones());
                        int numero = guardada.getDosis();
                        dosis.setSelection(numero > 0 && numero < dosis.getCount() ? numero : 0);
                        ((MaterialSwitch) findViewById(R.id.swRecordatorioVacuna)).setChecked(guardada.isRecordatorio());
                    }
                    findViewById(R.id.btnEnviarVacuna).setEnabled(true);
                });
            } catch (Exception exception) {
                Log.e("RegistroVacuna", "No se pudo cargar la vacuna para editar", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    error.setText(R.string.error_cargar_detalle_vacuna);
                    error.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private boolean obligatorio(EditText campo) {
        boolean completo = !campo.getText().toString().trim().isEmpty();
        campo.setError(completo ? null : getString(R.string.error_obligatorio));
        return completo;
    }

    private void enviar() {
        if (vacunaEditarId > 0 && vacunaEditar == null) return;
        boolean completo = obligatorio(nombre);
        completo = obligatorio(aplicacion) && completo;
        completo = obligatorio(proxima) && completo;
        completo = obligatorio(veterinario) && completo;
        if (!completo || dosis.getSelectedItemPosition() == 0) {
            error.setText(R.string.error_form_vacuna);
            error.setVisibility(View.VISIBLE);
            return;
        }
        error.setVisibility(View.GONE);
        String notas = observaciones.getText().toString().trim();
        boolean recordar = ((MaterialSwitch) findViewById(R.id.swRecordatorioVacuna)).isChecked();
        Vacuna vacuna = new Vacuna(mascota.getId(), nombre.getText().toString().trim(),
                dosis.getSelectedItemPosition(), aplicacion.getText().toString().trim(),
                proxima.getText().toString().trim(), vacunaEditar == null || vacunaEditar.isAplicada(),
                veterinario.getText().toString().trim(), notas, recordar);
        guardarVacuna(vacuna);
    }

    private void guardarVacuna(Vacuna vacuna) {
        MaterialButton boton = findViewById(R.id.btnEnviarVacuna);
        if (!boton.isEnabled()) return;
        boton.setEnabled(false);
        boton.setText(R.string.guardando_vacuna);
        new Thread(() -> {
            try {
                if (vacunaEditarId > 0) {
                    vacuna.setId(vacunaEditarId);
                    int cambios = SopsDatabase.obtenerInstancia(getApplicationContext())
                            .vacunaDao().actualizar(vacuna);
                    if (cambios != 1) throw new IllegalStateException("La vacuna ya no existe");
                } else {
                    SopsDatabase.obtenerInstancia(getApplicationContext()).vacunaDao().insertar(vacuna);
                }
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, vacunaEditarId > 0 ? R.string.vacuna_actualizada
                            : R.string.vacuna_guardada, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroVacuna", "No se pudo guardar la vacuna", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(vacunaEditarId > 0 ? R.string.guardar_cambios : R.string.guardar_vacuna);
                    error.setText(R.string.error_guardar_vacuna);
                    error.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ESTADO_EDICION_CARGADA, vacunaEditar != null);
        super.onSaveInstanceState(outState);
    }
}
