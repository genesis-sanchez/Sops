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
import com.udh.sops.model.VisitaVeterinaria;

public class DetalleVisitaActivity extends BaseActivity {
    public static final String EXTRA_VISITA_ID = "com.udh.sops.extra.VISITA_ID";
    private long visitaId;
    private boolean eliminando;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_detalle_visita, R.string.detalle_visita_titulo);
        visitaId = getIntent().getLongExtra(EXTRA_VISITA_ID, 0);
        if (visitaId <= 0) {
            Toast.makeText(this, R.string.visita_no_encontrada, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (visitaId <= 0 || eliminando) return;
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                VisitaVeterinaria visita = base.visitaVeterinariaDao().obtenerPorId(visitaId);
                Mascota mascota = visita == null ? null : base.mascotaDao().obtenerPorId(visita.getMascotaId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed() || eliminando) return;
                    if (visita == null || mascota == null) {
                        Toast.makeText(this, R.string.visita_no_encontrada, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    mostrar(R.id.tvMascotaDetalleVisita, getString(R.string.visita_de, mascota.getNombre()));
                    mostrar(R.id.tvMotivoDetalleVisita, visita.getMotivo());
                    mostrar(R.id.tvFechaDetalleVisita, visita.getFecha());
                    mostrar(R.id.tvDiagnosticoDetalleVisita, visita.getDiagnostico());
                    mostrar(R.id.tvTratamientoDetalleVisita, visita.getTratamiento());
                    mostrar(R.id.tvVeterinarioDetalleVisita, visita.getVeterinario());
                    mostrar(R.id.tvClinicaDetalleVisita, visita.getClinica());
                    mostrar(R.id.tvPesoDetalleVisita, visita.getPeso());
                    mostrar(R.id.tvProximaDetalleVisita, visita.getProximaRevision());
                    mostrar(R.id.tvObservacionesDetalleVisita, visita.getObservaciones());
                    findViewById(R.id.btnEditarVisita).setOnClickListener(view -> {
                        Intent intent = new Intent(this, RegistroVisitaActivity.class);
                        intent.putExtra(RegistroVisitaActivity.EXTRA_EDITAR_VISITA_ID, visita.getId());
                        intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                        startActivity(intent);
                    });
                    findViewById(R.id.btnEliminarVisita).setOnClickListener(view ->
                            new MaterialAlertDialogBuilder(this)
                                    .setTitle(R.string.eliminar_visita)
                                    .setMessage(getString(R.string.confirmar_eliminar_visita, visita.getFecha()))
                                    .setNegativeButton(R.string.cancelar, null)
                                    .setPositiveButton(R.string.eliminar,
                                            (dialog, which) -> eliminarVisita(visita, mascota))
                                    .show());
                    findViewById(R.id.tvEstadoCargaVisita).setVisibility(View.GONE);
                    findViewById(R.id.contenedorDetalleVisita).setVisibility(View.VISIBLE);
                });
            } catch (Exception exception) {
                Log.e("DetalleVisita", "No se pudo cargar la visita", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    mostrar(R.id.tvEstadoCargaVisita, getString(R.string.error_cargar_detalle_visita));
                    findViewById(R.id.tvEstadoCargaVisita).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto == null || texto.trim().isEmpty()
                ? getString(R.string.dato_sin_registrar) : texto);
    }

    private void eliminarVisita(VisitaVeterinaria visita, Mascota mascota) {
        if (eliminando) return;
        eliminando = true;
        findViewById(R.id.btnEliminarVisita).setEnabled(false);
        findViewById(R.id.btnEditarVisita).setEnabled(false);
        new Thread(() -> {
            try {
                int eliminadas = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .visitaVeterinariaDao().eliminar(visita);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, eliminadas > 0 ? R.string.visita_eliminada
                            : R.string.visita_no_encontrada, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HistorialMedicoActivity.class);
                    intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception exception) {
                Log.e("DetalleVisita", "No se pudo eliminar la visita", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    eliminando = false;
                    findViewById(R.id.btnEliminarVisita).setEnabled(true);
                    findViewById(R.id.btnEditarVisita).setEnabled(true);
                    Toast.makeText(this, R.string.error_eliminar_visita, Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
