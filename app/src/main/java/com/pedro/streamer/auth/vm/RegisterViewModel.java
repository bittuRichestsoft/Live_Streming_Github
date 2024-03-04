package com.pedro.streamer.auth.vm;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pedro.streamer.auth.LoginScreen;
import com.pedro.streamer.customexample.RtmpActivity;
import com.pedro.streamer.data.remote.MyResource;
import com.pedro.streamer.data.remote.Retro;
import com.pedro.streamer.utils.Utility;
import com.pedro.streamer.utils.constants.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.RequestBody;


public class RegisterViewModel extends ViewModel {

    public MutableLiveData<String> preferredName = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> confirmPassword = new MutableLiveData<>();

    public void signup(Context context) {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.NAME, preferredName.getValue());
        hashMap.put(AppConstants.EMAIL, email.getValue());
        hashMap.put( "phoneNumber", phone.getValue());
        // hashMap.put(AppConstants.PASSWORD, password.getValue());
     /*   hashMap.put( "userName", "userName");
        */
        hashMap.put( "platform", "android");
        hashMap.put( "deviceToken", "deviceToken");


        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(hashMap)).toString());

        Retro.networkCalls(new Dialog(context), Retro.service(context).signup(body), new MyResource() {
            @Override
            public void isSuccess(String res) {
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt(AppConstants.STATUS) == 200) {
                        Log.e(TAG, "isSuccess: " + jsonObject1);
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
                   try {
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               Intent intent = new Intent(context, LoginScreen.class);
                               context.startActivity(intent);
                           }
                       }, 4000);
                   }
                   catch (Exception exp){

                   }


                    } else if (jsonObject1.getInt(AppConstants.STATUS) == 400) {
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
                    } else    {
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
                      //  Utility.logout(context);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void isError(String err, int code) {
                Utility.toast(context, err);
            }
        });
    }

   /* public void verifyUser(Context context, String from) {
        String emailotp = otpe1.getValue() + otpe2.getValue() + otpe3.getValue() + otpe4.getValue();
        String mobileotp = otpm1.getValue() + otpm2.getValue() + otpm3.getValue() + otpm4.getValue();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, "test123"*//*Utility.getDeviceId(context)*//*);
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.USER_ID, userid.getValue());
       *//* hashMap.put(AppConstants.EMAIL_OTP, emailotp);
        hashMap.put(AppConstants.PHONE_OTP, mobileotp);*//*
        Log.e(TAG, "verifyUser: " + hashMap);
        Retro.networkCalls(new Dialog(context), Retro.service(context).verifyUser(hashMap), new MyResource() {
            @Override
            public void isSuccess(String res) {

                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt(AppConstants.STATUS) == 1) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        successToast.setValue(res);
                        //Utility.toast(context, jsonObject1.getString("message"));
                        // Intent intent = new Intent(context, CompleteYourProfileActivity.class);
                        // intent.putExtra(AppConstants.ID,data.getString("id"));
                        // intent.putExtra(AppConstants.EMAIL_ID,data.getString("email_id"));
                        // intent.putExtra(AppConstants.FROM,from);
                        // context.startActivity(intent);
                    } else if (jsonObject1.getInt(AppConstants.STATUS) == 0) {
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
                    } else if (jsonObject1.getInt(AppConstants.STATUS) == 4) {
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
                        Utility.logout(context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void isError(String err, int code) {
                Utility.toast(context, err);
            }
        });

    }

    public void resendVerifyOtp(Context context) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.USER_ID, userid.getValue());
        Log.e(TAG, "resendVerifyOtp: " + hashMap);
        Retro.networkCalls(new Dialog(context), Retro.service(context).resendVerifyOtp(hashMap), new MyResource() {
            @Override
            public void isSuccess(String res) {

                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt(AppConstants.STATUS) == 1) {
                        Utility.toast(context, jsonObject1.getString("message"));
                    } else if (jsonObject1.getInt(AppConstants.STATUS) == 0) {
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
                    } else if (jsonObject1.getInt(AppConstants.STATUS) == 4) {
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
                        Utility.logout(context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void isError(String err, int code) {
                Utility.toast(context, err);
            }
        });

    }

*/
}
