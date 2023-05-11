package com.example.tfgpictorutinas;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class HomeAlumno extends AppCompatActivity {

    String usuActual;
    int tamRutinas = 0;
    int tamTareas = 0;
    int limite = 0;
    int limiteTareas = 0;
    ArrayList<Tarea> tareas = new ArrayList<>();
    ArrayList<Tarea> tareasListado = new ArrayList<>();
    HashMap<Long,String> listaRutinas = new HashMap<>();
    Temporizador alarmaActual;
    AdaptadorTareasVisionAlumno adaptadorTareasVisionAlumno;
    String hoy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_alumno);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        cal.set (Calendar.HOUR_OF_DAY, 20);
        cal.set (Calendar.MINUTE, 12);
        cal.set (Calendar.SECOND, 0);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            usuActual = usuario.getDisplayName();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refUsuRutinas = database.getReference().child("pictorutinas").child("usuariosrutinas");
        DatabaseReference refRutinas = database.getReference().child("pictorutinas").child("rutinas");
        DatabaseReference refTareas = database.getReference().child("pictorutinas").child("tareas");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String aux = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL,new Locale("es","ES"));
            hoy = aux.toUpperCase().charAt(0) + aux.substring(1, aux.length());
        }
        TextView diaActual = (TextView) findViewById(R.id.idDiaTV);
        diaActual.setText(hoy);

        ListView list = (ListView) findViewById(R.id.listaTareasAlumno);
        this.adaptadorTareasVisionAlumno = new AdaptadorTareasVisionAlumno(this, tareas);

        Query queryRutina = refRutinas.orderByChild("idRutina");
        queryRutina.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataRutinas: dataSnapshot.getChildren()){
                    HashMap dataHashRutinas = (HashMap) dataRutinas.getValue();
                    listaRutinas.put((Long)dataHashRutinas.get("idRutina"), (String)dataHashRutinas.get("repeticiones"));

                    Query queryTarea = refTareas.orderByChild("rutina_id");
                    queryTarea.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            limite = limite + 1;
                            if (limite == tamRutinas){
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    HashMap dataHash = (HashMap) data.getValue();
                                    Tarea aux = new Tarea((Long) dataHash.get("idTarea"), (String) dataHash.get("nombreTarea"), (String) dataHash.get("fotoTarea"), (String) dataHash.get("hora_ini"), (String) dataHash.get("hora_end"), (Long) dataHash.get("rutina_id"));
                                    tareasListado.add(aux);

                                    Query queryUsuRut = refUsuRutinas.orderByChild("nombreUsuario").equalTo(usuActual);
                                    queryUsuRut.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            limiteTareas = limiteTareas + 1;
                                            if (limiteTareas == tamTareas){
                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                    HashMap dataUsuRut = (HashMap) appleSnapshot.getValue();
                                                    //if ((Long) dataUsuRut.get("idRutina") == (Long) dataHash.get("rutina_id")) {
                                                        //if (listaRutinas.containsKey((Long) dataHash.get("rutina_id"))) {
                                                            String repe = listaRutinas.get((Long) dataUsuRut.get("idRutina"));
                                                            if (repe.contains("" + hoy.charAt(0))) {
                                                                for (Tarea tar:tareasListado){
                                                                    if (tar.getRutina_id()==(Long) dataUsuRut.get("idRutina")) tareas.add(tar);
                                                                }
                                                                //Tarea aux = new Tarea((Long) dataHash.get("idTarea"), (String) dataHash.get("nombreTarea"), (String) dataHash.get("fotoTarea"), (String) dataHash.get("hora_ini"), (String) dataHash.get("hora_end"), (Long) dataHash.get("rutina_id"));
                                                                //tareas.add(aux);
                                                            }
                                                       // }
                                                    //}

                                                }
                                            adaptadorTareasVisionAlumno = new AdaptadorTareasVisionAlumno(HomeAlumno.this, tareas);
                                            list.setAdapter(adaptadorTareasVisionAlumno);
                                            if (tareas.size() != 0) activarAlarma(tareas);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e(TAG, "onCancelled", databaseError.toException());
                                        }
                                    });
                                tamTareas=tamTareas+1;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    tamRutinas = tamRutinas +1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void activarAlarma(ArrayList<Tarea> tareas){
        Calendar calen = Calendar.getInstance();
        calen.setTimeInMillis(System.currentTimeMillis());
        int hour = calen.get(Calendar.HOUR);
        int minutes = calen.get(Calendar.MINUTE);
        int am_pm = calen.get(Calendar.AM_PM);
        String amPMS;
        Date horaPosteriorFormato = null;
        String horaPosterior = "";
        Long tareaPosterior = 0L;
        boolean primero = false;
        if (am_pm==1)  amPMS="PM"; else amPMS="AM";

        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date dateActual, dateIni, dateFin;

        boolean encontrado = false;
        for (Tarea tarea:tareas) {
            try {
                dateActual = dateFormat.parse(hour + ":" + minutes + " " + amPMS);
                dateIni = dateFormat.parse(tarea.hora_ini);
                dateFin = dateFormat.parse(tarea.hora_end);

                if ((dateIni.compareTo(dateActual) <= 0) && (dateFin.compareTo(dateActual) > 0)) {
                    String[] partesHora = tarea.getHora_ini().split(":");
                    String hora = partesHora[0];
                    String[] partesMinutos = partesHora[1].split(" ");
                    String minutos = partesMinutos[0];
                    String ampm = partesMinutos[1];
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    alarmaActual = new Temporizador();
                    if (Integer.valueOf(hora)==12) hora = "0";
                    cal.set (Calendar.HOUR, Integer.valueOf(hora));
                    cal.set (Calendar.MINUTE, Integer.valueOf(minutos));
                    if (ampm.equals("AM")) cal.set(Calendar.AM_PM, Calendar.AM);
                    else cal.set(Calendar.AM_PM, Calendar.PM);
                    cal.set (Calendar.SECOND, 0);
                    alarmaActual.setAlarm(HomeAlumno.this,cal,tarea.getIdTarea());
                    encontrado = true;
                    break;
                }else{
                    if ((dateIni.compareTo(dateActual) >= 0)) {
                        if (horaPosteriorFormato == null) {
                            horaPosteriorFormato = dateIni;
                            horaPosterior = tarea.hora_ini;
                            tareaPosterior = tarea.getIdTarea();
                        } else if (horaPosteriorFormato.compareTo(dateIni) >= 0) {
                            horaPosteriorFormato = dateIni;
                            horaPosterior = tarea.hora_ini;
                            tareaPosterior = tarea.getIdTarea();
                        }
                    }else {//No hay más tareas posteriores
                        if (horaPosteriorFormato == null) {
                            Date diaPosterior = dateFormat.parse("11:59 PM");
                            horaPosteriorFormato = diaPosterior;
                            horaPosterior = "11:59 PM";
                            tareaPosterior = 0L;
                        }
                    }

                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        if (!encontrado){
            String[] partesHora = horaPosterior.split(":");
            String hora = partesHora[0];
            String[] partesMinutos = partesHora[1].split(" ");
            String minutos = partesMinutos[0];
            String ampm = partesMinutos[1];
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            alarmaActual = new Temporizador();
            if (Integer.valueOf(hora)==12) hora = "0";
            cal.set (Calendar.HOUR, Integer.valueOf(hora));
            cal.set (Calendar.MINUTE, Integer.valueOf(minutos));
            if ( tareaPosterior==0L) cal.set(Calendar.SECOND,59); else cal.set(Calendar.SECOND,0);
            if (ampm.equals("AM")) cal.set(Calendar.AM_PM, Calendar.AM);
            else cal.set(Calendar.AM_PM, Calendar.PM);
            alarmaActual.setAlarm(HomeAlumno.this,cal,tareaPosterior);
        }
    }

}