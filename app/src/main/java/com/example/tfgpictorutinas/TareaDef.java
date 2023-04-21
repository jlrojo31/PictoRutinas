package com.example.tfgpictorutinas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Base64;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TareaDef extends AppCompatActivity {

    //variables picto(foto,archivo,picto)
    Uri imagenUri;
    ImageView picto;
    int TOMAR_FOTO = 100;
    int SELEC_IMAGEN = 200;
    int ARRASAC = 300;
    Bundle extras;
    String HORA ;

    TextView hora_ini;
    TextView hora_end;
    EditText et_descripcion;
    Button btn_actualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_def);

        extras = getIntent().getExtras();
        //variables de la vista

        picto = findViewById(R.id.fotoTareaDef);

        hora_ini = findViewById(R.id.tiempoTareaDef_ini);
        hora_end = findViewById(R.id.tiempoTareaDef_end);

        et_descripcion = findViewById(R.id.EtDescripcionTareaDef);
        btn_actualizar = findViewById(R.id.idBtActualizarTarea);

        setHoraActual();
        hora_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTimePicker(v,hora_ini);
            }
        });
        hora_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTimePicker(v,hora_end);
            }
        });

        // onclick listener para la imagen del pictograma.
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String descripcion = et_descripcion.getText().toString().trim();
                String aDate = hora_ini.getText().toString();

            }
        });
    }
    private void  guardardata(ImageView picto, TextView hora_end, TextView hora_ini, EditText et_descripcion,String rutina){
        String tarea_picto= getBitstreamPicto( picto);
        String tarea_hora_ini= hora_ini.getText().toString();
        String tarea_hora_end= hora_end.getText().toString();
        String tarea_descripcion= et_descripcion.getText().toString();
        if(tarea_picto.isEmpty() ||  tarea_hora_ini.isEmpty() || tarea_hora_end.isEmpty() || tarea_descripcion.isEmpty() || rutina.isEmpty()){
            String error = "";
            if (tarea_picto.isEmpty())
                error="PICTOGRAMA";
            if (tarea_picto.isEmpty())
                error="HORA DE INICIO";
            if (tarea_picto.isEmpty())
                error="HORA DE FINALIZACIÓN";
            if (tarea_picto.isEmpty())
                error="DESCRIPCION";
            if (tarea_picto.isEmpty())
                error="RUTINA";
            Toast.makeText(this,"Falta"+error+" datos por definir",Toast.LENGTH_SHORT).show();
        }
    }
    private String getBitstreamPicto(ImageView picto){
        Bitmap bitmap = ((BitmapDrawable) picto.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String pic = Base64.encodeToString(byteArray,Base64.DEFAULT);
        return pic;
    }
    private void setHoraActual() {
        Date date = new Date(String.valueOf(Calendar.getInstance().getTime()));
        DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        hora_ini.setText(dateFormat.format(date));
        hora_end.setText(dateFormat.format(date));
    }
    public void abrirTimePicker(View v, TextView hour_) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String am_pm = "AM";
                if(hour>12){
                    am_pm = "PM";
                    hour = hour-12;
                }
                //Showing the picked value in the textView
                //hora.setText(String.valueOf(hour)+ ":"+String.valueOf(minute)+" "+am_pm);
                String hora = hour+ ":"+minute+" "+am_pm;
                hour_.setText(hora);
            }
        }, 12, 30, false);

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


