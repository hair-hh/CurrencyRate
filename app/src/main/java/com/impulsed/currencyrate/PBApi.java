package com.impulsed.currencyrate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PBApi {
    @GET("/p24api/exchange_rates")
    Call<PBPojo> getData(@Query("json") int json, @Query("date") String date);
}
