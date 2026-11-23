package com.udh.sops.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;
import com.udh.sops.model.VisitaVeterinaria;

public class DetalleVisitaActivity extends BaseActivity {
    public static final String EXTRA_VISITA_ID = "com.udh.sops.extra.VISITA_ID";
    private long visitaId;

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
        if (visitaId <= 0) return;
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                VisitaVeterinaria visita = base.visitaVeterinariaDao().obtenerPorId(visitaId);
                Mascota mascota = visita == null ? null : base.mascotaDao().obtenerPorId(visita.getMascotaId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
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
}
