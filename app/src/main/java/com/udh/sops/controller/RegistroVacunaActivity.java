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
    private EditText nombre, aplicacion, proxima, veterinario, observaciones;
    private Spinner dosis;
    private TextView error;
    private Mascota mascota;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_registro_vacuna, R.string.registrar_vacuna);
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
        Vacuna vacuna = new Vacuna(mascota.getId(), nombre.getText().toString().trim(),
                dosis.getSelectedItemPosition(), aplicacion.getText().toString().trim(),
                proxima.getText().toString().trim(), true,
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
                SopsDatabase.obtenerInstancia(getApplicationContext()).vacunaDao().insertar(vacuna);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, R.string.vacuna_guardada, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroVacuna", "No se pudo guardar la vacuna", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(R.string.guardar_vacuna);
                    error.setText(R.string.error_guardar_vacuna);
                    error.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
}
