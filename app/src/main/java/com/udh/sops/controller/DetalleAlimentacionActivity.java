package com.udh.sops.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.udh.sops.R;
import com.udh.sops.model.Alimentacion;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;

public class DetalleAlimentacionActivity extends BaseActivity {
    public static final String EXTRA_ALIMENTACION_ID = "com.udh.sops.extra.ALIMENTACION_ID";
    private long alimentacionId;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_detalle_alimentacion, R.string.detalle_alimentacion_titulo);
        alimentacionId = getIntent().getLongExtra(EXTRA_ALIMENTACION_ID, 0);
        if (alimentacionId <= 0) {
            Toast.makeText(this, R.string.alimentacion_no_encontrada, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (alimentacionId <= 0) return;
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                Alimentacion registro = base.alimentacionDao().obtenerPorId(alimentacionId);
                Mascota mascota = registro == null ? null : base.mascotaDao().obtenerPorId(registro.getMascotaId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (registro == null || mascota == null) {
                        Toast.makeText(this, R.string.alimentacion_no_encontrada, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    mostrar(R.id.tvNombreDetalleAlimentacion, registro.getNombreAlimento());
                    mostrar(R.id.tvMascotaDetalleAlimentacion, getString(R.string.alimentacion_de, mascota.getNombre()));
                    mostrar(R.id.tvTipoDetalleAlimentacion, registro.getTipoAlimento());
                    String cantidad = registro.getCantidad();
                    if (cantidad != null && !cantidad.trim().isEmpty()
                            && registro.getUnidad() != null && !registro.getUnidad().trim().isEmpty()) {
                        cantidad += " " + registro.getUnidad();
                    }
                    mostrar(R.id.tvCantidadDetalleAlimentacion, cantidad);
                    mostrar(R.id.tvHorariosDetalleAlimentacion, registro.getHorarios());
                    mostrar(R.id.tvFrecuenciaDetalleAlimentacion, registro.getVecesAlDia() > 0
                            ? getResources().getQuantityString(R.plurals.veces_al_dia_guardadas,
                                    registro.getVecesAlDia(), registro.getVecesAlDia()) : null);
                    mostrar(R.id.tvObservacionesDetalleAlimentacion, registro.getObservaciones());
                    mostrar(R.id.tvInicioDetalleAlimentacion, registro.getFechaInicio());
                    mostrar(R.id.tvFinDetalleAlimentacion, registro.getFechaFin());
                    mostrar(R.id.tvEstadoDetalleAlimentacion, getString(registro.isActivo()
                            ? R.string.alimentacion_activa : R.string.alimentacion_inactiva));
                    findViewById(R.id.tvEstadoCargaAlimentacion).setVisibility(View.GONE);
                    findViewById(R.id.contenedorDetalleAlimentacion).setVisibility(View.VISIBLE);
                });
            } catch (Exception exception) {
                Log.e("DetalleAlimentacion", "No se pudo cargar la alimentacion", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    mostrar(R.id.tvEstadoCargaAlimentacion, getString(R.string.error_cargar_detalle_alimentacion));
                    findViewById(R.id.tvEstadoCargaAlimentacion).setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void mostrar(int id, String texto) {
        ((TextView) findViewById(id)).setText(texto == null || texto.trim().isEmpty()
                ? getString(R.string.dato_sin_registrar) : texto);
    }
}
