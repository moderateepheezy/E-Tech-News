package com.simpumind.e_tech_news.service;

import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by simpumind on 5/1/17.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseurl){

        if  (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }
}
