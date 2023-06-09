package com.example.tfgpictorutinas;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tfgpictorutinas.firebaseRDB.RutinasTareas;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class EditorTareas extends AppCompatActivity {
    int TAREA = 300;
    RecyclerView recyclerView;
    AdaptadorTareas adaptadorTareas;

    ArrayList<Tarea> listaTareas = new ArrayList();
    long autoincrementid = 0;
    TextView hora_ini_rut;
    long id_rutina;

    Button new_tarea;
    TextView nombrerutina;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_tareas);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refTareas = database.getReference().child("pictorutinas").child("tareas");
        refTareas.keepSynced(true);
        nombrerutina = findViewById(R.id.nombreRut);
        new_tarea = findViewById(R.id.idBtnNuevaTarea);
        //hora_ini_rut= findViewById(R.id.defTimeRutina);
        recyclerView = findViewById(R.id.listaTareas);

        Bundle extras;
        extras = getIntent().getExtras();
        id_rutina = extras.getLong("idRutina");
        String nombre = get_name();
        nombrerutina.setText(nombre);
        // Get a reference to your user
// Attach a listener to read the data at your profile reference
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
                        if (adaptadorTareas.getItemCount() > 0) {
                            autoincrementid = ((Tarea)listaTareas.get(listaTareas.size()-1)).getIdTarea();
                            long id_new_tarea = autoincrementid+1;
                            Intent intent = new Intent(EditorTareas.this, TareaDef.class);

                            intent.putExtra("is_old",0);
                            intent.putExtra("idTarea",id_new_tarea);
                            intent.putExtra("idRutina", id_rutina);
                            intent.putExtra("nombre",nombrerutina.getText());
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
                            String horaAntfin = adaptadorTareas.getItem(adaptadorTareas.getItemCount()-1).getHora_end();
                            intent.putExtra("tarea_hora_ini",horaAntfin);
                            Log.d("TAG",horaAntfin);
                            startActivityForResult(intent, TAREA);
                        }else {
                            if (listaTareas.isEmpty()) autoincrementid = autoincrementid + 1;
                            else autoincrementid = ((Tarea)listaTareas.get(listaTareas.size()-1)).getIdTarea();
                            long id_new_tarea = autoincrementid+1;
                            Intent intent = new Intent(EditorTareas.this, TareaDef.class);
                            intent.putExtra("is_old",0);
                            intent.putExtra("idTarea",id_new_tarea);
                            intent.putExtra("idRutina", id_rutina);
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a",Locale.US);
                            String horaAntfin = "00:00 AM";
                            intent.putExtra("tarea_hora_ini",horaAntfin);
                            Log.d("TAG",horaAntfin);
                            startActivityForResult(intent, TAREA);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
       /* hora_ini_rut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTimePicker(v,hora_ini_rut);
            }
        });*/
        populateAdapter();
    }

    private void populateAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Tarea> options=
                new FirebaseRecyclerOptions.Builder<Tarea>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("pictorutinas").child("tareas").orderByChild("rutina_id").equalTo(id_rutina), Tarea.class)
                        .build();
        this.adaptadorTareas = new AdaptadorTareas(options,EditorTareas.this);
        recyclerView.setAdapter(adaptadorTareas);
    }
    public String get_name(){
        String name = " ";
        DatabaseReference r2 = FirebaseDatabase.getInstance().getReference()
                .child("pictorutinas").child("rutinas").child(String.valueOf(id_rutina));
        r2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("nombre").getValue(String.class);
                nombrerutina.setText(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
        return name;
    }
    /* private void abrirTimePicker(View v, TextView hour_) {
         TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
             @Override
             public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                 String am_pm = "AM";
                 String minute_f;
                 if(hour>12){
                     am_pm = "PM";
                     hour = hour-12;
                 }if(hour==12){
                     am_pm= "PM";
                 }
                 //añadir 00;
                 if(minute == 0){
                     minute_f= "00";
                 }else {
                     minute_f= String.valueOf(minute);
                 }
                 //Showing the picked value in the textView
                 //hora.setText(String.valueOf(hour)+ ":"+String.valueOf(minute)+" "+am_pm);
                 String hora = hour+ ":"+minute_f+" "+am_pm;
                 hour_.setText(hora);
                 try {
                     actualizarHoras();
                 } catch (ParseException e) {
                     throw new RuntimeException(e);
                 }
             }
         }, 12, 00, false);
         timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

         timePickerDialog.show();
     }

     public void actualizarHoras() throws ParseException {

         if(adaptadorTareas.getItemCount()>0 && !hora_ini_rut.getText().toString().isEmpty()) {
             SimpleDateFormat sdf = new SimpleDateFormat("h:mm a",Locale.US);
             Query query = FirebaseDatabase.getInstance().getReference()
                     .child("pictorutinas").child("tareas")
                     .orderByChild("rutina_id").equalTo(id_rutina);

             Date hora_inicial = sdf.parse(hora_ini_rut.getText().toString());
             Map<String, Object> update = new HashMap<>();
             Log.d("TAG","////////////////CALL ACTUALIZAR ///////////");
             query.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     int count= 0;
                     Date ini_hora=null ;
                     Date fin_hora =null;
                     Date duracion ;
                     Date ini_old;
                     Date end_old;
                     for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                         DatabaseReference referencia =
                                 FirebaseDatabase.getInstance().getReference().child("pictorutinas")
                                         .child("tareas").child(childSnapshot.getRef().getKey());
                         referencia.keepSynced(true);
                         try {
                             ini_old =sdf.parse(adaptadorTareas.getItem(count).getHora_ini());
                             end_old =sdf.parse(adaptadorTareas.getItem(count).getHora_end());
                             duracion = new Date(end_old.getTime()-ini_old.getTime());
                             Log.d("TAG","ini_old: "+ini_old.toString());
                             Log.d("TAG","end_old: "+end_old.toString());
                             Log.d("TAG","duracion_old: "+duracion);

                             if (count==0){
                                 ini_hora = hora_inicial;

                                 fin_hora = new Date(ini_hora.getTime()+duracion.getTime());
                                 if(sdf.format(ini_hora)!= ini_old.toString())
                                     update.put("hora_ini", sdf.format(ini_hora));
                                 if(sdf.format(fin_hora)!= fin_hora.toString())
                                     update.put("hora_end",sdf.format(fin_hora));
                                 Log.d("TAG","fin_hora_0: "+fin_hora.toString());
                                 Log.d("TAG","ini_hora_0: "+ini_hora.toString());
                                 Log.d("TAG","duracion_hora_0: "+duracion.toString());
                             }
                             else {
                                 ini_hora=fin_hora;
                                 Calendar cal= Calendar.getInstance();
                                 cal.setTime(ini_hora);
                                 cal.add(Calendar.MINUTE,1);
                                 ini_hora = (cal.getTime());
                                 fin_hora = new Date(ini_hora.getTime()+duracion.getTime());
                                 if(sdf.format(ini_hora)!= ini_old.toString())
                                     update.put("hora_ini", sdf.format(ini_hora));
                                 if(sdf.format(fin_hora)!= fin_hora.toString())
                                     update.put("hora_end",sdf.format(fin_hora));
                                 Log.d("TAG","fin_hora: "+fin_hora.toString());
                                 Log.d("TAG","ini_hora: "+ini_hora.toString());
                                 Log.d("TAG","duracion: "+duracion.toString());
                             }
                             count++;
                             referencia.updateChildren(update);
                             Log.d("TAG", childSnapshot.getKey());

                         } catch (ParseException e) {
                             throw new RuntimeException(e);
                         }

                     }
                 }
                 @Override
                 public void onCancelled(DatabaseError databaseError) {
                     // Getting User failed, log a message
                     Log.w("firebase", "Error getting data", databaseError.toException());
                     // ...
                 }
             });
         }else {

         }
     }
     public void setHora() {
             String date_ini_rut = adaptadorTareas.getItem(0).getHora_ini();
             Log.d("TAG", "sethora"+date_ini_rut);
             hora_ini_rut.setText(date_ini_rut);
     }*/
    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.getRecycledViewPool().clear();
        adaptadorTareas.notifyDataSetChanged();
        adaptadorTareas.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adaptadorTareas.stopListening();
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == TAREA) {
            id_rutina = data.getIntExtra("idRutina",-1);
        }
    }

}