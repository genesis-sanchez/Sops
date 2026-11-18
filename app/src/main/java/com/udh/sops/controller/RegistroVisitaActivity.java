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

public class RegistroVisitaActivity extends BaseActivity {
    private EditText fecha, diagnostico, tratamiento, veterinario, proximaRevision;
    private Spinner motivo;
    private Mascota mascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_registro_visita, R.string.registrar_visita);
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.motivos_visita, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        motivo.setAdapter(adapter);
        findViewById(R.id.btnEnviarVisita).setOnClickListener(view -> enviar());
    }

    private boolean obligatorio(EditText campo) {
        boolean completo = !campo.getText().toString().trim().isEmpty();
        campo.setError(completo ? null : getString(R.string.error_obligatorio));
        return completo;
    }

    private void enviar() {
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
                veterinario.getText().toString().trim(), null, null,
                proximaRevision.getText().toString().trim(), null);
        MaterialButton boton = findViewById(R.id.btnEnviarVisita);
        if (!boton.isEnabled()) return;
        boton.setEnabled(false);
        boton.setText(R.string.guardando_visita);
        findViewById(R.id.tvErrorVisita).setVisibility(View.GONE);
        new Thread(() -> {
            try {
                SopsDatabase.obtenerInstancia(getApplicationContext())
                        .visitaVeterinariaDao().insertar(visita);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, R.string.visita_guardada, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroVisita", "No se pudo guardar la visita", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(R.string.guardar_visita);
                    findViewById(R.id.tvErrorVisita).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
}
