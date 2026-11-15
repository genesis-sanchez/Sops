package com.udh.sops.controller;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.core.content.IntentCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;

public class RegistroAlimentacionActivity extends BaseActivity {
    private EditText alimento, cantidad, horarios, observaciones;
    private Spinner frecuencia;
    private Mascota mascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_registro_alimentacion, R.string.registrar_alimentacion);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null) { finish(); return; }
        ((TextView) findViewById(R.id.tvMascotaRegistroAlimentacion)).setText(
                getString(R.string.alimentacion_de, mascota.getNombre()));
        alimento = findViewById(R.id.etAlimento);
        cantidad = findViewById(R.id.etCantidadAlimento);
        horarios = findViewById(R.id.etHorariosAlimento);
        observaciones = findViewById(R.id.etObservacionesAlimento);
        frecuencia = findViewById(R.id.spFrecuenciaAlimento);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frecuencias_alimento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frecuencia.setAdapter(adapter);
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
        if (!completo || frecuencia.getSelectedItemPosition() == 0) return;
        String notas = observaciones.getText().toString().trim();
        boolean especial = ((CheckBox) findViewById(R.id.cbDietaEspecial)).isChecked();
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.resumen_alimentacion)
                .setMessage(getString(R.string.alimentacion_confirmacion, mascota.getNombre(),
                        alimento.getText().toString().trim(), cantidad.getText().toString().trim(),
                        horarios.getText().toString().trim(), frecuencia.getSelectedItem().toString(),
                        getString(especial ? R.string.respuesta_si : R.string.respuesta_no),
                        notas.isEmpty() ? getString(R.string.sin_observaciones) : notas))
                .setPositiveButton(R.string.volver_formulario, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
