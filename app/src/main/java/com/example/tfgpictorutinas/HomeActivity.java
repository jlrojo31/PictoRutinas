package com.example.tfgpictorutinas;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfgpictorutinas.R;
import com.example.tfgpictorutinas.firebaseRDB.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Adaptador adapter;
    ArrayList<Rutina> listaRutinas = new ArrayList(30);
    long autoincrementid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getIntent().setAction("Already created");

        //Generación de listView
        ListView list = (ListView) findViewById(R.id.lista);
        this.adapter = new Adaptador(this, this.listaRutinas);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("pictorutinas").child("rutinas");

        //MIO
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            // User is signed in
            String name = usuario.getDisplayName();
            String email = usuario.getEmail();
            Uri photoUrl = usuario.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = usuario.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = usuario.getUid();
        } else {
            // No user is signed in
        }
        //MIO

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    Rutina aux = new Rutina((Long)dataHash.get("idRutina"),(String)dataHash.get("nombre") ,(String)dataHash.get("foto"), (String)dataHash.get("repeticiones"));
                    listaRutinas.add(aux);
                }
                if (listaRutinas.size() > 0) autoincrementid = ((Rutina)listaRutinas.get(listaRutinas.size()-1)).getIdRutina();
                adapter = new Adaptador(HomeActivity.this, listaRutinas);
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Rutina> lR = new ArrayList(30);
                for(DataSnapshot data: snapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    Rutina aux = new Rutina((Long)dataHash.get("idRutina"),(String)dataHash.get("nombre") ,(String)dataHash.get("foto"), (String)dataHash.get("repeticiones"));
                    lR.add(aux);
                }
                if (lR.size() > 0) autoincrementid = ((Rutina)lR.get(lR.size()-1)).getIdRutina();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                View str = ((LinearLayout)arg1).getChildAt(1);
                Rutina aux = listaRutinas.get(position);
                long id = aux.getIdRutina();
                String texto = ((TextView)str).getText().toString();
                String repeticiones = aux.getRepeticiones();

                Intent i = new Intent(HomeActivity.this, RutinaDef.class);
                i.putExtra("idRutina", id);
                i.putExtra("nombre", texto);
                i.putExtra("repeticiones",repeticiones);
                startActivity(i);
            }
        });

        // boton de aniadir
        Button AniadirBtn = findViewById(R.id.idBtnAniadir);

        // onclick listener para el boton de aniadir
        AniadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rutina a = new Rutina(autoincrementid+1,"Rutina" + (autoincrementid+1), "" + R.drawable.alumno, ""); //@todo Revisar
                listaRutinas.add(a);
                myRef.child(String.valueOf(autoincrementid+1)).setValue(a);
                list.setAdapter(adapter);
                Toast.makeText(HomeActivity.this,"Se ha añadido rutina a la lista",Toast.LENGTH_SHORT).show();
            }
        });

        // boton de aniadir
        Button GABtn = findViewById(R.id.idBtnGA);

        // onclick listener para el boton de aniadir
        GABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, Alumnos.class);
                startActivity(i);
            }
        });


        // boton de logout
        Button logoutBtn = findViewById(R.id.idBtnLogout);

        // onclick listener para el boton de logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // below line is for getting instance
                // for AuthUi and after that calling a
                // sign out method from FIrebase.
                AuthUI.getInstance()
                        .signOut(HomeActivity.this)

                        // after sign out is executed we are redirecting
                        // our user to MainActivity where our login flow is being displayed.
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                // below method is used after logout from device.
                                Toast.makeText(HomeActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                                // below line is to go to MainActivity via an intent.
                                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });

    }

    @Override
    public void onResume() {
        String action = getIntent().getAction();
        if(action == null || !action.equals("Already created")) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            getIntent().setAction(null);

        super.onResume();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}