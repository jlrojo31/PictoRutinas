package com.example.tfgpictorutinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.example.tfgpictorutinas.firebaseRDB.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AdministradorAlumno extends AppCompatActivity {

    String nombre;
    boolean admin = true;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("pictorutinas").child("usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador_alumno);
        myRef.keepSynced(true);

        ImageButton AdminBtn = findViewById(R.id.idBtnAdmin);
        ImageButton AlumBtn = findViewById(R.id.idBtnAlum);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            nombre = extras.getString("usuario");
        }

        AdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            String key= data.getKey().toString();
                            HashMap dataHash = (HashMap) data.getValue();
                            if (nombre.equals((String) dataHash.get("nombre"))){
                                Map<String, Object> update = new HashMap<>();
                                update.put("administrador", true);
                                DatabaseReference referencia = myRef.child(key);
                                referencia.keepSynced(true);
                                referencia.updateChildren(update);
                                break;
                            };
                        }
                        Intent i = new Intent(AdministradorAlumno.this, HomeActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        AlumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            String key= data.getKey().toString();
                            HashMap dataHash = (HashMap) data.getValue();
                            if (nombre.equals((String) dataHash.get("nombre"))){
                                Map<String, Object> update = new HashMap<>();
                                update.put("administrador", false);
                                DatabaseReference referencia = myRef.child(key);
                                referencia.keepSynced(true);
                                referencia.updateChildren(update);
                                break;
                            };
                        }
                        Intent i = new Intent(AdministradorAlumno.this, HomeAlumno.class);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}