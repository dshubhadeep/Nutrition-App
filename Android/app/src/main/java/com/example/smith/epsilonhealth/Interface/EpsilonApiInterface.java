package com.example.smith.epsilonhealth.Interface;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by SMITH on 28-Jan-18.
 */

public interface EpsilonApiInterface {
/*
    @POST("api/login")
    @FormUrlEncoded
    Call<Login> attemptLogin(@Field("email") String email, @Field("password") String password);

    @GET("api/profile")
    Call<Profile> getProfile(@Header("auth-token") String auth);*/

    @POST("/sendImage")
    @Multipart
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file,
                                    @Part("Image") RequestBody name);

    @GET("/app/search/{id}")
    Call<ResponseBody> foodSearch(@Path("id") String foodName);

    @POST("/app/nutrient")
    @FormUrlEncoded
    Call<ResponseBody> getFoodDetails(@Field("uri") String uri );

    @POST("/login")
    @FormUrlEncoded
    Call<ResponseBody> attemptLogin(@Field("userName") String usrnm,@Field("password") String pass);

    @POST("/register")
    @FormUrlEncoded
    Call<ResponseBody> attemptregister(@Field("usrName") String usrnm,
                                       @Field("password") String pass,
                                       @Field("age") String age,
                                       @Field("gender") String gender,
                                       @Field("height") String height,
                                       @Field("weight") String weight,
                                       @Field("location") String local);
    @POST("/app/addMeal")
    @FormUrlEncoded
    Call<ResponseBody> addMeal(@Field("intake") JSONObject json);

}
