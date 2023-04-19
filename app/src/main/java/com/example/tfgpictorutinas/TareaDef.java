package com.example.tfgpictorutinas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.io.File;

public class TareaDef extends AppCompatActivity {

    //variables camara
    Uri imagenUri;

    int TOMAR_FOTO = 100;
    int SELEC_IMAGEN = 200;

    String CARPETA_RAIZ = "MisFotosApp";
    String CARPETAS_IMAGENES = "imagenes";
    String RUTA_IMAGEN = CARPETA_RAIZ + CARPETAS_IMAGENES;
    String path;

    ImageView picto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_def);
        String nombre;
        String imagen;
        Bundle extras = getIntent().getExtras();
        picto = findViewById(R.id.fotoTareaDef);

        if (extras!=null) {
            nombre = extras.getString("idpicto");
            imagen = extras.getString("imagen");
            if (imagen!=null) {
                byte[] imageAsBytes = Base64.decode(imagen, Base64.DEFAULT);
                picto.setImageBitmap((BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));
            }
        }

        if(ContextCompat.checkSelfPermission(TareaDef.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TareaDef.this,
                    new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }



        // onclick listener para la imagen del pictograma.
        /*picto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(TareaDef.this, AraasacPics.class);
                startActivity(i);
            }
        });*/

    }

    public void popupmenu(View v) {
        ImageView picto = findViewById(R.id.fotoTareaDef);
        PopupMenu popup = new PopupMenu(this, picto);
        popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                switch (item.getItemId()) {
                    case R.id.nav_camera:
                        tomarFoto();
                        Toast.makeText(TareaDef.this, "Abriendo Camara", Toast.LENGTH_SHORT).show();
                        //cosas que hace
                        return true;
                    case R.id.nav_album:
                        seleccionarImagen();
                        Toast.makeText(TareaDef.this, "Abriendo Album", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_araasac:
                        Intent i = new Intent(TareaDef.this, AraasacPics.class);
                        startActivity(i);
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    // la imagen de camara no se muestra donde deberia pero si enra a la camara
    public void tomarFoto(){
        String nombreImagen="";
        File fileImagen = new  File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();

        if(isCreada == false){
            isCreada = fileImagen.mkdirs();
        }

        if(isCreada == true){
            nombreImagen = (System.currentTimeMillis()/1000+".jpg");
        }

        path = Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
        File imagen = new File(path);

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            String authorities = this.getPackageName()+".provider";
            Uri imageUri = FileProvider.getUriForFile(this, authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imagen));
        }

        startActivityForResult(intent,TOMAR_FOTO);
    }

    public void seleccionarImagen(){
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria,SELEC_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SELEC_IMAGEN) {
            imagenUri = data.getData();
            picto.setImageURI(imagenUri);
        } else if(resultCode == RESULT_OK && requestCode == TOMAR_FOTO) {
            MediaScannerConnection.scanFile(TareaDef.this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {

                }
            });

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            picto.setImageBitmap(bitmap);
        }
    }
}


