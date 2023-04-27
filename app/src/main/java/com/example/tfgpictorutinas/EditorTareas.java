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

    Button new_tarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*String nombre = "";
        HashMap<Long,Long> tareasOrden = new HashMap<>();
        tareasOrden.put(-1L,-1L);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_tareas);/*

        Bundle extras = getIntent().getExtras();
        tvNombre = (TextView) findViewById(R.id.nombreRut);
        if (extras!=null) {
            idRutina = extras.getLong("idRutina");
            nombre = extras.getString("nombre");
            tvNombre.setText(nombre);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefTareas = database.getReference().child("pictorutinas").child("tareas");
        DatabaseReference refRT = database.getReference().child("pictorutinas").child("rutinastareas");
        //Generación de listView
        ListView list = (ListView) findViewById(R.id.listaTareas);
        this.adapter = new AdaptadorTareas(this, this.listaTareas);

        refRT.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    if ((Long)dataHash.get("idRutina")==idRutina) tareasOrden.put((Long)dataHash.get("idTarea"),(Long)dataHash.get("orden"));
                }
                myRefTareas.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            int orden = 0;
                            HashMap dataHash = (HashMap) data.getValue();
                            Tarea aux = new Tarea((Long)dataHash.get("idTarea"),(String)dataHash.get("nombreTarea") ,(String)dataHash.get("fotoTarea"),(String)dataHash.get("hora_ini"),(String)dataHash.get("hora_end"),(Long) dataHash.get("rutina"));
                            if (tareasOrden.containsKey((Long)dataHash.get("idTarea"))) {
                                orden = tareasOrden.get((Long)dataHash.get("idTarea")).intValue();
                                listaTareasHash.put(orden, aux);
                            }
                            listaTareas = new ArrayList<Tarea>(listaTareasHash.values());
                            if ((Long)dataHash.get("idTarea") > autoincrementid) autoincrementid = (Long)dataHash.get("idTarea");
                        }
                        listaTareas = new ArrayList<Tarea>(listaTareasHash.values());
                        /*if (listaTareas.size() > 0) autoincrementid = ((Tarea)listaTareas.get(listaTareas.size()-1)).getIdTarea();comentar
                        adapter = new AdaptadorTareas(EditorTareas.this, listaTareas);
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


        myRefTareas.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Tarea> lR = new ArrayList(30);
                for(DataSnapshot data: snapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    Tarea aux = new Tarea((Long)dataHash.get("idTarea"),(String)dataHash.get("nombreTarea") ,(String)dataHash.get("fotoTarea"),(String)dataHash.get("hora_ini"),(String)dataHash.get("hora_end"),(Long) dataHash.get("rutina"));
                    lR.add(aux);
                }
                if (lR.size()>0) autoincrementid = ((Tarea)lR.get(lR.size()-1)).getIdTarea();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // boton de NuevaTarea
        Button NuevaTareaBtn = findViewById(R.id.idBtnNuevaTarea);

        // onclick listener para el boton de NuevaTarea
        NuevaTareaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = "SS";
                Tarea t = new Tarea(autoincrementid+1,"Tarea" + (autoincrementid+1), "" + R.drawable.alumno,"g","",idRutina); //@todo Revisar
                listaTareas.add(t);
                int tam = listaTareas.size();
                myRefTareas.child(String.valueOf(autoincrementid+1)).setValue(t);
                //myRefTareas.push().setValue(t);
                RutinasTareas rT = new RutinasTareas(idRutina,autoincrementid+1,tam-1);
                refRT.push().setValue(rT);
                list.setAdapter(adapter);
                Intent i = new Intent(EditorTareas.this, TareaDef.class);
                i.putExtra("idRutina", idRutina); // Todo habra que dejar un id de bbdd para pasar a la siguiente pantalla
                i.putExtra("idTarea", Long.valueOf(autoincrementid+1)); // Todo habra que dejar un id de bbdd para pasar a la siguiente pantalla
                startActivity(i);
            }
        });*/
        ///MIS prubas recuerda comentar

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
                long id_new_tarea = adaptadorTareas.getItemCount()+1;
                Intent intent = new Intent(EditorTareas.this, TareaDef.class);

                intent.putExtra("is_old",0);
                intent.putExtra("idTarea",id_new_tarea);
                intent.putExtra("idRutina", id_rutina);

                startActivity(intent);
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