package com.example.doanmobile;

import com.example.doanmobile.Model.Appdata;
import com.example.doanmobile.Model.LoginRequest;
import com.example.doanmobile.Model.LoginRespon;
import com.example.doanmobile.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @POST("/api/User")
    Call<LoginRespon> Login(@Body LoginRequest loginRequest);
    @GET("/api/Test")
        Call<LoginRespon> test();
    @FormUrlEncoded
    @POST("/api/User")
    Call<LoginRespon> getUser(@Field("email") String username,
                              @Field("password") String password);
    @GET("/api/Test")
    Call<LoginRespon> test2( );
    @POST("/api/User/register")
    Call<LoginRespon> Register(@Body User user);

    @POST("/api/Appdata/appdata")
    Call<List<Appdata>> loadData(@Body int week);
    @POST("/api/Appdata/save")
    Call<String> save(@Body List<Appdata> appdata);
    @POST("/api/Appdata/appdataName")
    Call<List<Appdata>> getdataname(@Body String appdata);
}
