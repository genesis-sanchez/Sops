package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;
import androidx.core.content.IntentCompat;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;

public class DetalleMascotaActivity extends BaseActivity {
    public static final String EXTRA_MASCOTA = "com.udh.sops.extra.MASCOTA";
    public static final String EXTRA_REGISTRO_DEMO = "com.udh.sops.extra.REGISTRO_DEMO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_detalle_mascota, R.string.detalle_titulo);
        Mascota mascota = IntentCompat.getSerializableExtra(getIntent(), EXTRA_MASCOTA, Mascota.class);
        if (mascota == null) {
            finish();
            return;
        }
        mostrar(R.id.tvNombreDetalle, mascota.getNombre());
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
        if (getIntent().getBooleanExtra(EXTRA_REGISTRO_DEMO, false)) {
            mostrar(R.id.tvAvisoDetalle, getString(R.string.confirmacion_demo));
        }
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto);
    }
}
