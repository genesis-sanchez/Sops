package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.IntentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.udh.sops.R;
import com.udh.sops.model.DataSource;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.Vacuna;
import com.udh.sops.view.VacunaAdapter;
import java.util.ArrayList;
import java.util.List;

public class VacunasActivity extends BaseActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_vacunas, R.string.vacunas_titulo);
        Mascota mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null) { finish(); return; }
        findViewById(R.id.btnRegistrarVacuna).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroVacunaActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
            startActivity(intent);
        });
        ((TextView) findViewById(R.id.tvMascotaVacunas)).setText(
                getString(R.string.vacunas_de, mascota.getNombre()));
        boolean nueva = getIntent().getBooleanExtra(DetalleMascotaActivity.EXTRA_REGISTRO_DEMO, false);
        List<Vacuna> vacunas = nueva ? new ArrayList<>() : DataSource.getVacunasEjemplo(mascota);
        RecyclerView lista = findViewById(R.id.rvVacunas);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(new VacunaAdapter(vacunas));
        findViewById(R.id.tvSinVacunas).setVisibility(vacunas.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
