package com.udh.sops.controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.udh.sops.view.FotoMascota;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.core.content.IntentCompat;
import com.google.android.material.button.MaterialButton;
import com.udh.sops.R;
import com.udh.sops.model.Mascota;
import com.udh.sops.model.SopsDatabase;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

public class RegistroMascotaActivity extends BaseActivity {
    public static final String EXTRA_EDITAR_MASCOTA = "com.udh.sops.extra.EDITAR_MASCOTA";
    private static final String ESTADO_FECHA = "fechaNacimiento";
    private EditText nombre, raza, peso;
    private Spinner especie;
    private RadioGroup sexo;
    private MaterialButton fecha;
    private TextView error;
    private Calendar nacimiento;
    private Mascota mascotaEditar;
    private String fotoUri;
    private final ActivityResultLauncher<String[]> elegirFoto = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri == null) return;
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fotoUri = uri.toString();
                    mostrarFoto();
                } catch (SecurityException exception) {
                    Toast.makeText(this, R.string.error_elegir_foto, Toast.LENGTH_SHORT).show();
                }
            });

    private void mostrarFoto() {
        FotoMascota.mostrar(findViewById(R.id.ivFotoRegistro), fotoUri);
        findViewById(R.id.btnQuitarFoto).setVisibility(fotoUri == null ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mascotaEditar = IntentCompat.getSerializableExtra(getIntent(), EXTRA_EDITAR_MASCOTA, Mascota.class);
        if (mascotaEditar != null && mascotaEditar.getId() <= 0) { finish(); return; }
        prepararPantalla(R.layout.activity_registro_mascota, mascotaEditar == null
                ? R.string.title_registro_mascota : R.string.editar_mascota);
        fotoUri = savedInstanceState != null ? savedInstanceState.getString("fotoUri")
                : mascotaEditar == null ? null : mascotaEditar.getFotoUri();
        mostrarFoto();
        findViewById(R.id.btnElegirFoto).setOnClickListener(view -> elegirFoto.launch(new String[]{"image/*"}));
        findViewById(R.id.btnQuitarFoto).setOnClickListener(view -> { fotoUri = null; mostrarFoto(); });
        nombre = findViewById(R.id.etNombre);
        raza = findViewById(R.id.etRaza);
        peso = findViewById(R.id.etPeso);
        especie = findViewById(R.id.spEspecie);
        sexo = findViewById(R.id.rgSexo);
        fecha = findViewById(R.id.btnFechaNacimiento);
        error = findViewById(R.id.tvErrorRegistro);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.especies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        especie.setAdapter(adapter);
        if (mascotaEditar != null) {
            nombre.setText(mascotaEditar.getNombre());
            raza.setText(mascotaEditar.getRaza());
            peso.setText(mascotaEditar.getPeso());
            int posicion = adapter.getPosition(mascotaEditar.getTipo());
            especie.setSelection(Math.max(0, posicion));
            if (getString(R.string.sexo_macho).equals(mascotaEditar.getSexo())) sexo.check(R.id.rbMacho);
            else if (getString(R.string.sexo_hembra).equals(mascotaEditar.getSexo())) sexo.check(R.id.rbHembra);
            ((CheckBox) findViewById(R.id.cbEsterilizado)).setChecked(mascotaEditar.isEsterilizado());
            ((MaterialButton) findViewById(R.id.btnGuardarMascota)).setText(R.string.guardar_cambios);
            try {
                DateFormat formato = DateFormat.getDateInstance(DateFormat.MEDIUM);
                formato.setLenient(false);
                if (mascotaEditar.getFechaNacimiento() != null) {
                    nacimiento = Calendar.getInstance();
                    nacimiento.setTime(formato.parse(mascotaEditar.getFechaNacimiento()));
                    actualizarFecha();
                }
            } catch (ParseException exception) {
                nacimiento = null;
                error.setText(R.string.revisar_nacimiento);
                error.setVisibility(View.VISIBLE);
            }
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(ESTADO_FECHA)) {
            nacimiento = Calendar.getInstance();
            nacimiento.setTimeInMillis(savedInstanceState.getLong(ESTADO_FECHA));
            actualizarFecha();
        }
        fecha.setOnClickListener(view -> elegirFecha());
        findViewById(R.id.btnGuardarMascota).setOnClickListener(view -> enviar());
    }

    private void elegirFecha() {
        Calendar inicial = nacimiento == null ? Calendar.getInstance() : nacimiento;
        DatePickerDialog dialogo = new DatePickerDialog(this, (picker, anio, mes, dia) -> {
            nacimiento = Calendar.getInstance();
            nacimiento.clear();
            nacimiento.set(anio, mes, dia);
            actualizarFecha();
        }, inicial.get(Calendar.YEAR), inicial.get(Calendar.MONTH), inicial.get(Calendar.DAY_OF_MONTH));
        dialogo.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialogo.show();
    }

    private void actualizarFecha() {
        fecha.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(nacimiento.getTime()));
    }

    private boolean obligatorio(EditText campo) {
        if (campo.getText().toString().trim().isEmpty()) {
            campo.setError(getString(R.string.error_obligatorio));
            return false;
        }
        campo.setError(null);
        return true;
    }

    private void enviar() {
        boolean valido = obligatorio(nombre);
        valido = obligatorio(raza) && valido;
        boolean pesoValido = obligatorio(peso);
        if (pesoValido) {
            try {
                double valor = Double.parseDouble(peso.getText().toString().trim().replace(',', '.'));
                pesoValido = Double.isFinite(valor) && valor > 0;
            } catch (NumberFormatException exception) {
                pesoValido = false;
            }
            if (!pesoValido) peso.setError(getString(R.string.error_peso));
        }
        valido = pesoValido && valido;
        boolean seleccionCompleta = especie.getSelectedItemPosition() > 0
                && sexo.getCheckedRadioButtonId() != -1 && nacimiento != null;
        if (!valido || !seleccionCompleta) {
            error.setText(R.string.error_registro);
            error.setVisibility(View.VISIBLE);
            if (nombre.getError() != null) nombre.requestFocus();
            else if (raza.getError() != null) raza.requestFocus();
            else if (peso.getError() != null) peso.requestFocus();
            return;
        }
        error.setVisibility(View.GONE);
        Calendar hoy = Calendar.getInstance();
        int anios = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
        if (hoy.get(Calendar.MONTH) < nacimiento.get(Calendar.MONTH)
                || (hoy.get(Calendar.MONTH) == nacimiento.get(Calendar.MONTH)
                && hoy.get(Calendar.DAY_OF_MONTH) < nacimiento.get(Calendar.DAY_OF_MONTH))) anios--;
        String edad = anios == 0 ? getString(R.string.menor_un_anio)
                : getResources().getQuantityString(R.plurals.edad_anios, anios, anios);
        String sexoElegido = getString(sexo.getCheckedRadioButtonId() == R.id.rbMacho
                ? R.string.sexo_macho : R.string.sexo_hembra);
        Mascota mascota = new Mascota(nombre.getText().toString().trim(),
                especie.getSelectedItem().toString(), raza.getText().toString().trim(), edad,
                sexoElegido, ((CheckBox) findViewById(R.id.cbEsterilizado)).isChecked(),
                fecha.getText().toString(), peso.getText().toString().trim());
        mascota.setFotoUri(fotoUri);
        guardarMascota(mascota);
    }

    private void guardarMascota(Mascota mascota) {
        MaterialButton boton = findViewById(R.id.btnGuardarMascota);
        if (!boton.isEnabled()) return;
        boton.setEnabled(false);
        boton.setText(R.string.guardando_mascota);
        new Thread(() -> {
            try {
                if (mascotaEditar == null) {
                    long id = SopsDatabase.obtenerInstancia(getApplicationContext())
                            .mascotaDao().insertar(mascota);
                    mascota.setId(id);
                } else {
                    mascota.setId(mascotaEditar.getId());
                    int cambios = SopsDatabase.obtenerInstancia(getApplicationContext())
                            .mascotaDao().actualizar(mascota);
                    if (cambios != 1) throw new IllegalStateException("La mascota ya no existe");
                }
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this, mascotaEditar == null ? R.string.mascota_guardada
                            : R.string.mascota_actualizada, Toast.LENGTH_SHORT).show();
                    if (mascotaEditar == null) {
                        Intent intent = new Intent(this, DetalleMascotaActivity.class);
                        intent.putExtra(DetalleMascotaActivity.EXTRA_MASCOTA, mascota);
                        startActivity(intent);
                    }
                    finish();
                });
            } catch (Exception exception) {
                Log.e("RegistroMascota", "No se pudo guardar la mascota", exception);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    boton.setEnabled(true);
                    boton.setText(mascotaEditar == null ? R.string.enviar_mascota : R.string.guardar_cambios);
                    error.setText(R.string.error_guardar_mascota);
                    error.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("fotoUri", fotoUri);
        if (nacimiento != null) outState.putLong(ESTADO_FECHA, nacimiento.getTimeInMillis());
        super.onSaveInstanceState(outState);
    }
}
