package com.example.tfgpictorutinas;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tfgpictorutinas.firebaseRDB.Usuario;
import com.example.tfgpictorutinas.firebaseRDB.UsuariosRutinas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Participantes extends AppCompatActivity {

    ListView listViewParticipantes;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayParticipantes = new ArrayList<>();
    Long idRutina;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("pictorutinas").child("usuarios");
    DatabaseReference refUsuRutinas = database.getReference().child("pictorutinas").child("usuariosrutinas");

    ArrayList usuariosRutinas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participantes);


        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            idRutina = extras.getLong("idRutina");
        }

        listViewParticipantes = findViewById(R.id.listView_participantes);
        listViewParticipantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = view.getId();
                boolean checked = listViewParticipantes.isItemChecked(i);

                String texto = ((TextView)view).getText().toString();

                UsuariosRutinas usuRut = new UsuariosRutinas(texto, idRutina);


                refUsuRutinas.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean existe = false;
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            HashMap dataHash = (HashMap) data.getValue();

                            if (idRutina==((Long) dataHash.get("idRutina"))&& texto.equals((String) dataHash.get("nombreUsuario"))){
                                existe = true;
                                if (!checked){
                                    Query query = refUsuRutinas.orderByChild("idNombre").equalTo(texto+ String.valueOf(idRutina));
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                appleSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e(TAG, "onCancelled", databaseError.toException());
                                        }
                                    });
                                }
                                break;
                            };
                        }
                        if (!existe && checked) {
                            refUsuRutinas.push().setValue(usuRut);
                        }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }
        });

        refUsuRutinas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    usuariosRutinas.add(dataHash.get("idNombre"));
                }
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
                        for (int i=0;i<arrayParticipantes.size();i++) {
                            if (usuariosRutinas.contains(arrayParticipantes.get(i).toString()+ String.valueOf(idRutina)))
                                listViewParticipantes.setItemChecked(i,true);
                        }
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

    /*@Override
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
                    View wantedView = listViewParticipantes.getChildAt(i);

                    refUsuRutinas.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean existe = false;
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                HashMap dataHash = (HashMap) data.getValue();
                                if (idRutina.equals((String) dataHash.get("idRutina"))&&listViewParticipantes.getItemAtPosition(i).){
                                    existe = true;
                                    break;
                                };
                            }
                            if (!existe) myRef.push().setValue(usu);
                            } else {
                                refUsuRutinas.push().setValue(())// No user is signed in
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    itemSelected += listViewParticipantes.getItemAtPosition(i) + "\n";
                }else{

                }
            }
        }
        return super.onOptionsItemSelected(item);
    }*/
}