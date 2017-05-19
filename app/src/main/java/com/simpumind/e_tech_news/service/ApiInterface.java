package com.simpumind.e_tech_news.service;

import com.simpumind.e_tech_news.models.Code;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by simpumind on 5/1/17.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("users")
    Call<Code> authenticate(@Field("phone") String phone,
                        @Field("country") String country);
}
