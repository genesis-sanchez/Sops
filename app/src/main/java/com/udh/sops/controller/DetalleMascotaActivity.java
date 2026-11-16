package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.IntentCompat;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DetalleMascotaActivity extends BaseActivity {
    public static final String EXTRA_MASCOTA = "com.udh.sops.extra.MASCOTA";
    public static final String EXTRA_REGISTRO_DEMO = "com.udh.sops.extra.REGISTRO_DEMO";
    private Mascota mascota;
    private boolean eliminando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_detalle_mascota, R.string.detalle_titulo);
        mascota = IntentCompat.getSerializableExtra(getIntent(), EXTRA_MASCOTA, Mascota.class);
        if (mascota == null) {
            finish();
            return;
        }
        findViewById(R.id.btnEditarMascota).setVisibility(mascota.getId() > 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.btnEliminarMascota).setVisibility(mascota.getId() > 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.btnEditarMascota).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroMascotaActivity.class);
            intent.putExtra(RegistroMascotaActivity.EXTRA_EDITAR_MASCOTA, mascota);
            startActivity(intent);
        });
        findViewById(R.id.btnEliminarMascota).setOnClickListener(view ->
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.eliminar_mascota)
                        .setMessage(getString(R.string.confirmar_eliminar_mascota, mascota.getNombre()))
                        .setNegativeButton(R.string.cancelar, null)
                        .setPositiveButton(R.string.eliminar, (dialog, which) -> eliminarMascota())
                        .show());
        findViewById(R.id.btnAlimentacionMascota).setOnClickListener(view -> {
            Intent intent = new Intent(this, AlimentacionActivity.class);
            intent.putExtra(EXTRA_MASCOTA, mascota);
            intent.putExtra(EXTRA_REGISTRO_DEMO, getIntent().getBooleanExtra(EXTRA_REGISTRO_DEMO, false));
            startActivity(intent);
        });
        findViewById(R.id.btnHistorialMascota).setOnClickListener(view -> {
            Intent intent = new Intent(this, HistorialMedicoActivity.class);
            intent.putExtra(EXTRA_MASCOTA, mascota);
            intent.putExtra(EXTRA_REGISTRO_DEMO, getIntent().getBooleanExtra(EXTRA_REGISTRO_DEMO, false));
            startActivity(intent);
        });
        findViewById(R.id.btnVacunasMascota).setOnClickListener(view -> {
            Intent intent = new Intent(this, VacunasActivity.class);
            intent.putExtra(EXTRA_MASCOTA, mascota);
            intent.putExtra(EXTRA_REGISTRO_DEMO, getIntent().getBooleanExtra(EXTRA_REGISTRO_DEMO, false));
            startActivity(intent);
        });
        mostrarFicha();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mascota == null || mascota.getId() <= 0 || eliminando) return;
        new Thread(() -> {
            try {
                Mascota actualizada = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .mascotaDao().obtenerPorId(mascota.getId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed() || eliminando) return;
                    if (actualizada == null) { finish(); return; }
                    mascota = actualizada;
                    getIntent().putExtra(EXTRA_MASCOTA, mascota);
                    mostrarFicha();
                });
            } catch (Exception exception) {
                Log.e("DetalleMascota", "No se pudo actualizar la ficha", exception);
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed())
                        Toast.makeText(this, R.string.error_cargar_ficha, Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void eliminarMascota() {
        if (eliminando) return;
        eliminando = true;
        findViewById(R.id.btnEliminarMascota).setEnabled(false);
        findViewById(R.id.btnEditarMascota).setEnabled(false);
        new Thread(() -> {
            try {
                SopsDatabase.obtenerInstancia(getApplicationContext()).mascotaDao().eliminar(mascota);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, R.string.mascota_eliminada, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ListaMascotasActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception exception) {
                Log.e("DetalleMascota", "No se pudo eliminar la mascota", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    eliminando = false;
                    findViewById(R.id.btnEliminarMascota).setEnabled(true);
                    findViewById(R.id.btnEditarMascota).setEnabled(true);
                    Toast.makeText(this, R.string.error_eliminar_mascota, Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void mostrarFicha() {
        mostrar(R.id.tvNombreDetalle, mascota.getNombre());
        mostrar(R.id.tvRazaDetalle, mascota.getRaza());
        mostrar(R.id.tvEspecieDetalle, mascota.getTipo());
        mostrar(R.id.tvEdadDetalle, mascota.getEdad());
        mostrar(R.id.tvSexoDetalle, mascota.getSexo());
        mostrar(R.id.tvEsterilizadoDetalle, getString(mascota.isEsterilizado()
                ? R.string.respuesta_si : R.string.respuesta_no));
        if (mascota.getFechaNacimiento() != null && mascota.getPeso() != null) {
            findViewById(R.id.contenedorDatosRegistro).setVisibility(View.VISIBLE);
            mostrar(R.id.tvNacimientoDetalle, mascota.getFechaNacimiento());
            mostrar(R.id.tvPesoDetalle, getString(R.string.peso_kg, mascota.getPeso()));
        }
        if (mascota.getId() > 0) {
            findViewById(R.id.tvAvisoDetalle).setVisibility(View.GONE);
        } else if (getIntent().getBooleanExtra(EXTRA_REGISTRO_DEMO, false)) {
            mostrar(R.id.tvAvisoDetalle, getString(R.string.confirmacion_demo));
        }
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto);
    }
}
