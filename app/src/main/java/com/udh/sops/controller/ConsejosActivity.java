package com.udh.sops.controller;

import android.os.Bundle;
import com.udh.sops.R;

public class ConsejosActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepararPantalla(R.layout.activity_consejos, R.string.consejos_titulo);
    }
}
