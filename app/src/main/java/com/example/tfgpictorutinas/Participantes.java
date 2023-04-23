package com.example.tfgpictorutinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tfgpictorutinas.firebaseRDB.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Participantes extends AppCompatActivity {

    ListView listViewParticipantes;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayParticipantes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participantes);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("pictorutinas").child("usuarios");

        listViewParticipantes = findViewById(R.id.listView_participantes);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    Usuario aux = new Usuario((String)dataHash.get("nombre"),(String)dataHash.get("email") ,(boolean)dataHash.get("administrador"));
                    arrayParticipantes.add(aux.getNombre());
                }
                adapter = new ArrayAdapter<String>(Participantes.this, android.R.layout.simple_list_item_multiple_choice,arrayParticipantes);
                listViewParticipantes.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.participantes_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_done){
            String itemSelected = "";
            for (int i=0;i<listViewParticipantes.getCount();i++){
                if(listViewParticipantes.isItemChecked(i)){
                    itemSelected += listViewParticipantes.getItemAtPosition(i) + "\n";
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}