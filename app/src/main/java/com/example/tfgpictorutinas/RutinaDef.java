package com.example.tfgpictorutinas;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RutinaDef extends AppCompatActivity {
    private EditText tvNombre;
    private GridLayout gridRepeticiones;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("pictorutinas").child("rutinas");
    DatabaseReference myRefUsuRuti = database.getReference().child("pictorutinas").child("usuariosrutinas");

    Long idRutina;
    String nombre;
    String repeticiones;
    CheckBox cbl, cbm, cbx, cbj, cbv, cbs, cbd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_def);
        myRef.keepSynced(true);
        myRefUsuRuti.keepSynced(true);

        cbl= (CheckBox) findViewById(R.id.idCbLunes);
        cbm= (CheckBox) findViewById(R.id.idCbMartes);
        cbx= (CheckBox) findViewById(R.id.idCbMiercoles);
        cbj= (CheckBox) findViewById(R.id.idCbJueves);
        cbv= (CheckBox) findViewById(R.id.idCbViernes);
        cbs= (CheckBox) findViewById(R.id.idCbSabado);
        cbd= (CheckBox) findViewById(R.id.idCbDomingo);

        tvNombre = (EditText) findViewById(R.id.nombreRutina);
        gridRepeticiones = findViewById(R.id.idGlRepeticiones);
        if(gridRepeticiones.getVisibility()== View.VISIBLE){
            gridRepeticiones.setVisibility(View.GONE);
        }

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            nombre = extras.getString("nombre");
            idRutina = extras.getLong("idRutina");
            repeticiones = extras.getString("repeticiones");
            tvNombre.setText(nombre);
        }


        Button editarBtn = findViewById(R.id.idBtEditarRutina);

        // onclick listener para el boton editar tarea.
        editarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RutinaDef.this, EditorTareas.class);
                Bundle extras = getIntent().getExtras();
                if (extras!=null) {
                    i.putExtra("idRutina", extras.getLong("idRutina"));
                    i.putExtra("nombre", tvNombre.getText().toString());
                    i.putExtra("repeticiones", extras.getString("repeticiones"));
                }
                startActivity(i);
            }
        });

        Button participantesBtn = findViewById(R.id.idBtParticipantes);

        // onclick listener para el boton editar participantes.
        participantesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RutinaDef.this, Participantes.class);
                Bundle extras = getIntent().getExtras();
                if (extras!=null) {
                    i.putExtra("idRutina", extras.getLong("idRutina"));
                    i.putExtra("nombre", extras.getString("nombre"));
                }
                startActivity(i);
            }
        });

        Button repeticionesBtn = findViewById(R.id.idBtRepeticiones);

        // onclick listener para el boton editar repeticiones.
        repeticionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridRepeticiones = findViewById(R.id.idGlRepeticiones);
                if(gridRepeticiones.getVisibility()== View.GONE){
                    gridRepeticiones.setVisibility(View.VISIBLE);
                }else{
                    gridRepeticiones.setVisibility(View.GONE);
                }
                if (repeticiones.contains("L"))  cbl.setChecked(true);
                if (repeticiones.contains("M"))  cbm.setChecked(true);
                if (repeticiones.contains("X"))  cbx.setChecked(true);
                if (repeticiones.contains("J"))  cbj.setChecked(true);
                if (repeticiones.contains("V"))  cbv.setChecked(true);
                if (repeticiones.contains("S"))  cbs.setChecked(true);
                if (repeticiones.contains("D"))  cbd.setChecked(true);
            }
        });

        tvNombre.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Map<String, Object> update = new HashMap<>();
                            update.put("nombre", tvNombre.getText().toString());
                            DatabaseReference referencia = myRef.child(idRutina.toString());
                            referencia.keepSynced(true);
                            referencia.updateChildren(update);
                            closeSoftKeyBoard();
                            tvNombre.setFocusable(false);

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        tvNombre.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvNombre.setFocusableInTouchMode(true);
                return false;
            }
        });

        Button eliminarRutinaBtn = findViewById(R.id.idBtEliminarRutina);

        // onclick listener para el boton de eliminar rutina
        eliminarRutinaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = myRef.child(String.valueOf(idRutina));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();

                        Intent i = new Intent(RutinaDef.this, HomeActivity.class);
                        Bundle extras = getIntent().getExtras();
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
                myRefUsuRuti.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            HashMap dataHash = (HashMap) data.getValue();
                            if ((Long)dataHash.get("idRutina") == idRutina){
                                data.getRef().removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }


    public void checkone(View v){
        String dias = "";
        if(cbl.isChecked()){
            dias+="L";
        }if(cbm.isChecked()){
            dias+="M";
        }if(cbx.isChecked()){
            dias+="X";
        }if(cbj.isChecked()){
            dias+="J";
        }if(cbv.isChecked()){
            dias+="V";
        }if(cbs.isChecked()){
            dias+="S";
        }if(cbd.isChecked()){
            dias+="D";
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.keepSynced(true);
        myRef.child("pictorutinas").child("rutinas").child(String.valueOf(idRutina)).child("repeticiones").setValue(dias);
    }

    public void closeSoftKeyBoard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}