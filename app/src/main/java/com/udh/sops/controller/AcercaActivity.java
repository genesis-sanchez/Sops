package com.udh.sops.controller;

import android.os.Bundle;
import com.udh.sops.R;

public class AcercaActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_acerca, R.string.acerca_titulo);
    }
}
