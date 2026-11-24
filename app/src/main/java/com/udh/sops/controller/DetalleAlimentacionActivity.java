package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.udh.sops.R;
import com.udh.sops.model.Alimentacion;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;

public class DetalleAlimentacionActivity extends BaseActivity {
    public static final String EXTRA_ALIMENTACION_ID = "com.udh.sops.extra.ALIMENTACION_ID";
    private long alimentacionId;
    private boolean eliminando;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_detalle_alimentacion, R.string.detalle_alimentacion_titulo);
        alimentacionId = getIntent().getLongExtra(EXTRA_ALIMENTACION_ID, 0);
        if (alimentacionId <= 0) {
            Toast.makeText(this, R.string.alimentacion_no_encontrada, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (alimentacionId <= 0 || eliminando) return;
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                Alimentacion registro = base.alimentacionDao().obtenerPorId(alimentacionId);
                Mascota mascota = registro == null ? null : base.mascotaDao().obtenerPorId(registro.getMascotaId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed() || eliminando) return;
                    if (registro == null || mascota == null) {
                        Toast.makeText(this, R.string.alimentacion_no_encontrada, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    mostrar(R.id.tvNombreDetalleAlimentacion, registro.getNombreAlimento());
                    mostrar(R.id.tvMascotaDetalleAlimentacion, getString(R.string.alimentacion_de, mascota.getNombre()));
                    mostrar(R.id.tvTipoDetalleAlimentacion, registro.getTipoAlimento());
                    String cantidad = registro.getCantidad();
                    if (cantidad != null && !cantidad.trim().isEmpty()
                            && registro.getUnidad() != null && !registro.getUnidad().trim().isEmpty()) {
                        cantidad += " " + registro.getUnidad();
                    }
                    mostrar(R.id.tvCantidadDetalleAlimentacion, cantidad);
                    mostrar(R.id.tvHorariosDetalleAlimentacion, registro.getHorarios());
                    mostrar(R.id.tvFrecuenciaDetalleAlimentacion, registro.getVecesAlDia() > 0
                            ? getResources().getQuantityString(R.plurals.veces_al_dia_guardadas,
                                    registro.getVecesAlDia(), registro.getVecesAlDia()) : null);
                    mostrar(R.id.tvObservacionesDetalleAlimentacion, registro.getObservaciones());
                    mostrar(R.id.tvInicioDetalleAlimentacion, registro.getFechaInicio());
                    mostrar(R.id.tvFinDetalleAlimentacion, registro.getFechaFin());
                    mostrar(R.id.tvEstadoDetalleAlimentacion, getString(registro.isActivo()
                            ? R.string.alimentacion_activa : R.string.alimentacion_inactiva));
                    findViewById(R.id.btnEditarAlimentacion).setOnClickListener(view -> {
                        Intent intent = new Intent(this, RegistroAlimentacionActivity.class);
                        intent.putExtra(RegistroAlimentacionActivity.EXTRA_EDITAR_ALIMENTACION_ID, registro.getId());
                        intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                        startActivity(intent);
                    });
                    findViewById(R.id.btnEliminarAlimentacion).setOnClickListener(view ->
                            new MaterialAlertDialogBuilder(this)
                                    .setTitle(R.string.eliminar_alimentacion)
                                    .setMessage(getString(R.string.confirmar_eliminar_alimentacion, registro.getNombreAlimento()))
                                    .setNegativeButton(R.string.cancelar, null)
                                    .setPositiveButton(R.string.eliminar,
                                            (dialog, which) -> eliminarRegistro(registro, mascota))
                                    .show());
                    findViewById(R.id.tvEstadoCargaAlimentacion).setVisibility(View.GONE);
                    findViewById(R.id.contenedorDetalleAlimentacion).setVisibility(View.VISIBLE);
                });
            } catch (Exception exception) {
                Log.e("DetalleAlimentacion", "No se pudo cargar la alimentacion", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    mostrar(R.id.tvEstadoCargaAlimentacion, getString(R.string.error_cargar_detalle_alimentacion));
                    findViewById(R.id.tvEstadoCargaAlimentacion).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto == null || texto.trim().isEmpty()
                ? getString(R.string.dato_sin_registrar) : texto);
    }
    private void eliminarRegistro(Alimentacion registro, Mascota mascota) {
        if (eliminando) return;
        eliminando = true;
        findViewById(R.id.btnEliminarAlimentacion).setEnabled(false);
        findViewById(R.id.btnEditarAlimentacion).setEnabled(false);
        new Thread(() -> {
            try {
                int eliminadas = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .alimentacionDao().eliminar(registro);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, eliminadas > 0 ? R.string.alimentacion_eliminada
                            : R.string.alimentacion_no_encontrada, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AlimentacionActivity.class);
                    intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception exception) {
                Log.e("DetalleAlimentacion", "No se pudo eliminar la alimentacion", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    eliminando = false;
                    findViewById(R.id.btnEliminarAlimentacion).setEnabled(true);
                    findViewById(R.id.btnEditarAlimentacion).setEnabled(true);
                    Toast.makeText(this, R.string.error_eliminar_alimentacion, Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
