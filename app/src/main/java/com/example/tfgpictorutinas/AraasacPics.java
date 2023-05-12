package com.example.tfgpictorutinas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfgpictorutinas.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AraasacPics extends AppCompatActivity {
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_araasac_pics);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.urlaraasac))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        AraasacApi postService = retrofit.create(AraasacApi.class);

        searchView = findViewById(R.id.Busqueda);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                for (int i = 0; i < 8; i++) {
                    String nombre = getString(R.string.tvimagen) + i;
                    int resID = getResources().getIdentifier(nombre, getString(R.string.id), getPackageName());
                    ImageView imgView = findViewById(resID);
                    imgView.setImageResource(0);
                }
                getImagenes(postService, searchView.getQuery().toString().trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }


    private void getImage(AraasacApi postService, String id, int pantalla) {
        Call<ResponseBody> call = postService.getImage(id, "false");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStream is = response.body().byteStream();
                Bitmap png = BitmapFactory.decodeStream(is);
                ImageView imgView = findViewById(pantalla);
                imgView.setImageBitmap(png);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imgView.setTooltipText(id);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getImagenes(AraasacApi postService, String query) {
        Call<Resultado[]> call = postService.getBestSearch(getString(R.string.locale), query);
        call.enqueue(new Callback<Resultado[]>() {
            @Override
            public void onResponse(Call<Resultado[]> call, Response<Resultado[]> response) {
                Resultado[] result = response.body();
                if (result!=null) {
                    for (int i = 0; i < result.length && i < 8; i++) {
                        String nombre = getString(R.string.tvimagen) + i;
                        int resID = getResources().getIdentifier(nombre, getString(R.string.id), getPackageName());
                        getImage(postService, result[i].getId().toString(), resID);
                    }
                }else{
                    Toast.makeText(AraasacPics.this, getString(R.string.sinresultados), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Resultado[]> call, Throwable t) {
                System.out.println(getString(R.string.error));
            }
        });
    }


    public void clickPicto(View v){
            switch(v.getId()){
                case R.id.tvImagen0:
                case R.id.tvImagen1:
                case R.id.tvImagen2:
                case R.id.tvImagen3:
                case R.id.tvImagen4:
                case R.id.tvImagen5:
                case R.id.tvImagen6:
                case R.id.tvImagen7:
                    Intent i = new Intent();
                    Bundle extras = getIntent().getExtras();
                    ImageView imgView = findViewById(v.getId());
                    Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String encode = Base64.encodeToString(byteArray,Base64.DEFAULT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        i.putExtra("idPicto", v.getTooltipText());
                    }
                    i.putExtra("imagen", encode);
                    setResult(RESULT_OK, i);
                    finish();

                    break;
                default:
                    break;
            }
                /*Intent i = new Intent(RutinaDef.this, EditorTareas.class);
                Bundle extras = getIntent().getExtras();
                if (extras!=null) {
                    i.putExtra("idRutina", extras.getInt("idRutina"));
                    i.putExtra("nombre", extras.getString("nombre"));
                }
                startActivity(i);*/
        }
}