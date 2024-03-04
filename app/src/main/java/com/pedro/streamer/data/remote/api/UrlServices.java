package com.pedro.streamer.data.remote.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface UrlServices {

    /*
    @FormUrlEncoded
    @POST("api/auth/verify_user")
    Observable<ResponseBody> verifyUser(@FieldMap HashMap<String, String> jsonBody);

    @FormUrlEncoded
    @POST("api/auth/resend_verify_otp")
    Observable<ResponseBody> resendVerifyOtp(@FieldMap HashMap<String, String> jsonBody);

    @FormUrlEncoded
    @POST("api/auth/social_signup")
    Observable<ResponseBody> socialSignup(@FieldMap HashMap<String, String> jsonBody);
*/

  //  @FormUrlEncoded
    @POST("api/auth/login")
    //Observable<ResponseBody> login(@FieldMap HashMap<String, String> jsonBody);
    Observable<ResponseBody> login(@Body RequestBody params);


    @POST("api/auth/sign-up")
    Observable<ResponseBody> signup(@Body RequestBody params);


  @GET("api/stream/stream-url")
  Observable<ResponseBody> getActiveLiveVideo();


    @POST("api/auth/forgot-password")
    Observable<ResponseBody> forgotPassword(@Body RequestBody params);


}
