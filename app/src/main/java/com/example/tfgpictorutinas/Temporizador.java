package com.example.tfgpictorutinas;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;

public class Temporizador extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)  {
        Bundle extras = intent.getExtras();
        Long idTarea;
        idTarea = extras.getLong("idTarea");
        Toast.makeText(context,"Alarma Actual",Toast.LENGTH_SHORT).show();
        if (idTarea==0){
            Intent tareaEnCurso = new Intent(context, HomeAlumno.class);
            tareaEnCurso.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(tareaEnCurso);
        }else {
            Intent tareaEnCurso = new Intent(context, TareaEnCurso.class);
            tareaEnCurso.putExtra("idTarea", idTarea);
            tareaEnCurso.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(tareaEnCurso);
        }
    }

    public void setAlarm(Context context, Calendar cal, Long idTarea){
        Intent intent = new Intent(context, Temporizador.class);
        intent.putExtra("idTarea", idTarea);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),sender);
    }
}