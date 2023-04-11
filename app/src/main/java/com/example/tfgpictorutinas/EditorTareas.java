package com.example.tfgpictorutinas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EditorTareas extends AppCompatActivity {
    private TextView tvNombre;

    AdaptadorTareas adapter;
    ArrayList<Tarea> listaTareas = new ArrayList(30);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_tareas);

        Bundle extras = getIntent().getExtras();
        tvNombre = (TextView) findViewById(R.id.nombreRut);
        if (extras!=null) {
            String nombre = extras.getString("nombre");
            tvNombre.setText(nombre);
        }


        //Generación de listView
        ListView list = (ListView) findViewById(R.id.listaTareas);
        this.adapter = new AdaptadorTareas(this, this.listaTareas);

        // boton de NuevaTarea
        Button NuevaTareaBtn = findViewById(R.id.idBtnNuevaTarea);

        // onclick listener para el boton de NuevaTarea
        NuevaTareaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tarea t = new Tarea(listaTareas.size()+1,"Tarea" + (listaTareas.size()+1), "" + R.drawable.alumno); //@todo Revisar
                listaTareas.add(t);
                list.setAdapter(adapter);
            }
        });
    }
}