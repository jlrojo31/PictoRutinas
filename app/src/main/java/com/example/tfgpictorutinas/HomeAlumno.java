package com.example.tfgpictorutinas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

public class HomeAlumno extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_alumno);

        String day = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String aux = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL,new Locale("es","ES"));
            day = aux.toUpperCase().charAt(0) + aux.substring(1, aux.length());
        }
        TextView diaActual = (TextView) findViewById(R.id.idAlumnoTV);
        diaActual.setText(day);
    }
}