package com.udh.sops.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.appbar.MaterialToolbar;
import com.udh.sops.R;

/** Configuración común de las pantallas que tienen una barra para volver. */
public abstract class BaseActivity extends AppCompatActivity {
    protected void prepararPantalla(int layout, int titulo) {
        setContentView(layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contenedorPantalla), (view, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });
        MaterialToolbar barra = findViewById(R.id.toolbar);
        barra.setTitle(titulo);
        barra.setNavigationOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
    }
}
