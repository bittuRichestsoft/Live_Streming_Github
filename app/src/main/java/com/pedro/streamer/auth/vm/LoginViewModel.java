package com.pedro.streamer.auth.vm;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pedro.streamer.MainActivity;
import com.pedro.streamer.data.local.SessionManager;
import com.pedro.streamer.data.remote.MyResource;
import com.pedro.streamer.data.remote.Retro;
import com.pedro.streamer.utils.Utility;
import com.pedro.streamer.utils.constants.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;


public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();


    public MutableLiveData<String> successToast = new MutableLiveData<>();

    public void login(Context context) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("platform", "test123"/*Utility.getDeviceId(context)*/);
        hashMap.put("deviceToken", "345jgdf"/*AppConstants.DEVICE_TYPE_VALUE*/);
        hashMap.put(AppConstants.EMAIL, email.getValue() );
        hashMap.put(AppConstants.PASSWORD,  password.getValue());



        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(hashMap)).toString());

        Log.e(TAG, "login: " + hashMap);
        Retro.networkCalls(new Dialog(context),Retro.service(context).login(body), new MyResource() {
            @Override
            public void isSuccess(String res) {

                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt(AppConstants.STATUS) == 200) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        Log.e(TAG, "login data: " + data);

                             SessionManager.setAuthenticationToken(context,data.get("token").toString());
                              Intent intent = new Intent(context, MainActivity.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           context.startActivity(intent);


                    }else   {
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
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


}
