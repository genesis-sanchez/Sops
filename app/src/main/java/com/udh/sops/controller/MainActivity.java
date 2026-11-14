package com.udh.sops.controller;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.udh.sops.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnVerMascotas).setOnClickListener(view ->
                startActivity(new Intent(this, ListaMascotasActivity.class)));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contenedorInicio), (view, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });
    }
}
