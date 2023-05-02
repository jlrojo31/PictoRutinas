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
    String hora_actual;
    String hora_inicio;
    String hora_final;
    ArrayList<Tarea> tareas = new ArrayList<>();
    ArrayList<Tarea> tareasListado = new ArrayList<>();
    Temporizador alarmaActual;
    AdaptadorTareasVisionAlumno adaptadorTareasVisionAlumno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_alumno);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        cal.set (Calendar.HOUR_OF_DAY, 20);
        cal.set (Calendar.MINUTE, 12);
        cal.set (Calendar.SECOND, 0);
        //dd.setAlarm(this, cal); //OJO BUENO DESCOMENTAR ESTO PARA ALARMA
        /*Intent intentoLanzar = new Intent(getBaseContext(), Temporizador.class);
        PendingIntent pIntent=PendingIntent.getBroadcast(this, 0, intentoLanzar, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT );

        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int a = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int b = calendar.get(Calendar.MINUTE);        // gets hour in 12h format
        int c = calendar.get(Calendar.MONTH);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        cal.set (Calendar.HOUR_OF_DAY, 16);
        cal.set (Calendar.MINUTE, 40);
        cal.set (Calendar.SECOND, 0);

        AlarmManager aMan = (AlarmManager)getSystemService(ALARM_SERVICE);
        aMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);
*/


        /*LocalDateTime locaDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            locaDate = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                    .ofPattern("hh:mm a");
            hora_actual = dateTimeFormatter.format(locaDate);
        }*/

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            usuActual = usuario.getDisplayName();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refUsuRutinas = database.getReference().child("pictorutinas").child("usuariosrutinas");
        DatabaseReference refRutinas = database.getReference().child("pictorutinas").child("rutinas");
        DatabaseReference refTareas = database.getReference().child("pictorutinas").child("tareas");
        String day = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String aux = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL,new Locale("es","ES"));
            day = aux.toUpperCase().charAt(0) + aux.substring(1, aux.length());
        }
        TextView diaActual = (TextView) findViewById(R.id.idDiaTV);
        diaActual.setText(day);

        ListView list = (ListView) findViewById(R.id.listaTareasAlumno);
        this.adaptadorTareasVisionAlumno = new AdaptadorTareasVisionAlumno(this, tareas);

        //TODO FALTARIA CONSULTAR LAS RUTINAS PARA VER LAS REPETICIONES Y SABER SI EL DIA ACTUAL HAY QUE PONER ESA RUTINA O NO.
        Query queryTarea = refTareas.orderByChild("rutina_id");
        queryTarea.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap dataHash = (HashMap) data.getValue();
                    Tarea aux = new Tarea((Long)dataHash.get("idTarea"),(String)dataHash.get("nombreTarea") ,(String)dataHash.get("fotoTarea"),(String)dataHash.get("hora_ini"),(String)dataHash.get("hora_end"),(Long) dataHash.get("rutina_id"));
                    tareasListado.add(aux);

                    Query queryUsuRut = refUsuRutinas.orderByChild("nombreUsuario").equalTo(usuActual);
                    queryUsuRut.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                HashMap dataUsuRut = (HashMap) appleSnapshot.getValue();
                                if ((Long)dataUsuRut.get("idRutina") == (Long)dataHash.get("rutina_id")){
                                    Tarea aux = new Tarea((Long)dataHash.get("idTarea"),(String)dataHash.get("nombreTarea") ,(String)dataHash.get("fotoTarea"),(String)dataHash.get("hora_ini"),(String)dataHash.get("hora_end"),(Long) dataHash.get("rutina_id"));
                                    tareas.add(aux);
                                }

                            }
                            adaptadorTareasVisionAlumno = new AdaptadorTareasVisionAlumno(HomeAlumno.this, tareas);
                            list.setAdapter(adaptadorTareasVisionAlumno);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });

                }
                if (tareasListado.size()!=0) activarAlarma(tareasListado);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void activarAlarma(ArrayList<Tarea> tareas){
        //TODO FALTARIA COMPROBAR SI LA TAREA SE ENCUENTRA DENTRO DE LA HORA ACTUAL O CUAL ES LA QUE COMIENZA ANTES
        // AHORA MISMO ÚNICAMENTE PONE ALARMA A LA PRIMERA TAREA QUE ENCUENTRA.
        Calendar calen = Calendar.getInstance();
        calen.setTimeInMillis(System.currentTimeMillis());
        int hour = calen.get(Calendar.HOUR);
        int minutes = calen.get(Calendar.MINUTE);
        int am_pm = calen.get(Calendar.AM_PM);
        String amPMS;
        if (am_pm==1)  amPMS="PM"; else amPMS="AM";


        String[] partesHora = tareas.get(0).getHora_ini().split(":");
        String hora = partesHora[0];
        String[] partesMinutos = partesHora[1].split(" ");
        String minutos = partesMinutos[0];
        String ampm = partesMinutos[1];
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        alarmaActual = new Temporizador();
        cal.set (Calendar.HOUR, Integer.valueOf(hora)-2);
        cal.set (Calendar.MINUTE, Integer.valueOf(minutos));
        if (ampm.equals("AM")) cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set (Calendar.SECOND, 0);
        alarmaActual.setAlarm(HomeAlumno.this,cal,tareas.get(0).idTarea);
    }

}