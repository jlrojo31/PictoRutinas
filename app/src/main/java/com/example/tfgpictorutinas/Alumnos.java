package com.example.tfgpictorutinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tfgpictorutinas.firebaseRDB.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Alumnos extends AppCompatActivity {

    AdaptadorAlumnos adapter;
    ArrayList<Usuario> listaAlumnos = new ArrayList(30);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);

        //Generación de listView
        ListView list = (ListView) findViewById(R.id.alumnosLV);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("pictorutinas").child("usuarios");
        myRef.keepSynced(true);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    Usuario aux = new Usuario((String)dataHash.get("nombre"),(String)dataHash.get("email") ,(boolean)dataHash.get("administrador"));
                    listaAlumnos.add(aux);
                }
                adapter = new AdaptadorAlumnos(Alumnos.this, listaAlumnos);
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                View str = ((LinearLayout)arg1).getChildAt(1);
                String texto = ((TextView)str).getText().toString();

                Intent i = new Intent(Alumnos.this, HorarioAlumno.class);
                i.putExtra("alumno", texto);
                startActivity(i);
            }
        });
    }
}