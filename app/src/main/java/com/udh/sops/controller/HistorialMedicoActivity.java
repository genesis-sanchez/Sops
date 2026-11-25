package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.IntentCompat;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;
import com.udh.sops.model.EntradaHistorial;
import com.udh.sops.model.HistorialMedico;
import com.udh.sops.util.HistorialPdf;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class HistorialMedicoActivity extends BaseActivity {
    private boolean cargado, generando;
    private final ActivityResultLauncher<String> guardarPdf = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("application/pdf"), uri -> {
                if (uri != null) generarPdf(uri, false);
            });
    private Mascota mascota;
    private LinearLayout registros;
    private TextView estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_historial_medico, R.string.historial_titulo);
        mascota = IntentCompat.getSerializableExtra(getIntent(),
                DetalleMascotaActivity.EXTRA_MASCOTA, Mascota.class);
        if (mascota == null || mascota.getId() <= 0) {
            Toast.makeText(this, R.string.guardar_mascota_primero, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        findViewById(R.id.btnExportarHistorial).setOnClickListener(view -> guardarPdf.launch("historial_mascota_" + mascota.getId() + ".pdf"));
        findViewById(R.id.btnCompartirHistorial).setOnClickListener(view -> generarPdf(null, true));
        registros = findViewById(R.id.contenedorRegistrosMedicos);
        estado = findViewById(R.id.tvSinHistorial);
        findViewById(R.id.btnRegistrarVisita).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroVisitaActivity.class);
            intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
            startActivity(intent);
        });
        ((TextView) findViewById(R.id.tvMascotaHistorial)).setText(
                getString(R.string.historial_de, mascota.getNombre()));
    }

    @Override protected void onResume() {
        super.onResume();
        if (mascota != null && mascota.getId() > 0) cargarHistorial();
    }

    private void cargarHistorial() {
        cargado = false;
        actualizarBotones();
        registros.removeAllViews();
        estado.setText(R.string.cargando_historial);
        estado.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                List<EntradaHistorial> entradas = HistorialMedico.cargar(
                        SopsDatabase.obtenerInstancia(getApplicationContext()), mascota.getId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    registros.removeAllViews();
                    for (EntradaHistorial entrada : entradas) mostrarEntrada(entrada);
                    cargado = true;
                    actualizarBotones();
                    estado.setText(R.string.sin_historial);
                    estado.setVisibility(entradas.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception exception) {
                Log.e("HistorialMedico", "No se pudieron cargar las visitas", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    estado.setText(R.string.error_cargar_historial);
                    estado.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void mostrarEntrada(EntradaHistorial entrada) {
        View tarjeta = getLayoutInflater().inflate(R.layout.item_entrada_historial, registros, false);
        ((TextView) tarjeta.findViewById(R.id.tvTipoEntrada)).setText(entrada.tipo);
        ((TextView) tarjeta.findViewById(R.id.tvTituloEntrada)).setText(entrada.titulo);
        ((TextView) tarjeta.findViewById(R.id.tvFechaEntrada)).setText(entrada.fecha == null
                || entrada.fecha.trim().isEmpty() ? getString(R.string.dato_sin_registrar) : entrada.fecha);
        ((TextView) tarjeta.findViewById(R.id.tvDetalleEntrada)).setText(entrada.detalle);
        tarjeta.setOnClickListener(view -> {
            Intent intent;
            if ("vacuna".equals(entrada.tipo)) {
                intent = new Intent(this, DetalleVacunaActivity.class);
                intent.putExtra(DetalleVacunaActivity.EXTRA_VACUNA_ID, entrada.registroId);
            } else if ("visita".equals(entrada.tipo)) {
                intent = new Intent(this, DetalleVisitaActivity.class);
                intent.putExtra(DetalleVisitaActivity.EXTRA_VISITA_ID, entrada.registroId);
            } else {
                intent = new Intent(this, DetalleMedicamentoActivity.class);
                intent.putExtra(DetalleMedicamentoActivity.EXTRA_MEDICAMENTO_ID, entrada.registroId);
            }
            startActivity(intent);
        });
        registros.addView(tarjeta);
    }

    private void actualizarBotones() {
        findViewById(R.id.btnExportarHistorial).setEnabled(cargado && !generando);
        findViewById(R.id.btnCompartirHistorial).setEnabled(cargado && !generando);
    }

    private void generarPdf(Uri destino, boolean compartir) {
        if (mascota == null || generando) return;
        generando = true;
        actualizarBotones();
        new Thread(() -> {
            try {
                SopsDatabase base = SopsDatabase.obtenerInstancia(getApplicationContext());
                Mascota actual = base.mascotaDao().obtenerPorId(mascota.getId());
                if (actual == null) throw new IllegalStateException("La mascota ya no existe");
                List<EntradaHistorial> entradas = HistorialMedico.cargar(base, actual.getId());
                File archivo = null;
                if (compartir) {
                    File carpeta = new File(getCacheDir(), "historiales");
                    if (!carpeta.exists() && !carpeta.mkdirs()) throw new java.io.IOException("No se pudo crear la carpeta");
                    archivo = File.createTempFile("historial_" + actual.getId() + "_", ".pdf", carpeta);
                }
                try (OutputStream salida = compartir ? new FileOutputStream(archivo)
                        : getContentResolver().openOutputStream(destino, "wt")) {
                    if (salida == null) throw new java.io.IOException("No se pudo abrir el archivo");
                    HistorialPdf.escribir(actual, entradas, salida);
                }
                File resultado = archivo;
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    generando = false;
                    actualizarBotones();
                    if (compartir) {
                        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".archivos", resultado);
                        Intent enviar = new Intent(Intent.ACTION_SEND).setType("application/pdf");
                        enviar.putExtra(Intent.EXTRA_STREAM, uri);
                        enviar.setClipData(android.content.ClipData.newRawUri("historial", uri));
                        enviar.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(enviar, getString(R.string.compartir_historial)));
                    } else Toast.makeText(this, R.string.historial_exportado, Toast.LENGTH_SHORT).show();
                });
            } catch (Exception exception) {
                Log.e("HistorialMedico", "No se pudo generar el PDF", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    generando = false;
                    actualizarBotones();
                    Toast.makeText(this, R.string.error_exportar_historial, Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
