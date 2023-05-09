package com.example.tfgpictorutinas;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;

public class TemporizadorRegreso extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)  {

        Toast.makeText(context,"Regreso de alarma",Toast.LENGTH_SHORT).show();

        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.sd_alert4);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                Intent homeAlumno = new Intent(context, HomeAlumno.class);
                homeAlumno.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(homeAlumno);
            }
        });
        mediaPlayer.start();

    }

    public void setAlarm(Context context, Calendar cal){
        Intent intent = new Intent(context, TemporizadorRegreso.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),sender);
    }
}