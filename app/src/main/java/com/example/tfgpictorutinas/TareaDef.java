package com.example.tfgpictorutinas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TareaDef extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_def);

        ImageView picto = findViewById(R.id.fotoTareaDef);

        // onclick listener para la imagen del pictograma.
        picto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TareaDef.this, AraasacPics.class);
                startActivity(i);
            }
        });
    }
}