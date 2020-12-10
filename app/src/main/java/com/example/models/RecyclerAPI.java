package com.example.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecyclerAPI {
    @GET("pokemon")
    Call<Pokemon> getResults(@Query("limit") int limit);

    @GET("pokemon/{id}")
    Call<IDImgTypes> getId(@Path("id") int id);

    @GET("pokemon-species/{id}")
    Call<FlavorTextEntries> getFlav(@Path("id") int id);
}
