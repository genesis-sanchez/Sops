package com.udh.sops.controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.udh.sops.R;
import com.udh.sops.model.DataSource;
import com.udh.sops.view.MascotaAdapter;

public class ListaMascotasActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_lista_mascotas, R.string.title_lista_mascotas);
        RecyclerView lista = findViewById(R.id.rvMascotas);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(new MascotaAdapter(DataSource.getMascotasEjemplo(), mascota -> {
            Intent intent = new Intent(this, DetalleMascotaActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
            startActivity(intent);
        }));
    }
}
