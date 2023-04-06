package com.example.tfgpictorutinas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfgpictorutinas.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Adaptador adapter;
    ArrayList<Rutina> listaRutinas = new ArrayList(50);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        //Generación de listView
        ListView list = (ListView) findViewById(R.id.lista);
        this.adapter = new Adaptador(this, this.listaRutinas);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                //Object o = listView.getItemAtPosition(position);
                // Realiza lo que deseas, al recibir clic en el elemento de tu listView determinado por su posicion.
                Toast.makeText(HomeActivity.this, "pulsado", Toast.LENGTH_SHORT).show();

            }
        });

        // boton de aniadir
        Button AniadirBtn = findViewById(R.id.idBtnAniadir);

        // onclick listener para el boton de logout
        AniadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rutina a = new Rutina(listaRutinas.size()+1,"Rutina" + (listaRutinas.size()+1), "" + R.drawable.alumno); //@todo Revisar
                listaRutinas.add(a);
                list.setAdapter(adapter);
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

        // boton de araasac
        Button araasacBtn = findViewById(R.id.idBtnAraasac);

        // onclick listener para el boton araasac.
        araasacBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AraasacPics.class);
                startActivity(i);
            }
        });
    }
}