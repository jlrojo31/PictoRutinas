package com.example.tfgpictorutinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TareaDef extends AppCompatActivity {

    //variables picto(foto,archivo,picto)
    Uri imagenUri;
    ImageView picto;
    int TOMAR_FOTO = 100;
    int SELEC_IMAGEN = 200;
    int ARRASAC = 300;
    Bundle extras;
    String HORA ;

    TextView duracion;
    EditText et_descripcion;
    Button btn_actualizar;

    private DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    private long idRutina;
    private String key;
    private long idTarea;
    private String nombreRut;
    private String tarea_hora_ini;
    private String tarea_hora_fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_def);


        extras = getIntent().getExtras();
        //variables de la vista

        picto = findViewById(R.id.fotoTareaDef);

        duracion = findViewById(R.id.tiempoTareaDef_end);

        et_descripcion = findViewById(R.id.EtDescripcionTareaDef);
        btn_actualizar = findViewById(R.id.idBtActualizarTarea);


        extras = getIntent().getExtras();
        
        idRutina = extras.getLong("idRutina");
        idTarea = extras.getLong("idTarea");
        key=extras.getString("key");

        nombreRut = extras.getString("nombre");
        if(key != null ){
            showdata();
        }else{
            setHoraActual();
        }
        duracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTimePicker(v, duracion);
            }
        });

        // onclick listener para la imagen del pictograma.
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(key!=null){
                    try {
                        updateTarea(picto, duracion,et_descripcion);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    try {
                        guardardataNew(picto, duracion,et_descripcion);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    private void showdata() {
        extras = getIntent().getExtras();
        idRutina = extras.getLong("idRutina");
        idTarea = extras.getLong("idTarea");
        String tarea_picto = extras.getString("tarea_picto");
        String tarea_descripcion = extras.getString("tarea_descripcion");
        tarea_hora_ini = extras.getString("tarea_hora_ini");
        tarea_hora_fin = extras.getString("tarea_hora_end");

        byte[] imageAsBytes = Base64.decode(tarea_picto, Base64.DEFAULT);
        picto.setImageBitmap((BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));
        et_descripcion.setText(tarea_descripcion);
        try {
            texto_duracion(tarea_hora_fin,tarea_hora_ini);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        btn_actualizar.setText("Actualizar");

    }

    private void texto_duracion(String tarea_hora_end, String tarea_hora_ini) throws ParseException {
        String duracion_txt="";
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a",Locale.US);
        Date ini_hora=sdf.parse(tarea_hora_ini) ;
        Date fin_hora =sdf.parse(tarea_hora_end);
        Calendar duracion_cal = Calendar.getInstance();
        duracion_cal.setTime(fin_hora);
        duracion_cal.add(Calendar.HOUR,-ini_hora.getHours());
        duracion_cal.add(Calendar.MINUTE,-ini_hora.getMinutes());
        Date duracion_end;
        duracion_end= duracion_cal.getTime();
        SimpleDateFormat st = new SimpleDateFormat("HH:mm",Locale.US);
        int hora = duracion_end.getHours();
        int min = duracion_end.getMinutes();
        String minute_f;
        String hour_f;
        if(min<10){
            minute_f= "0"+min;
        }else {
            minute_f= String.valueOf(min);
        }
        if(hora<10){
            hour_f= "0"+hora;
        }else {
            hour_f= String.valueOf(hora);
        }
        //Showing the picked value in the textView
        //hora.setText(String.valueOf(hour)+ ":"+String.valueOf(minute)+" "+am_pm);
        String horas = hour_f+ ":"+minute_f;
        duracion.setText(horas);
    }

    private void updateTarea(ImageView picto, TextView hora_end, EditText et_descripcion) throws ParseException {

        Map<String,Object> map = new HashMap<>();
        map.put("idTarea",idTarea);
        map.put("nombreTarea",et_descripcion.getText().toString());
        map.put("fotoTarea",getBitstreamPicto( picto));
        map.put("hora_ini",tarea_hora_ini);
        map.put("hora_end",getHoraEnd());
        map.put("rutina_id",idRutina);

        mDataBase.child("pictorutinas/tareas").child(key).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(TareaDef.this,"Tarea actualizada",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TareaDef.this, EditorTareas.class);
                        i.putExtra("idRutina",idRutina);
                        i.putExtra("nombre",nombreRut);
                        Log.d("TAG",nombreRut);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TareaDef.this,"ERROR",Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private String getHoraEnd() throws ParseException {
        String hora;
        Date date1 = get_date(tarea_hora_ini);
        SimpleDateFormat st = new SimpleDateFormat("hh:mm",Locale.US);
        Date date2 = st.parse(duracion.getText().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.HOUR,date2.getHours());
        calendar.add(Calendar.MINUTE,date2.getMinutes());
        Date date_resul = calendar.getTime();
        Log.d("TAG","duracion: "+date2);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a",Locale.US);
        hora = sdf.format(date_resul);
        Log.d("TAG","hora: "+hora);
        return hora;
    }

    private boolean datoscorectos() throws ParseException {
        boolean datos_correctos;
        Date date1 = get_date(tarea_hora_ini);
        Date date2 = get_date(String.valueOf(duracion.getText()));
        if(date1.before(date2)){
            datos_correctos = true;
        }else {
            datos_correctos=false;
            Toast.makeText(TareaDef.this,"ERROR EN HORAS",Toast.LENGTH_LONG).show();
        }
        return datos_correctos;
    }
    private Date get_date(String hour) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a",Locale.US);
        Date date = sdf.parse(hour);
        return date;
    }
    private void  guardardataNew(ImageView picto, TextView hora_end, EditText et_descripcion) throws ParseException {
        String tarea_picto= getBitstreamPicto( picto);
        String tarea_descripcion= et_descripcion.getText().toString();
        if(tarea_picto.isEmpty() ||  tarea_hora_ini.isEmpty() || tarea_descripcion.isEmpty()||duracion.getText().toString().isEmpty() ){
            String error = "";
            if (tarea_picto.isEmpty())
                error="PICTOGRAMA";
            if (tarea_hora_ini.isEmpty())
                error="HORA DE INICIO";
            if (tarea_descripcion.isEmpty())
                error="DESCRIPCION";
            if (tarea_hora_fin.isEmpty())
                error="HORA DE FINALIZACIÓN";

            Toast.makeText(this,"Falta"+error+" datos por definir",Toast.LENGTH_SHORT).show();
        }else {

            Map<String,Object> map = new HashMap<>();
            map.put("idTarea",idTarea);
            map.put("nombreTarea",et_descripcion.getText().toString());
            map.put("fotoTarea",getBitstreamPicto( picto));
            map.put("hora_ini",tarea_hora_ini);
            map.put("hora_end",getHoraEnd());
            map.put("rutina_id",idRutina);

            mDataBase.child("pictorutinas/tareas").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(TareaDef.this,"Tarea guardada",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(TareaDef.this, EditorTareas.class);
                            i.putExtra("idRutina",idRutina);
                            i.putExtra("nombre",nombreRut);
                            startActivity(i);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TareaDef.this,"ERROR",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private String getBitstreamPicto(ImageView picto){
        Bitmap bitmap = ((BitmapDrawable) picto.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,5,byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String pic = Base64.encodeToString(byteArray,Base64.DEFAULT);
        return pic;
    }
    private void setHoraActual() {
        btn_actualizar.setText("Añadir tarea");
        idRutina = extras.getLong("idRutina");
        tarea_hora_ini = extras.getString("tarea_hora_ini");

    }
    public void abrirTimePicker(View v, TextView hour_) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                String minute_f;
                String hour_f;

                //añadir 00;
                if(minute<10){
                    minute_f= "0"+minute;
                }else {
                    minute_f= String.valueOf(minute);
                }
                if(hour<10){
                    hour_f= "0"+hour;
                }else {
                    hour_f= String.valueOf(hour);
                }
                //Showing the picked value in the textView
                //hora.setText(String.valueOf(hour)+ ":"+String.valueOf(minute)+" "+am_pm);
                String hora = hour_f+ ":"+minute_f;
                hour_.setText(hora);
            }
        }, 00, 00, true);

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        timePickerDialog.show();
    }
    public void pedirPermisosCamara(){
        if(ContextCompat.checkSelfPermission(TareaDef.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TareaDef.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }
    //Menu popup de imagen
    public void popupmenu(View v) {
        ImageView picto = findViewById(R.id.fotoTareaDef);
        PopupMenu popup = new PopupMenu(this, picto);
        pedirPermisosCamara();
        popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                switch (item.getItemId()) {
                    case R.id.nav_camera:
                        tomarFoto();
                        return true;
                    case R.id.nav_album:
                        seleccionarImagen();
                        return true;
                    case R.id.nav_araasac:
                        seleccionarARAASAC();
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }
    public void tomarFoto(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imagenUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
        startActivityForResult(cameraIntent, TOMAR_FOTO);
    }
    public void seleccionarImagen(){
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria,SELEC_IMAGEN);
    }
    public void seleccionarARAASAC(){
        Intent araasac = new Intent(TareaDef.this, AraasacPics.class);
        startActivityForResult(araasac, ARRASAC);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SELEC_IMAGEN) {
            imagenUri = data.getData();
            picto.setImageURI(imagenUri);
        } else if(resultCode == RESULT_OK && requestCode == TOMAR_FOTO) {
            picto.setImageURI(imagenUri);
        }else if(resultCode == RESULT_OK && requestCode == ARRASAC) {
            String imagen;
            imagen = data.getStringExtra("imagen");
            if (imagen!=null) {
                byte[] imageAsBytes = Base64.decode(imagen, Base64.DEFAULT);
                picto.setImageBitmap((BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));
            }
        }
    }
}


