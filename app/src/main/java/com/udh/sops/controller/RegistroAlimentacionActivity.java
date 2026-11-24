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
    private EditText alimento, cantidad, horarios, observaciones;
    private Spinner frecuencia, tipo;
    private Mascota mascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_registro_alimentacion, R.string.registrar_alimentacion);
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
    }

    private boolean obligatorio(EditText campo) {
        boolean completo = !campo.getText().toString().trim().isEmpty();
        campo.setError(completo ? null : getString(R.string.error_obligatorio));
        return completo;
    }

    private void enviar() {
        boolean completo = obligatorio(alimento);
        completo = obligatorio(cantidad) && completo;
        completo = obligatorio(horarios) && completo;
        ((TextView) findViewById(R.id.tvFrecuenciaAlimento)).setError(
                frecuencia.getSelectedItemPosition() == 0 ? getString(R.string.error_obligatorio) : null);
        ((TextView) findViewById(R.id.tvTipoAlimento)).setError(
                tipo.getSelectedItemPosition() == 0 ? getString(R.string.error_obligatorio) : null);
        if (!completo || frecuencia.getSelectedItemPosition() == 0 || tipo.getSelectedItemPosition() == 0) return;
        String notas = observaciones.getText().toString().trim();
        String inicio = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(new Date());
        Alimentacion registro = new Alimentacion(mascota.getId(), alimento.getText().toString().trim(),
                tipo.getSelectedItem().toString(), cantidad.getText().toString().trim(), null,
                frecuencia.getSelectedItemPosition(), horarios.getText().toString().trim(),
                inicio, null, notas, true);
        MaterialButton boton = findViewById(R.id.btnEnviarAlimentacion);
        if (!boton.isEnabled()) return;
        boton.setEnabled(false);
        boton.setText(R.string.guardando_alimentacion);
        findViewById(R.id.tvErrorAlimentacion).setVisibility(View.GONE);
        new Thread(() -> {
            try {
                SopsDatabase.obtenerInstancia(getApplicationContext()).alimentacionDao().insertar(registro);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, R.string.alimentacion_guardada, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroAlimentacion", "No se pudo guardar la alimentacion", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(R.string.guardar_alimentacion);
                    findViewById(R.id.tvErrorAlimentacion).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
}
