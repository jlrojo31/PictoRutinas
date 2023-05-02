package com.example.tfgpictorutinas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;

public class TemporizadorRegreso extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)  {

        Toast.makeText(context,"Regreso de alarma",Toast.LENGTH_SHORT).show();

        Intent homeAlumno = new Intent(context, HomeAlumno.class);
        homeAlumno.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(homeAlumno);
    }

    public void setAlarm(Context context, Calendar cal){
        Intent intent = new Intent(context, TemporizadorRegreso.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),sender);
    }
}