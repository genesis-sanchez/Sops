package com.udh.sops.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import androidx.core.content.IntentCompat;
import com.google.android.material.button.MaterialButton;
import com.udh.sops.R;
import com.udh.sops.model.*;
import java.util.Date;

public class RegistroMedicamentoActivity extends BaseActivity {
    public static final String EXTRA_EDITAR_MEDICAMENTO_ID = "com.udh.sops.extra.EDITAR_MEDICAMENTO_ID";
    private static final String EDICION_CARGADA = "medicamentoCargado";
    private long editarId;
    private Medicamento editar;
    private Mascota mascota;
    private EditText nombre, dosis, intervalo, inicio, fin, indicaciones;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editarId = getIntent().getLongExtra(EXTRA_EDITAR_MEDICAMENTO_ID, 0);
        prepararPantalla(R.layout.activity_registro_medicamento,
                editarId > 0 ? R.string.editar_medicamento : R.string.registrar_medicamento);
        mascota = IntentCompat.getSerializableExtra(getIntent(), DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.tvMascotaRegistroMedicamento)).setText(getString(R.string.medicamentos_de, mascota.getNombre()));
        nombre = findViewById(R.id.etNombreMedicamento);
        dosis = findViewById(R.id.etDosisMedicamento);
        intervalo = findViewById(R.id.etIntervaloMedicamento);
        inicio = findViewById(R.id.etInicioMedicamento);
        fin = findViewById(R.id.etFinMedicamento);
        indicaciones = findViewById(R.id.etIndicacionesMedicamento);
        findViewById(R.id.btnGuardarMedicamento).setOnClickListener(view -> guardar());
        if (editarId > 0) {
            ((MaterialButton) findViewById(R.id.btnGuardarMedicamento)).setText(R.string.guardar_cambios);
            findViewById(R.id.btnGuardarMedicamento).setEnabled(false);
            cargarMedicamento(savedInstanceState == null || !savedInstanceState.getBoolean(EDICION_CARGADA));
        }
    }
    private void cargarMedicamento(boolean rellenar) {
        new Thread(() -> {
            try {
                Medicamento guardado = SopsDatabase.obtenerInstancia(getApplicationContext()).medicamentoDao().obtenerPorId(editarId);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (guardado == null || guardado.getMascotaId() != mascota.getId()) {
                        Toast.makeText(this, R.string.medicamento_no_encontrado, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    editar = guardado;
                    if (rellenar) {
                        nombre.setText(guardado.getNombre());
                        dosis.setText(guardado.getDosis());
                        intervalo.setText(String.valueOf(guardado.getIntervaloHoras()));
                        inicio.setText(guardado.getFechaInicio());
                        fin.setText(guardado.getFechaFin());
                        indicaciones.setText(guardado.getIndicaciones());
                        ((CheckBox) findViewById(R.id.cbMedicamentoActivo)).setChecked(guardado.isActivo());
                    }
                    findViewById(R.id.btnGuardarMedicamento).setEnabled(true);
                });
            } catch (Exception exception) {
                Log.e("RegistroMedicamento", "No se pudo cargar el medicamento", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    TextView error = findViewById(R.id.tvErrorMedicamento);
                    error.setText(R.string.error_cargar_detalle_medicamento);
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
    private void guardar() {
        if (editarId > 0 && editar == null) return;
        boolean valido = obligatorio(nombre);
        valido = obligatorio(dosis) && valido;
        int horas = 0;
        try { horas = Integer.parseInt(intervalo.getText().toString().trim()); }
        catch (NumberFormatException exception) { /* Se muestra el error debajo del campo. */ }
        intervalo.setError(horas > 0 ? null : getString(R.string.error_intervalo_medicamento));
        Date desde = CuidadoPendiente.leerFecha(inicio.getText().toString());
        Date hasta = CuidadoPendiente.leerFecha(fin.getText().toString());
        inicio.setError(desde == null ? getString(R.string.error_fecha_medicamento) : null);
        fin.setError(hasta == null ? getString(R.string.error_fecha_medicamento) : null);
        if (desde != null && hasta != null && hasta.before(desde)) {
            fin.setError(getString(R.string.error_fin_medicamento));
            return;
        }
        if (!valido || horas <= 0 || desde == null || hasta == null) return;
        Medicamento registro = new Medicamento(mascota.getId(), editar == null ? null : editar.getVisitaId(), nombre.getText().toString().trim(),
                dosis.getText().toString().trim(), horas, inicio.getText().toString().trim(),
                fin.getText().toString().trim(), indicaciones.getText().toString().trim(),
                ((CheckBox) findViewById(R.id.cbMedicamentoActivo)).isChecked());
        MaterialButton boton = findViewById(R.id.btnGuardarMedicamento);
        if (!boton.isEnabled()) return;
        boton.setEnabled(false);
        boton.setText(R.string.guardando_medicamento);
        findViewById(R.id.tvErrorMedicamento).setVisibility(View.GONE);
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                if (editarId > 0) {
                    registro.setId(editarId);
                    if (base.medicamentoDao().actualizar(registro) != 1) throw new IllegalStateException("El medicamento ya no existe");
                } else base.medicamentoDao().insertar(registro);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, editarId > 0 ? R.string.medicamento_actualizado : R.string.medicamento_guardado, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroMedicamento", "No se pudo guardar el medicamento", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(editarId > 0 ? R.string.guardar_cambios : R.string.guardar_medicamento);
                    ((TextView) findViewById(R.id.tvErrorMedicamento)).setText(R.string.error_guardar_medicamento);
                    findViewById(R.id.tvErrorMedicamento).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDICION_CARGADA, editar != null);
        super.onSaveInstanceState(outState);
    }
}
