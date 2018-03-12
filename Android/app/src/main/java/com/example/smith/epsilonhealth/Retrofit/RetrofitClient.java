package com.example.smith.epsilonhealth.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SMITH on 28-Jan-18.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofitClient(String url){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
