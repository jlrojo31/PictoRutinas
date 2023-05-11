package com.example.tfgpictorutinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.tfgpictorutinas.firebaseRDB.RutinasTareas;
import com.example.tfgpictorutinas.firebaseRDB.UsuariosRutinas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HorarioAlumno extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("pictorutinas").child("usuariosrutinas");
    DatabaseReference myRefRutinas = database.getReference().child("pictorutinas").child("rutinas");

    AdaptadorHorariosAlumno adapter;
    ArrayList<Long> idRutinas = new ArrayList<>();
    ArrayList<String> rutina = new ArrayList<>();
    ArrayList<String> dias = new ArrayList<>();
    String alumno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_alumno);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            alumno = extras.getString("alumno");
        }
        ListView list = (ListView) findViewById(R.id.horarioalumnoLV);
        this.adapter = new AdaptadorHorariosAlumno(this, this.rutina, this.dias);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    if (((String)dataHash.get("nombreUsuario")).equals(alumno))
                        idRutinas.add((Long)dataHash.get("idRutina"));
                }

                myRefRutinas.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){

                            HashMap dataHash = (HashMap) data.getValue();
                            if (idRutinas.contains((Long)dataHash.get("idRutina"))){
                                rutina.add((String)dataHash.get("nombre"));
                                dias.add((String)dataHash.get("repeticiones"));
                            }
                        }

                        adapter = new AdaptadorHorariosAlumno(HorarioAlumno.this, rutina, dias);
                        list.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}