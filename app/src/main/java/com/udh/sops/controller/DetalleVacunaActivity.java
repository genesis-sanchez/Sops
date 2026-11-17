package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;
import com.udh.sops.model.Vacuna;

public class DetalleVacunaActivity extends BaseActivity {
    public static final String EXTRA_VACUNA_ID = "com.udh.sops.extra.VACUNA_ID";
    private long vacunaId;
    private boolean eliminando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_detalle_vacuna, R.string.detalle_vacuna_titulo);
        vacunaId = getIntent().getLongExtra(EXTRA_VACUNA_ID, 0);
        if (vacunaId <= 0) finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vacunaId <= 0 || eliminando) return;
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                Vacuna vacuna = base.vacunaDao().obtenerPorId(vacunaId);
                Mascota mascota = vacuna == null || vacuna.getMascotaId() == null
                        ? null : base.mascotaDao().obtenerPorId(vacuna.getMascotaId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed() || eliminando) return;
                    if (vacuna == null || mascota == null) {
                        Toast.makeText(this, R.string.vacuna_no_encontrada, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    mostrar(R.id.tvNombreDetalleVacuna, vacuna.getNombre());
                    mostrar(R.id.tvMascotaDetalleVacuna, getString(R.string.vacunas_de, mascota.getNombre()));
                    mostrar(R.id.tvEstadoDetalleVacuna, getString(vacuna.isAplicada()
                            ? R.string.vacuna_aplicada : R.string.vacuna_pendiente));
                    mostrar(R.id.tvDosisDetalleVacuna, String.valueOf(vacuna.getDosis()));
                    mostrar(R.id.tvEtiquetaFechaVacuna, getString(vacuna.isAplicada()
                            ? R.string.fecha_aplicada : R.string.fecha_programada));
                    mostrar(R.id.tvAplicacionDetalleVacuna, vacuna.getFecha());
                    mostrar(R.id.tvProximaDetalleVacuna, vacuna.getProximaFecha());
                    mostrar(R.id.tvVeterinarioDetalleVacuna, vacuna.getVeterinario());
                    mostrar(R.id.tvObservacionesDetalleVacuna, vacuna.getObservaciones() == null
                            || vacuna.getObservaciones().trim().isEmpty()
                            ? getString(R.string.sin_observaciones) : vacuna.getObservaciones());
                    mostrar(R.id.tvRecordatorioDetalleVacuna, getString(vacuna.isRecordatorio()
                            ? R.string.respuesta_si : R.string.respuesta_no));
                    findViewById(R.id.btnEditarVacuna).setOnClickListener(view -> {
                        Intent intent = new Intent(this, RegistroVacunaActivity.class);
                        intent.putExtra(RegistroVacunaActivity.EXTRA_EDITAR_VACUNA_ID, vacuna.getId());
                        intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                        startActivity(intent);
                    });
                    findViewById(R.id.btnEliminarVacuna).setOnClickListener(view ->
                            new MaterialAlertDialogBuilder(this)
                                    .setTitle(R.string.eliminar_vacuna)
                                    .setMessage(getString(R.string.confirmar_eliminar_vacuna, vacuna.getNombre()))
                                    .setNegativeButton(R.string.cancelar, null)
                                    .setPositiveButton(R.string.eliminar,
                                            (dialog, which) -> eliminarVacuna(vacuna, mascota))
                                    .show());
                    findViewById(R.id.tvEstadoCargaVacuna).setVisibility(View.GONE);
                    findViewById(R.id.contenedorDetalleVacuna).setVisibility(View.VISIBLE);
                });
            } catch (Exception exception) {
                Log.e("DetalleVacuna", "No se pudo cargar la vacuna", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    mostrar(R.id.tvEstadoCargaVacuna, getString(R.string.error_cargar_detalle_vacuna));
                    findViewById(R.id.tvEstadoCargaVacuna).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto == null || texto.trim().isEmpty()
                ? getString(R.string.dato_sin_registrar) : texto);
    }

    private void eliminarVacuna(Vacuna vacuna, Mascota mascota) {
        if (eliminando) return;
        eliminando = true;
        findViewById(R.id.btnEliminarVacuna).setEnabled(false);
        findViewById(R.id.btnEditarVacuna).setEnabled(false);
        new Thread(() -> {
            try {
                int eliminadas = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .vacunaDao().eliminar(vacuna);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, eliminadas > 0 ? R.string.vacuna_eliminada
                            : R.string.vacuna_no_encontrada, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, VacunasActivity.class);
                    intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception exception) {
                Log.e("DetalleVacuna", "No se pudo eliminar la vacuna", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    eliminando = false;
                    findViewById(R.id.btnEliminarVacuna).setEnabled(true);
                    findViewById(R.id.btnEditarVacuna).setEnabled(true);
                    Toast.makeText(this, R.string.error_eliminar_vacuna, Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
