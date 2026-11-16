package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.IntentCompat;
import com.udh.sops.R;
import com.udh.sops.model.Alimentacion;
import com.udh.sops.model.DataSource;
import com.udh.sops.model.Mascota;

public class AlimentacionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_alimentacion, R.string.alimentacion_titulo);
        Mascota mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null) { finish(); return; }
        findViewById(R.id.btnRegistrarAlimentacion).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroAlimentacionActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
            startActivity(intent);
        });
        mostrar(R.id.tvMascotaAlimentacion, getString(R.string.alimentacion_de, mascota.getNombre()));
        boolean nueva = mascota.getId() > 0
                || getIntent().getBooleanExtra(DetalleMascotaActivity.EXTRA_REGISTRO_DEMO, false);
        Alimentacion alimentacion = nueva ? null : DataSource.getAlimentacionEjemplo(mascota);
        if (alimentacion == null) {
            findViewById(R.id.tarjetaAlimentacion).setVisibility(View.GONE);
            findViewById(R.id.tvSinAlimentacion).setVisibility(View.VISIBLE);
            return;
        }
        mostrar(R.id.tvAlimento, alimentacion.getAlimento());
        mostrar(R.id.tvCantidadAlimento, alimentacion.getCantidad());
        mostrar(R.id.tvHorariosAlimento, alimentacion.getHorarios());
        mostrar(R.id.tvObservacionesAlimento, alimentacion.getObservaciones());
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto);
    }
}
