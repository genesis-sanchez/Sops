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
    private Mascota mascota;
    private EditText nombre, dosis, intervalo, inicio, fin, indicaciones;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_registro_medicamento, R.string.registrar_medicamento);
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
    }
    private boolean obligatorio(EditText campo) {
        boolean completo = !campo.getText().toString().trim().isEmpty();
        campo.setError(completo ? null : getString(R.string.error_obligatorio));
        return completo;
    }
    private void guardar() {
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
        Medicamento registro = new Medicamento(mascota.getId(), null, nombre.getText().toString().trim(),
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
                SopsDatabase.obtenerInstancia(getApplicationContext()).medicamentoDao().insertar(registro);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, R.string.medicamento_guardado, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroMedicamento", "No se pudo guardar el medicamento", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(R.string.guardar_medicamento);
                    findViewById(R.id.tvErrorMedicamento).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
}
