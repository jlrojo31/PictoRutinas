package com.example.tfgpictorutinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        ToggleButton repeticionesBtn = findViewById(R.id.tgAdminAlum);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            nombre = extras.getString("usuario");
        }

        Button AdminAlumBtn = findViewById(R.id.idBtnAdminAlum);

        // onclick listener para el boton de aniadir
        AdminAlumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeticionesBtn.isChecked()) admin=true; else admin=false;

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            String key= data.getKey().toString();
                            HashMap dataHash = (HashMap) data.getValue();
                            if (nombre.equals((String) dataHash.get("nombre"))){
                                Map<String, Object> update = new HashMap<>();
                                update.put("administrador", admin);
                                DatabaseReference referencia = myRef.child(key);
                                referencia.updateChildren(update);
                                break;
                            };
                        }
                        if (admin){
                            Intent i = new Intent(AdministradorAlumno.this, HomeActivity.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(AdministradorAlumno.this, HomeAlumno.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}