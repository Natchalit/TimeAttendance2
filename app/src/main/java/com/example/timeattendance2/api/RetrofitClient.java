package com.example.timeattendance2.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://advcheckerapi.azurewebsites.net/api/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }

}
