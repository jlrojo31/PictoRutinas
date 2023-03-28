package com.example.tfgpictorutinas;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AraasacApi {
    @GET("pictograms/{idPictogram}")
    Call<ResponseBody> getImage(@Path("idPictogram") String id, @Query("url") String url);

    @GET("pictograms/{locale}/bestsearch/{searchText}")
    Call<Resultado[]> getBestSearch(@Path("locale") String locale, @Path("searchText") String searchText);
}
