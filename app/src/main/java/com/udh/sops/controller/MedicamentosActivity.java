package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.IntentCompat;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;
import com.udh.sops.model.Medicamento;
import java.util.List;

public class MedicamentosActivity extends BaseActivity {
    private Mascota mascota;
    private LinearLayout registros;
    private TextView estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_medicamentos, R.string.medicamentos_titulo);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        registros = findViewById(R.id.contenedorMedicamentos);
        estado = findViewById(R.id.tvSinMedicamentos);
        findViewById(R.id.btnRegistrarMedicamento).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroMedicamentoActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
            startActivity(intent);
        });
        ((TextView) findViewById(R.id.tvMascotaMedicamentos)).setText(
                getString(R.string.medicamentos_de, mascota.getNombre()));
    }

    @Override protected void onResume() {
        super.onResume();
        if (mascota != null && mascota.getId() > 0) cargarMedicamentos();
    }

    private void cargarMedicamentos() {
        registros.removeAllViews();
        estado.setText(R.string.cargando_medicamentos);
        estado.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                List<Medicamento> medicamentos = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .medicamentoDao().obtenerPorMascota(mascota.getId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    registros.removeAllViews();
                    for (Medicamento medicamento : medicamentos) mostrarMedicamento(medicamento);
                    estado.setText(R.string.sin_medicamentos);
                    estado.setVisibility(medicamentos.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception exception) {
                Log.e("Medicamentos", "No se pudieron cargar los medicamentos", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    estado.setText(R.string.error_cargar_medicamentos);
                    estado.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void mostrarMedicamento(Medicamento medicamento) {
        View tarjeta = getLayoutInflater().inflate(R.layout.item_medicamento, registros, false);
        ((TextView) tarjeta.findViewById(R.id.tvNombreMedicamento)).setText(medicamento.getNombre());
        ((TextView) tarjeta.findViewById(R.id.tvResumenMedicamento)).setText(getString(
                R.string.resumen_medicamento_guardado, medicamento.getDosis(), medicamento.getIntervaloHoras(),
                medicamento.getFechaInicio(), medicamento.getFechaFin()));
        ((TextView) tarjeta.findViewById(R.id.tvIndicacionesMedicamento)).setText(
                medicamento.getIndicaciones() == null || medicamento.getIndicaciones().trim().isEmpty()
                        ? getString(R.string.sin_observaciones) : medicamento.getIndicaciones());
        registros.addView(tarjeta);
    }
}
