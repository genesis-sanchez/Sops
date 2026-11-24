package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.IntentCompat;
import com.udh.sops.R;
import com.udh.sops.model.Alimentacion;
import com.udh.sops.model.SopsDatabase;
import com.udh.sops.model.Mascota;
import java.util.List;

public class AlimentacionActivity extends BaseActivity {
    private Mascota mascota;
    private LinearLayout registros;
    private TextView estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_alimentacion, R.string.alimentacion_titulo);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        registros = findViewById(R.id.contenedorAlimentacion);
        estado = findViewById(R.id.tvSinAlimentacion);
        findViewById(R.id.btnRegistrarAlimentacion).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroAlimentacionActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
            startActivity(intent);
        });
        mostrar(R.id.tvMascotaAlimentacion, getString(R.string.alimentacion_de, mascota.getNombre()));
    }

    @Override protected void onResume() {
        super.onResume();
        if (mascota != null && mascota.getId() > 0) cargarAlimentacion();
    }

    private void cargarAlimentacion() {
        registros.removeAllViews();
        estado.setText(R.string.cargando_alimentacion);
        estado.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                List<Alimentacion> lista = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .alimentacionDao().obtenerPorMascota(mascota.getId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    registros.removeAllViews();
                    for (Alimentacion alimentacion : lista) mostrarRegistro(alimentacion);
                    estado.setText(R.string.sin_alimentacion);
                    estado.setVisibility(lista.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception exception) {
                Log.e("Alimentacion", "No se pudieron cargar los registros", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    estado.setText(R.string.error_cargar_alimentacion);
                    estado.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void mostrarRegistro(Alimentacion alimentacion) {
        View tarjeta = getLayoutInflater().inflate(R.layout.item_alimentacion, registros, false);
        ((TextView) tarjeta.findViewById(R.id.tvAlimento)).setText(alimentacion.getNombreAlimento());
        ((TextView) tarjeta.findViewById(R.id.tvTipoAlimentoGuardado)).setText(alimentacion.getTipoAlimento());
        ((TextView) tarjeta.findViewById(R.id.tvCantidadAlimento)).setText(alimentacion.getCantidad());
        ((TextView) tarjeta.findViewById(R.id.tvHorariosAlimento)).setText(alimentacion.getHorarios());
        ((TextView) tarjeta.findViewById(R.id.tvFrecuenciaAlimentoGuardada)).setText(
                getResources().getQuantityString(R.plurals.veces_al_dia_guardadas,
                        alimentacion.getVecesAlDia(), alimentacion.getVecesAlDia()));
        String notas = alimentacion.getObservaciones();
        ((TextView) tarjeta.findViewById(R.id.tvObservacionesAlimento)).setText(
                notas == null || notas.trim().isEmpty() ? getString(R.string.sin_observaciones) : notas);
        registros.addView(tarjeta);
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto);
    }
}
