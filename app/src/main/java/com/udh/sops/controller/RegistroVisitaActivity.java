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

public class RegistroVisitaActivity extends BaseActivity {
    private EditText fecha, diagnostico, tratamiento, veterinario;
    private Spinner motivo;
    private Mascota mascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_registro_visita, R.string.registrar_visita);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null) { finish(); return; }
        ((TextView) findViewById(R.id.tvMascotaVisita)).setText(
                getString(R.string.visita_de, mascota.getNombre()));
        fecha = findViewById(R.id.etFechaVisita);
        diagnostico = findViewById(R.id.etDiagnostico);
        tratamiento = findViewById(R.id.etTratamiento);
        veterinario = findViewById(R.id.etVeterinarioVisita);
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

        boolean control = ((CheckBox) findViewById(R.id.cbControlVisita)).isChecked();
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.resumen_visita)
                .setMessage(getString(R.string.visita_confirmacion, mascota.getNombre(),
                        fecha.getText().toString().trim(), motivo.getSelectedItem().toString(),
                        diagnostico.getText().toString().trim(), tratamiento.getText().toString().trim(),
                        veterinario.getText().toString().trim(),
                        getString(control ? R.string.respuesta_si : R.string.respuesta_no)))
                .setPositiveButton(R.string.volver_formulario, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
