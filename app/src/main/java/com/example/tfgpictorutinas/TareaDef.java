package com.example.tfgpictorutinas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TareaDef extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_def);

        String nombre;
        String imagen;
        Bundle extras = getIntent().getExtras();
        ImageView picto = findViewById(R.id.fotoTareaDef);
        if (extras!=null) {
            nombre = extras.getString("idpicto");
            imagen = extras.getString("imagen");
            if (imagen!=null) {
                byte[] imageAsBytes = Base64.decode(imagen, Base64.DEFAULT);
                picto.setImageBitmap((BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));
            }
        }



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