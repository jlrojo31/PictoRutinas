package com.example.tfgpictorutinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tfgpictorutinas.firebaseRDB.RutinasTareas;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class EditorTareas extends AppCompatActivity {
   /* private TextView tvNombre;
    AdaptadorTareas adapter;
    ArrayList<Tarea> listaTareas = new ArrayList<>(50);
    ArrayList<RutinasTareas> listaRutinasTareas = new ArrayList(30);
    TreeMap listaTareasHash = new TreeMap<Integer,Tarea>();
    long autoincrementid = 0;
    long idRutina = 0;*/

//imo
    RecyclerView recyclerView;
    AdaptadorTareas adaptadorTareas;

    ArrayList<Tarea> listaTareas = new ArrayList();
    long autoincrementid = 0;
    Button new_tarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_tareas);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refTareas = database.getReference().child("pictorutinas").child("tareas");

        new_tarea = findViewById(R.id.idBtnNuevaTarea);

        recyclerView = findViewById(R.id.listaTareas);
        final long id_rutina;
        Bundle extras;
        extras = getIntent().getExtras();
        id_rutina = extras.getLong("idRutina");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Tarea> options=
                new FirebaseRecyclerOptions.Builder<Tarea>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("pictorutinas").child("tareas").orderByChild("rutina_id").equalTo(id_rutina), Tarea.class)
                        .build();
        this.adaptadorTareas = new AdaptadorTareas(options);
        recyclerView.setAdapter(adaptadorTareas);

        long finalId_rutina = id_rutina;
        new_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refTareas.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            HashMap dataHash = (HashMap) data.getValue();
                            Tarea aux = new Tarea((Long)dataHash.get("idTarea"),(String)dataHash.get("nombreTarea") ,(String)dataHash.get("fotoTarea"),(String)dataHash.get("hora_ini"),(String)dataHash.get("hora_end"),(Long) dataHash.get("rutina_id"));
                            listaTareas.add(aux);
                        }
                        if (listaTareas.size() > 0) autoincrementid = ((Tarea)listaTareas.get(listaTareas.size()-1)).getIdTarea();
                        long id_new_tarea = autoincrementid+1;
                        Intent intent = new Intent(EditorTareas.this, TareaDef.class);

                        intent.putExtra("is_old",0);
                        intent.putExtra("idTarea",id_new_tarea);
                        intent.putExtra("idRutina", id_rutina);

                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        adaptadorTareas.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adaptadorTareas.stopListening();
    }

}