package com.example.tfgpictorutinas;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;

public class TareaEnCurso extends AppCompatActivity {

    Long idTarea;
    TemporizadorRegreso alarmaRegreso;

    TextToSpeech tts;

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

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sd_alert3);
        mediaPlayer.start();

        idTareaEnCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsFunction();
            }
        });

        fotoTareaEnCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsFunction();
            }
        });

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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ttsFunction() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale locSpanish = new Locale("spa", "ESP");
                    int result = tts.setLanguage(locSpanish);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "Lenguaje no soportado", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.v(TAG, "onInit exitoso");
                        TextView mTextView = (TextView) findViewById(R.id.idTareaEnCurso);

                        String strTexto = mTextView.getText().toString();
                        leerTexto(strTexto);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Falló la inicialización", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void leerTexto(String strTexto){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.v(TAG, "API 21+");
            Bundle bundle = new Bundle();
            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
            tts.speak(strTexto, TextToSpeech.QUEUE_FLUSH, bundle, null);
        } else {
            Log.v(TAG, "API 15-");
            HashMap<String, String> param = new HashMap<>();
            param.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
            tts.speak(strTexto, TextToSpeech.QUEUE_FLUSH, param);
        }
    }
}