package com.udh.sops.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;
import com.udh.sops.view.MascotaAdapter;
import java.util.List;

public class ListaMascotasActivity extends BaseActivity {
    private RecyclerView lista;
    private TextView estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_lista_mascotas, R.string.title_lista_mascotas);
        lista = findViewById(R.id.rvMascotas);
        estado = findViewById(R.id.tvEstadoLista);
        lista.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMascotas();
    }

    private void cargarMascotas() {
        estado.setText(R.string.cargando_mascotas);
        estado.setVisibility(View.VISIBLE);
        lista.setVisibility(View.GONE);
        new Thread(() -> {
            try {
                List<Mascota> mascotas = SopsDatabase.obtenerInstancia(getApplicationContext())
                        .mascotaDao().obtenerTodas();
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    lista.setAdapter(new MascotaAdapter(mascotas, mascota -> {
                        Intent intent = new Intent(this, DetalleMascotaActivity.class);
                        intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                        startActivity(intent);
                    }));
                    estado.setText(R.string.lista_sin_mascotas);
                    estado.setVisibility(mascotas.isEmpty() ? View.VISIBLE : View.GONE);
                    lista.setVisibility(mascotas.isEmpty() ? View.GONE : View.VISIBLE);
                });
            } catch (Exception exception) {
                Log.e("ListaMascotas", "No se pudieron cargar las mascotas", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    estado.setText(R.string.error_cargar_mascotas);
                    estado.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
}
