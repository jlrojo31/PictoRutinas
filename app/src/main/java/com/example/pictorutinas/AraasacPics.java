package com.example.pictorutinas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
                getImagenes(postService, searchView.getQuery().toString());
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
}