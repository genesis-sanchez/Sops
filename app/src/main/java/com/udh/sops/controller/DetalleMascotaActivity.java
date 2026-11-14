package com.udh.sops.controller;

import android.os.Bundle;
import android.widget.TextView;
import androidx.core.content.IntentCompat;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;

public class DetalleMascotaActivity extends BaseActivity {
    public static final String EXTRA_MASCOTA = "com.udh.sops.extra.MASCOTA";

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
        mostrar(R.id.tvRazaDetalle, mascota.getRaza());
        mostrar(R.id.tvEspecieDetalle, mascota.getTipo());
        mostrar(R.id.tvEdadDetalle, mascota.getEdad());
        mostrar(R.id.tvSexoDetalle, mascota.getSexo());
        mostrar(R.id.tvEsterilizadoDetalle, getString(mascota.isEsterilizado()
                ? R.string.respuesta_si : R.string.respuesta_no));
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto);
    }
}
