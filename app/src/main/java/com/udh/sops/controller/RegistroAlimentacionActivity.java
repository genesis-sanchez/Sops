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
import com.udh.sops.model.Alimentacion;
import com.udh.sops.model.SopsDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroAlimentacionActivity extends BaseActivity {
    public static final String EXTRA_EDITAR_ALIMENTACION_ID = "com.udh.sops.extra.EDITAR_ALIMENTACION_ID";
    private static final String ESTADO_EDICION_CARGADA = "edicionAlimentacionCargada";
    private long registroEditarId;
    private Alimentacion registroEditar;
    private EditText alimento, cantidad, horarios, observaciones;
    private Spinner frecuencia, tipo;
    private Mascota mascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registroEditarId = getIntent().getLongExtra(EXTRA_EDITAR_ALIMENTACION_ID, 0);
        prepararPantalla(R.layout.activity_registro_alimentacion, registroEditarId > 0
                ? R.string.editar_alimentacion : R.string.registrar_alimentacion);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.tvMascotaRegistroAlimentacion)).setText(
                getString(R.string.alimentacion_de, mascota.getNombre()));
        alimento = findViewById(R.id.etAlimento);
        cantidad = findViewById(R.id.etCantidadAlimento);
        horarios = findViewById(R.id.etHorariosAlimento);
        observaciones = findViewById(R.id.etObservacionesAlimento);
        frecuencia = findViewById(R.id.spFrecuenciaAlimento);
        tipo = findViewById(R.id.spTipoAlimento);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.veces_alimento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frecuencia.setAdapter(adapter);
        ArrayAdapter<CharSequence> tipos = ArrayAdapter.createFromResource(this,
                R.array.tipos_alimento, android.R.layout.simple_spinner_item);
        tipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(tipos);
        findViewById(R.id.btnEnviarAlimentacion).setOnClickListener(view -> enviar());
        if (registroEditarId > 0) {
            ((MaterialButton) findViewById(R.id.btnEnviarAlimentacion)).setText(R.string.guardar_cambios);
            findViewById(R.id.btnEnviarAlimentacion).setEnabled(false);
            cargarRegistro(savedInstanceState == null || !savedInstanceState.getBoolean(ESTADO_EDICION_CARGADA));
        }
    }

    private void cargarRegistro(boolean rellenarCampos) {
        new Thread(() -> {
            try {
                Alimentacion guardado = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .alimentacionDao().obtenerPorId(registroEditarId);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (guardado == null || guardado.getMascotaId() != mascota.getId()) {
                        Toast.makeText(this, R.string.alimentacion_no_encontrada, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    registroEditar = guardado;
                    if (rellenarCampos) {
                        alimento.setText(guardado.getNombreAlimento());
                        cantidad.setText(guardado.getCantidad());
                        horarios.setText(guardado.getHorarios());
                        observaciones.setText(guardado.getObservaciones());
                        int veces = guardado.getVecesAlDia();
                        frecuencia.setSelection(veces > 0 && veces < frecuencia.getCount() ? veces : 0);
                        for (int i = 0; i < tipo.getCount(); i++) {
                            if (tipo.getItemAtPosition(i).equals(guardado.getTipoAlimento())) tipo.setSelection(i);
                        }
                    }
                    findViewById(R.id.btnEnviarAlimentacion).setEnabled(true);
                });
            } catch (Exception exception) {
                Log.e("RegistroAlimentacion", "No se pudo cargar el registro para editar", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    TextView error = findViewById(R.id.tvErrorAlimentacion);
                    error.setText(R.string.error_cargar_detalle_alimentacion);
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
        if (registroEditarId > 0 && registroEditar == null) return;
        boolean completo = obligatorio(alimento);
        completo = obligatorio(cantidad) && completo;
        completo = obligatorio(horarios) && completo;
        ((TextView) findViewById(R.id.tvFrecuenciaAlimento)).setError(
                frecuencia.getSelectedItemPosition() == 0 ? getString(R.string.error_obligatorio) : null);
        ((TextView) findViewById(R.id.tvTipoAlimento)).setError(
                tipo.getSelectedItemPosition() == 0 ? getString(R.string.error_obligatorio) : null);
        if (!completo || frecuencia.getSelectedItemPosition() == 0 || tipo.getSelectedItemPosition() == 0) return;
        String notas = observaciones.getText().toString().trim();
        String inicio = registroEditar == null
                ? new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(new Date()) : registroEditar.getFechaInicio();
        Alimentacion registro = new Alimentacion(mascota.getId(), alimento.getText().toString().trim(),
                tipo.getSelectedItem().toString(), cantidad.getText().toString().trim(),
                registroEditar == null ? null : registroEditar.getUnidad(),
                frecuencia.getSelectedItemPosition(), horarios.getText().toString().trim(),
                inicio, registroEditar == null ? null : registroEditar.getFechaFin(),
                notas, registroEditar == null || registroEditar.isActivo());
        MaterialButton boton = findViewById(R.id.btnEnviarAlimentacion);
        if (!boton.isEnabled()) return;
        boton.setEnabled(false);
        boton.setText(R.string.guardando_alimentacion);
        findViewById(R.id.tvErrorAlimentacion).setVisibility(View.GONE);
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                if (registroEditarId > 0) {
                    registro.setId(registroEditarId);
                    if (base.alimentacionDao().actualizar(registro) != 1) {
                        throw new IllegalStateException("El registro ya no existe");
                    }
                } else {
                    base.alimentacionDao().insertar(registro);
                }
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, registroEditarId > 0 ? R.string.alimentacion_actualizada
                            : R.string.alimentacion_guardada, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroAlimentacion", "No se pudo guardar la alimentacion", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(registroEditarId > 0 ? R.string.guardar_cambios : R.string.guardar_alimentacion);
                    ((TextView) findViewById(R.id.tvErrorAlimentacion)).setText(R.string.error_guardar_alimentacion);
                    findViewById(R.id.tvErrorAlimentacion).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ESTADO_EDICION_CARGADA, registroEditar != null);
        super.onSaveInstanceState(outState);
    }
}
