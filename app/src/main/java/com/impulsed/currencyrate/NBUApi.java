package com.impulsed.currencyrate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NBUApi {
    @GET("/NBU_Exchange/exchange")
    Call<List<NBUPojo>> getData(@Query("json") int json, @Query("date") String date);
}
