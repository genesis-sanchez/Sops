package com.udh.sops.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.core.content.IntentCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;

public class RegistroVacunaActivity extends BaseActivity {
    private EditText nombre, aplicacion, proxima, veterinario, observaciones;
    private Spinner dosis;
    private TextView error;
    private Mascota mascota;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_registro_vacuna, R.string.registrar_vacuna);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null) { finish(); return; }
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
    }

    private boolean obligatorio(EditText campo) {
        boolean completo = !campo.getText().toString().trim().isEmpty();
        campo.setError(completo ? null : getString(R.string.error_obligatorio));
        return completo;
    }

    private void enviar() {
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
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.vacuna_confirmacion_titulo)
                .setMessage(getString(R.string.vacuna_confirmacion, mascota.getNombre(),
                        nombre.getText().toString().trim(), dosis.getSelectedItem().toString(),
                        aplicacion.getText().toString().trim(), proxima.getText().toString().trim(),
                        veterinario.getText().toString().trim(),
                        notas.isEmpty() ? getString(R.string.sin_observaciones) : notas,
                        getString(recordar ? R.string.respuesta_si : R.string.respuesta_no)))
                .setPositiveButton(R.string.volver_formulario, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
