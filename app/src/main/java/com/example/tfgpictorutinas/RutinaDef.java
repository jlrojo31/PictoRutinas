package com.example.tfgpictorutinas;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

    Long idRutina;
    String nombre;
    String repeticiones;
    CheckBox cbl, cbm, cbx, cbj, cbv, cbs, cbd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_def);

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
                    i.putExtra("nombre", extras.getString("nombre"));
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


        // onclick listener para el boton editar tarea.
        tvNombre.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Map<String, Object> update = new HashMap<>();
                    update.put("nombre", tvNombre.getText().toString());
                    DatabaseReference referencia = myRef.child(idRutina.toString());
                    referencia.updateChildren(update);
                    return true;
                }
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
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
        myRef.child("pictorutinas").child("rutinas").child(String.valueOf(idRutina)).child("repeticiones").setValue(dias);
    }
}