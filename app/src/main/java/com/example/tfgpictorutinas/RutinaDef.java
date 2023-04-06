package com.example.tfgpictorutinas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RutinaDef extends AppCompatActivity {
    private TextView tvNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_def);

        Bundle extras = getIntent().getExtras();
        tvNombre = (TextView) findViewById(R.id.nombreRutina);
        if (extras!=null) {
            String nombre = extras.getString("nombre");
            tvNombre.setText(nombre);
        }

    }
}