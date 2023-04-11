package com.example.tfgpictorutinas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


        Button editarBtn = findViewById(R.id.idBtEditarRutina);

        // onclick listener para el boton editar tarea.
        editarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RutinaDef.this, EditorTareas.class);
                Bundle extras = getIntent().getExtras();
                if (extras!=null) {
                    i.putExtra("idRutina", extras.getInt("idRutina"));
                    i.putExtra("nombre", extras.getString("nombre"));
                }
                startActivity(i);
            }
        });

    }
}