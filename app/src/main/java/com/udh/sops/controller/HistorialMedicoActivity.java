package com.udh.sops.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.IntentCompat;
import com.udh.sops.R;
import com.udh.sops.model.DataSource;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.RegistroMedico;

public class HistorialMedicoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_historial_medico, R.string.historial_titulo);
        Mascota mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null) { finish(); return; }
        ((TextView) findViewById(R.id.tvMascotaHistorial)).setText(
                getString(R.string.historial_de, mascota.getNombre()));
        if (getIntent().getBooleanExtra(DetalleMascotaActivity.EXTRA_REGISTRO_DEMO, false)) {
            findViewById(R.id.tvSinHistorial).setVisibility(View.VISIBLE);
            return;
        }
        LinearLayout registros = findViewById(R.id.contenedorRegistrosMedicos);
        for (RegistroMedico registro : DataSource.getHistorialEjemplo(mascota)) {
            View tarjeta = getLayoutInflater().inflate(R.layout.item_registro_medico, registros, false);
            ((TextView) tarjeta.findViewById(R.id.tvMotivoConsulta)).setText(registro.getMotivo());
            ((TextView) tarjeta.findViewById(R.id.tvFechaConsulta)).setText(registro.getFecha());
            ((TextView) tarjeta.findViewById(R.id.tvVeterinarioConsulta)).setText(registro.getVeterinario());
            ((TextView) tarjeta.findViewById(R.id.tvObservacionesConsulta)).setText(registro.getObservaciones());
            registros.addView(tarjeta);
        }
    }
}
