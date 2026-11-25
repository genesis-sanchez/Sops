package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.Medicamento;
import com.udh.sops.model.SopsDatabase;

public class DetalleMedicamentoActivity extends BaseActivity {
    public static final String EXTRA_MEDICAMENTO_ID = "com.udh.sops.extra.MEDICAMENTO_ID";
    private long medicamentoId;
    private boolean eliminando;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_detalle_medicamento, R.string.detalle_medicamento_titulo);
        medicamentoId = getIntent().getLongExtra(EXTRA_MEDICAMENTO_ID, 0);
        if (medicamentoId <= 0) {
            Toast.makeText(this, R.string.medicamento_no_encontrado, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override protected void onResume() {
        super.onResume();
        if (medicamentoId <= 0 || eliminando) return;
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                Medicamento registro = base.medicamentoDao().obtenerPorId(medicamentoId);
                Mascota mascota = registro == null ? null : base.mascotaDao().obtenerPorId(registro.getMascotaId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed() || eliminando) return;
                    if (registro == null || mascota == null) {
                        Toast.makeText(this, R.string.medicamento_no_encontrado, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    mostrar(R.id.tvNombreDetalleMedicamento, registro.getNombre());
                    mostrar(R.id.tvMascotaDetalleMedicamento, getString(R.string.medicamentos_de, mascota.getNombre()));
                    mostrar(R.id.tvDosisDetalleMedicamento, registro.getDosis());
                    mostrar(R.id.tvIntervaloDetalleMedicamento, getString(R.string.intervalo_medicamento_guardado, registro.getIntervaloHoras()));
                    mostrar(R.id.tvInicioDetalleMedicamento, registro.getFechaInicio());
                    mostrar(R.id.tvFinDetalleMedicamento, registro.getFechaFin());
                    mostrar(R.id.tvIndicacionesDetalleMedicamento, registro.getIndicaciones());
                    mostrar(R.id.tvEstadoDetalleMedicamento, getString(registro.isActivo() ? R.string.medicamento_estado_activo : R.string.medicamento_estado_inactivo));
                    findViewById(R.id.btnEditarMedicamento).setOnClickListener(view -> {
                        Intent intent = new Intent(this, RegistroMedicamentoActivity.class);
                        intent.putExtra(RegistroMedicamentoActivity.EXTRA_EDITAR_MEDICAMENTO_ID, registro.getId());
                        intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                        startActivity(intent);
                    });
                    findViewById(R.id.btnEliminarMedicamento).setOnClickListener(view ->
                            new MaterialAlertDialogBuilder(this).setTitle(R.string.eliminar_medicamento)
                                    .setMessage(getString(R.string.confirmar_eliminar_medicamento, registro.getNombre()))
                                    .setNegativeButton(R.string.cancelar, null)
                                    .setPositiveButton(R.string.eliminar, (dialog, which) -> eliminarRegistro(registro, mascota)).show());
                    findViewById(R.id.tvEstadoCargaMedicamento).setVisibility(View.GONE);
                    findViewById(R.id.contenedorDetalleMedicamento).setVisibility(View.VISIBLE);
                });
            } catch (Exception exception) {
                Log.e("DetalleMedicamento", "No se pudo cargar el medicamento", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    mostrar(R.id.tvEstadoCargaMedicamento, getString(R.string.error_cargar_detalle_medicamento));
                    findViewById(R.id.tvEstadoCargaMedicamento).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto == null || texto.trim().isEmpty()
                ? getString(R.string.dato_sin_registrar) : texto);
    }
    private void eliminarRegistro(Medicamento registro, Mascota mascota) {
        if (eliminando) return;
        eliminando = true;
        findViewById(R.id.btnEliminarMedicamento).setEnabled(false);
        findViewById(R.id.btnEditarMedicamento).setEnabled(false);
        new Thread(() -> {
            try {
                int eliminadas = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .medicamentoDao().eliminar(registro);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, eliminadas > 0 ? R.string.medicamento_eliminado
                            : R.string.medicamento_no_encontrado, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MedicamentosActivity.class);
                    intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception exception) {
                Log.e("DetalleMedicamento", "No se pudo eliminar el medicamento", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    eliminando = false;
                    findViewById(R.id.btnEliminarMedicamento).setEnabled(true);
                    findViewById(R.id.btnEditarMedicamento).setEnabled(true);
                    Toast.makeText(this, R.string.error_eliminar_medicamento, Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
