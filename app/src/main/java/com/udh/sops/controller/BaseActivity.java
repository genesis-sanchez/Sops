package com.udh.sops.controller;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.appbar.MaterialToolbar;
import com.udh.sops.R;


public abstract class BaseActivity extends AppCompatActivity {
    protected void prepararPantalla(int layout, int titulo) {
        setContentView(layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contenedorPantalla), (view, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            view.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });
        MaterialToolbar barra = findViewById(R.id.toolbar);
        barra.setTitle(titulo);
        barra.setNavigationOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        prepararMenu(barra);
    }

    protected void prepararMenu(MaterialToolbar barra) {
        barra.inflateMenu(R.menu.menu_principal);
        barra.setOnMenuItemClickListener(item -> {
            Class<?> destino;
            if (item.getItemId() == R.id.menuInicio) destino = MainActivity.class;
            else if (item.getItemId() == R.id.menuConsejos) destino = ConsejosActivity.class;
            else if (item.getItemId() == R.id.menuAcerca) destino = AcercaActivity.class;
            else if (item.getItemId() == R.id.menuMascotas) destino = ListaMascotasActivity.class;
            else if (item.getItemId() == R.id.menuRegistro) destino = RegistroMascotaActivity.class;
            else return false;

            if (getClass() != destino) {
                Intent intent = new Intent(this, destino);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            return true;
        });
    }
}
