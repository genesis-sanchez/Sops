package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.material.button.MaterialButton;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.Vacuna;
import com.udh.sops.model.VisitaVeterinaria;
import com.udh.sops.model.CuidadoPendiente;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.widget.TextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.udh.sops.R;
import com.udh.sops.model.SopsDatabase;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepararMenu(findViewById(R.id.toolbarInicio));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contenedorInicio), (view, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });
    }

    @Override protected void onResume() {
        super.onResume();
        cargarResumen();
    }

    private void cargarResumen() {
        TextView total = findViewById(R.id.tvTotalMascotas);
        TextView estadoVacunas = findViewById(R.id.tvEstadoVacunasInicio);
        TextView estadoRevisiones = findViewById(R.id.tvEstadoRevisionesInicio);
        LinearLayout vacunas = findViewById(R.id.contenedorVacunasInicio);
        LinearLayout revisiones = findViewById(R.id.contenedorRevisionesInicio);
        total.setText(R.string.cargando_mascotas);
        estadoVacunas.setText(R.string.cargando_cuidados);
        estadoRevisiones.setText(R.string.cargando_cuidados);
        estadoVacunas.setVisibility(View.VISIBLE);
        estadoRevisiones.setVisibility(View.VISIBLE);
        vacunas.removeAllViews();
        revisiones.removeAllViews();
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                Map<Long, Mascota> mascotas = new HashMap<>();
                for (Mascota mascota : base.mascotaDao().obtenerTodas()) mascotas.put(mascota.getId(), mascota);
                List<CuidadoPendiente> pendientesVacunas = new ArrayList<>();
                List<CuidadoPendiente> pendientesRevisiones = new ArrayList<>();
                for (Vacuna vacuna : base.vacunaDao().obtenerTodas()) {
                    Mascota mascota = mascotas.get(vacuna.getMascotaId());
                    String fecha = vacuna.isAplicada() ? vacuna.getProximaFecha() : vacuna.getFecha();
                    if (mascota != null && (!vacuna.isAplicada() || tieneTexto(fecha))) {
                        pendientesVacunas.add(new CuidadoPendiente(mascota, vacuna.getId(),
                                vacuna.getNombre(), fecha, true));
                    }
                }
                for (VisitaVeterinaria visita : base.visitaVeterinariaDao().obtenerTodas()) {
                    Mascota mascota = mascotas.get(visita.getMascotaId());
                    if (mascota != null && tieneTexto(visita.getProximaRevision())) {
                        pendientesRevisiones.add(new CuidadoPendiente(mascota, visita.getId(),
                                visita.getMotivo(), visita.getProximaRevision(), false));
                    }
                }
                Collections.sort(pendientesVacunas);
                Collections.sort(pendientesRevisiones);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    total.setText(getResources().getQuantityString(
                            R.plurals.total_mascotas_inicio, mascotas.size(), mascotas.size()));
                    vacunas.removeAllViews();
                    revisiones.removeAllViews();
                    for (CuidadoPendiente cuidado : pendientesVacunas) mostrarCuidado(vacunas, cuidado);
                    for (CuidadoPendiente cuidado : pendientesRevisiones) mostrarCuidado(revisiones, cuidado);
                    estadoVacunas.setText(R.string.sin_vacunas_inicio);
                    estadoRevisiones.setText(R.string.sin_revisiones_inicio);
                    estadoVacunas.setVisibility(pendientesVacunas.isEmpty() ? View.VISIBLE : View.GONE);
                    estadoRevisiones.setVisibility(pendientesRevisiones.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception exception) {
                Log.e("Inicio", "No se pudo cargar el resumen", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    total.setText(R.string.error_total_mascotas);
                    estadoVacunas.setText(R.string.error_cargar_resumen);
                    estadoRevisiones.setText(R.string.error_cargar_resumen);
                });
            }
        }).start();
    }

    private boolean tieneTexto(String texto) { return texto != null && !texto.trim().isEmpty(); }

    private void mostrarCuidado(LinearLayout contenedor, CuidadoPendiente cuidado) {
        View tarjeta = getLayoutInflater().inflate(R.layout.item_cuidado_inicio, contenedor, false);
        ((TextView) tarjeta.findViewById(R.id.tvTituloCuidado)).setText(tieneTexto(cuidado.titulo)
                ? cuidado.titulo : getString(R.string.dato_sin_registrar));
        MaterialButton mascota = tarjeta.findViewById(R.id.btnMascotaCuidado);
        mascota.setText(cuidado.mascota.getNombre());
        mascota.setOnClickListener(view -> {
            Intent intent = new Intent(this, DetalleMascotaActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, cuidado.mascota);
            startActivity(intent);
        });
        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(new Date());
        Date fechaHoy = CuidadoPendiente.leerFecha(hoy);
        int estado = cuidado.fechaOrden == null ? R.string.cuidado_revisar_fecha
                : cuidado.fechaOrden.before(fechaHoy) ? R.string.cuidado_vencido
                : cuidado.fechaOrden.equals(fechaHoy) ? R.string.cuidado_hoy : R.string.cuidado_proximo;
        ((TextView) tarjeta.findViewById(R.id.tvFechaCuidado)).setText(getString(
                R.string.fecha_cuidado_inicio, getString(estado),
                tieneTexto(cuidado.fecha) ? cuidado.fecha : getString(R.string.dato_sin_registrar)));
        tarjeta.setOnClickListener(view -> {
            Intent intent = new Intent(this, cuidado.vacuna ? DetalleVacunaActivity.class : DetalleVisitaActivity.class);
            intent.putExtra(cuidado.vacuna ? DetalleVacunaActivity.EXTRA_VACUNA_ID
                    : DetalleVisitaActivity.EXTRA_VISITA_ID, cuidado.registroId);
            startActivity(intent);
        });
        contenedor.addView(tarjeta);
    }
}
