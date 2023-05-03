package com.example.tfgpictorutinas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfgpictorutinas.R;
import com.example.tfgpictorutinas.firebaseRDB.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    // variable for Firebase Auth
    private FirebaseAuth mFirebaseAuth;

    // declaring a const int value which we
    // will be using in Firebase auth.
    public static final int RC_SIGN_IN = 1;
    boolean administrador = true;
    // creating an auth listener for our Firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    // below is the list which we have created in which
    // we can add the authentication which we have to
    // display inside our app.
    List<AuthUI.IdpConfig> providers = Arrays.asList(

            // below is the line for adding
            // email and password authentication.
            new AuthUI.IdpConfig.EmailBuilder().build(),

            // below line is used for adding google
            // authentication builder in our app.
            new AuthUI.IdpConfig.GoogleBuilder().build());

    String name = "";
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // below line is for getting instance
        // for our firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        // below line is used for calling auth listener
        // for our Firebase authentication.
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // we are calling method for on authentication state changed.
                // below line is used for getting current user which is
                // authenticated previously.
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // checking if the user
                // is null or not.
                if (user != null) {
                    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                    if (usuario != null) {
                        // User is signed in
                        name = usuario.getDisplayName();
                        email = usuario.getEmail();
                    } else {
                        // No user is signed in
                    }

                    //Firebase RealTime Database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("pictorutinas").child("usuarios");

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean existe = false;
                            // MIRAR SI NO EXISTE USUARIO EN LA BBDD Y AÑADIRLO
                            name = usuario.getDisplayName();
                            email = usuario.getEmail();
                            Usuario usu = new Usuario(name,email,administrador);
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                HashMap dataHash = (HashMap) data.getValue();
                                if (email.equals((String) dataHash.get("email"))){
                                    existe = true;
                                    administrador = (boolean)dataHash.get("administrador");
                                    break;
                                };
                            }
                            if (!existe) myRef.push().setValue(usu);

                            if (administrador) {
                                Intent i = new Intent(MainActivity.this, AdministradorAlumno.class);
                                i.putExtra("usuario", name);
                                startActivity(i);
                            }else{
                                Intent i = new Intent(MainActivity.this, HomeAlumno.class);
                                startActivity(i);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //Fin Firebase RealTime Database
                    // we are calling finish method to kill or
                    // mainactivity which is displaying our login ui.
                    finish();
                } else {
                    // this method is called when our
                    // user is not authenticated previously.
                    startActivityForResult(
                            // below line is used for getting
                            // our authentication instance.
                            AuthUI.getInstance()
                                    // below line is used to
                                    // create our sign in intent
                                    .createSignInIntentBuilder()

                                    // below line is used for adding smart
                                    // lock for our authentication.
                                    // smart lock is used to check if the user
                                    // is authentication through different devices.
                                    // currently we are disabling it.
                                    .setIsSmartLockEnabled(false)

                                    // we are adding different login providers which
                                    // we have mentioned above in our list.
                                    // we can add more providers according to our
                                    // requirement which are available in firebase.
                                    .setAvailableProviders(providers)

                                    // below line is for customizing our theme for
                                    // login screen and set logo method is used for
                                    // adding logo for our login page.
                                    .setLogo(R.drawable.logo).setTheme(R.style.Theme)

                                    // below line is for customizing our theme for
                                    // login screen and set logo method is used for
                                    // adding logo for our login page.
                                    .setTheme(R.style.Theme)

                                    // after setting our theme and logo
                                    // we are calling a build() method
                                    // to build our login screen.
                                    .build(),
                            // and lastly we are passing our const
                            // integer which is declared above.
                            RC_SIGN_IN
                    );
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we are calling our auth
        // listener method on app resume.
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // here we are calling remove auth
        // listener method on stop.
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}