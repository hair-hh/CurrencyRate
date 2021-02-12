package com.impulsed.currencyrate;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String BASE_NBU = "https://bank.gov.ua";
    private static final String BASE_PB = "https://api.privatbank.ua";
    private static NBUApi nbuApi;
    private static PBApi pbApi;

    public static NBUApi getNbuApi() {
        return nbuApi;
    }

    public static PBApi getPbApi() {
        return pbApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Retrofit nbuRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_NBU)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        nbuApi = nbuRetrofit.create(NBUApi.class);

        Retrofit pbRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_PB)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        pbApi = pbRetrofit.create(PBApi.class);
    }


}
