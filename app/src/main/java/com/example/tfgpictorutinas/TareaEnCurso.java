package com.example.tfgpictorutinas;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;

public class TareaEnCurso extends AppCompatActivity {

    Long idTarea;
    TemporizadorRegreso alarmaRegreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_en_curso);

        ImageView fotoTareaEnCurso = (ImageView) findViewById(R.id.fotoTareaEnCurso);
        TextView idTareaEnCurso = (TextView) findViewById(R.id.idTareaEnCurso);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            idTarea = extras.getLong("idTarea");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refTareas = database.getReference().child("pictorutinas").child("tareas");

        Query queryTarea = refTareas.orderByChild("idTarea").equalTo(idTarea);
        queryTarea.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tarea aux = new Tarea();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    aux = new Tarea((Long)dataHash.get("idTarea"),(String)dataHash.get("nombreTarea") ,(String)dataHash.get("fotoTarea"),(String)dataHash.get("hora_ini"),(String)dataHash.get("hora_end"),(Long) dataHash.get("rutina_id"));
                    idTareaEnCurso.setText(aux.getNombreTarea());
                    byte[] decodedString = Base64.decode(aux.getFotoTarea(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    fotoTareaEnCurso.setImageBitmap(decodedByte);
                }
                String[] partesHora = aux.getHora_end().split(":");
                String hora = partesHora[0];
                String[] partesMinutos = partesHora[1].split(" ");
                String minutos = partesMinutos[0];
                String ampm = partesMinutos[1];
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                alarmaRegreso = new TemporizadorRegreso();
                cal.set (Calendar.HOUR, Integer.valueOf(hora)-2);
                cal.set (Calendar.MINUTE, Integer.valueOf(minutos));
                if (ampm.equals("AM")) cal.set(Calendar.AM_PM, Calendar.AM);
                cal.set (Calendar.SECOND, 0);
                alarmaRegreso.setAlarm(TareaEnCurso.this,cal);

                /*Calendar actual = Calendar.getInstance();
                actual.setTimeInMillis(System.currentTimeMillis());
                int horaActual = actual.get(Calendar.HOUR_OF_DAY);
                int horaFin = cal.get(Calendar.HOUR_OF_DAY);
                int diferenciaHoras = (horaFin-horaActual) *60;
                int minutosActual = actual.get(Calendar.MINUTE);
                int minutosFin = cal.get(Calendar.MINUTE);
                int diferenciaMinutos = minutosFin-minutosActual;
                int minutosTotal = diferenciaHoras+diferenciaMinutos;
                int reduccion = Integer.valueOf(minutosTotal/4);
                for (int i=0;i<3;i++){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Toast ddd= new Toast(TareaEnCurso.this);
                    ddd.setView((ImageView) findViewById(R.id.fotoTareaEnCurso));
                    ddd.setGravity(Gravity.LEFT|Gravity.LEFT,0,0);
                    ddd.setDuration(Toast.LENGTH_LONG);
                    ddd.show();
                    Toast toast2 = Toast.makeText(TareaEnCurso.this,"Regreso de alarma",Toast.LENGTH_SHORT);
                    toast2.setDuration(Toast.LENGTH_LONG);
                    toast2.setGravity(Gravity.CENTER|Gravity.LEFT,0,0);

                    toast2.show();
                }*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}