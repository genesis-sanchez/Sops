package com.udh.sops.controller;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.core.content.IntentCompat;
import com.google.android.material.button.MaterialButton;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;
import com.udh.sops.model.VisitaVeterinaria;
import java.util.ArrayList;
import java.util.Arrays;

public class RegistroVisitaActivity extends BaseActivity {
    public static final String EXTRA_EDITAR_VISITA_ID = "com.udh.sops.extra.EDITAR_VISITA_ID";
    private static final String ESTADO_EDICION_CARGADA = "edicionVisitaCargada";
    private EditText fecha, diagnostico, tratamiento, veterinario, proximaRevision;
    private Spinner motivo;
    private ArrayAdapter<String> motivos;
    private Mascota mascota;
    private long visitaEditarId;
    private VisitaVeterinaria visitaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitaEditarId = getIntent().getLongExtra(EXTRA_EDITAR_VISITA_ID, 0);
        prepararPantalla(R.layout.activity_registro_visita,
                visitaEditarId > 0 ? R.string.editar_visita : R.string.registrar_visita);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.tvMascotaVisita)).setText(
                getString(R.string.visita_de, mascota.getNombre()));
        fecha = findViewById(R.id.etFechaVisita);
        diagnostico = findViewById(R.id.etDiagnostico);
        tratamiento = findViewById(R.id.etTratamiento);
        veterinario = findViewById(R.id.etVeterinarioVisita);
        proximaRevision = findViewById(R.id.etProximaRevision);
        motivo = findViewById(R.id.spMotivoVisita);
        motivos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.motivos_visita))));
        motivos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        motivo.setAdapter(motivos);
        findViewById(R.id.btnEnviarVisita).setOnClickListener(view -> enviar());
        if (visitaEditarId > 0) {
            ((MaterialButton) findViewById(R.id.btnEnviarVisita)).setText(R.string.guardar_cambios);
            findViewById(R.id.btnEnviarVisita).setEnabled(false);
            cargarVisita(savedInstanceState == null
                    || !savedInstanceState.getBoolean(ESTADO_EDICION_CARGADA));
        }
    }

    private void cargarVisita(boolean rellenarCampos) {
        new Thread(() -> {
            try {
                VisitaVeterinaria guardada = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .visitaVeterinariaDao().obtenerPorId(visitaEditarId);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (guardada == null || guardada.getMascotaId() != mascota.getId()) {
                        Toast.makeText(this, R.string.visita_no_encontrada, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    visitaEditar = guardada;
                    if (guardada.getMotivo() != null && motivos.getPosition(guardada.getMotivo()) < 0) {
                        motivos.add(guardada.getMotivo());
                    }
                    if (rellenarCampos) {
                        fecha.setText(guardada.getFecha());
                        diagnostico.setText(guardada.getDiagnostico());
                        tratamiento.setText(guardada.getTratamiento());
                        veterinario.setText(guardada.getVeterinario());
                        proximaRevision.setText(guardada.getProximaRevision());
                        int posicion = motivos.getPosition(guardada.getMotivo());
                        motivo.setSelection(Math.max(0, posicion));
                    }
                    findViewById(R.id.btnEnviarVisita).setEnabled(true);
                });
            } catch (Exception exception) {
                Log.e("RegistroVisita", "No se pudo cargar la visita para editar", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    TextView error = findViewById(R.id.tvErrorVisita);
                    error.setText(R.string.error_cargar_detalle_visita);
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
        if (visitaEditarId > 0 && visitaEditar == null) return;
        boolean completo = obligatorio(fecha);
        completo = obligatorio(diagnostico) && completo;
        completo = obligatorio(tratamiento) && completo;
        completo = obligatorio(veterinario) && completo;
        TextView errorMotivo = findViewById(R.id.tvMotivoVisita);
        errorMotivo.setError(motivo.getSelectedItemPosition() == 0
                ? getString(R.string.error_obligatorio) : null);
        if (!completo || motivo.getSelectedItemPosition() == 0) return;

        VisitaVeterinaria visita = new VisitaVeterinaria(mascota.getId(),
                fecha.getText().toString().trim(), motivo.getSelectedItem().toString(),
                diagnostico.getText().toString().trim(), tratamiento.getText().toString().trim(),
                veterinario.getText().toString().trim(),
                visitaEditar == null ? null : visitaEditar.getClinica(),
                visitaEditar == null ? null : visitaEditar.getPeso(),
                proximaRevision.getText().toString().trim(),
                visitaEditar == null ? null : visitaEditar.getObservaciones());
        MaterialButton boton = findViewById(R.id.btnEnviarVisita);
        if (!boton.isEnabled()) return;
        boton.setEnabled(false);
        boton.setText(R.string.guardando_visita);
        findViewById(R.id.tvErrorVisita).setVisibility(View.GONE);
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                if (visitaEditarId > 0) {
                    visita.setId(visitaEditarId);
                    if (base.visitaVeterinariaDao().actualizar(visita) != 1) {
                        throw new IllegalStateException("La visita ya no existe");
                    }
                } else {
                    base.visitaVeterinariaDao().insertar(visita);
                }
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, visitaEditarId > 0 ? R.string.visita_actualizada
                            : R.string.visita_guardada, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroVisita", "No se pudo guardar la visita", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(visitaEditarId > 0 ? R.string.guardar_cambios : R.string.guardar_visita);
                    TextView error = findViewById(R.id.tvErrorVisita);
                    error.setText(R.string.error_guardar_visita);
                    error.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ESTADO_EDICION_CARGADA, visitaEditar != null);
        super.onSaveInstanceState(outState);
    }
}
