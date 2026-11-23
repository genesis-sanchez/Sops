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
import com.udh.sops.model.VisitaVeterinaria;
import java.util.List;

public class HistorialMedicoActivity extends BaseActivity {
    private Mascota mascota;
    private LinearLayout registros;
    private TextView estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_historial_medico, R.string.historial_titulo);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        registros = findViewById(R.id.contenedorRegistrosMedicos);
        estado = findViewById(R.id.tvSinHistorial);
        findViewById(R.id.btnRegistrarVisita).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroVisitaActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
            startActivity(intent);
        });
        ((TextView) findViewById(R.id.tvMascotaHistorial)).setText(
                getString(R.string.historial_de, mascota.getNombre()));
    }

    @Override protected void onResume() {
        super.onResume();
        if (mascota != null && mascota.getId() > 0) cargarVisitas();
    }

    private void cargarVisitas() {
        registros.removeAllViews();
        estado.setText(R.string.cargando_visitas);
        estado.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                List<VisitaVeterinaria> visitas = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .visitaVeterinariaDao().obtenerPorMascota(mascota.getId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    registros.removeAllViews();
                    for (VisitaVeterinaria visita : visitas) mostrarVisita(visita);
                    estado.setText(R.string.sin_historial);
                    estado.setVisibility(visitas.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception exception) {
                Log.e("HistorialMedico", "No se pudieron cargar las visitas", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    estado.setText(R.string.error_cargar_visitas);
                    estado.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void mostrarVisita(VisitaVeterinaria visita) {
        View tarjeta = getLayoutInflater().inflate(R.layout.item_registro_medico, registros, false);
        ((TextView) tarjeta.findViewById(R.id.tvMotivoConsulta)).setText(visita.getMotivo());
        ((TextView) tarjeta.findViewById(R.id.tvFechaConsulta)).setText(visita.getFecha());
        ((TextView) tarjeta.findViewById(R.id.tvVeterinarioConsulta)).setText(visita.getVeterinario());
        ((TextView) tarjeta.findViewById(R.id.tvObservacionesConsulta)).setText(
                getString(R.string.resumen_visita_guardada, visita.getDiagnostico(), visita.getTratamiento()));
        TextView proxima = tarjeta.findViewById(R.id.tvProximaRevisionConsulta);
        if (visita.getProximaRevision() != null && !visita.getProximaRevision().isEmpty()) {
            proxima.setText(getString(R.string.proxima_revision_guardada, visita.getProximaRevision()));
            proxima.setVisibility(View.VISIBLE);
        }
        tarjeta.setOnClickListener(view -> {
            Intent intent = new Intent(this, DetalleVisitaActivity.class);
            intent.putExtra(DetalleVisitaActivity.EXTRA_VISITA_ID, visita.getId());
            startActivity(intent);
        });
        registros.addView(tarjeta);
    }
}
