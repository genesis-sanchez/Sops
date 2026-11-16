package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.IntentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.udh.sops.R;
import com.udh.sops.model.SopsDatabase;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.Vacuna;
import com.udh.sops.view.VacunaAdapter;
import java.util.List;

public class VacunasActivity extends BaseActivity {
    private Mascota mascota;
    private RecyclerView lista;
    private TextView estado;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_vacunas, R.string.vacunas_titulo);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        findViewById(R.id.btnRegistrarVacuna).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroVacunaActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
            startActivity(intent);
        });
        ((TextView) findViewById(R.id.tvMascotaVacunas)).setText(
                getString(R.string.vacunas_de, mascota.getNombre()));
        lista = findViewById(R.id.rvVacunas);
        estado = findViewById(R.id.tvSinVacunas);
        lista.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override protected void onResume() {
        super.onResume();
        if (mascota != null && mascota.getId() > 0) cargarVacunas();
    }

    private void cargarVacunas() {
        estado.setText(R.string.cargando_vacunas);
        estado.setVisibility(View.VISIBLE);
        lista.setVisibility(View.GONE);
        new Thread(() -> {
            try {
                List<Vacuna> vacunas = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .vacunaDao().obtenerPorMascota(mascota.getId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    lista.setAdapter(new VacunaAdapter(vacunas));
                    estado.setText(R.string.sin_vacunas);
                    estado.setVisibility(vacunas.isEmpty() ? View.VISIBLE : View.GONE);
                    lista.setVisibility(vacunas.isEmpty() ? View.GONE : View.VISIBLE);
                });
            } catch (Exception exception) {
                Log.e("Vacunas", "No se pudieron cargar las vacunas", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    estado.setText(R.string.error_cargar_vacunas);
                    estado.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
}
