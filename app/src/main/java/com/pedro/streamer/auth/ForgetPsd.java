package com.pedro.streamer.auth;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pedro.streamer.MainActivity;
import com.pedro.streamer.R;
import com.pedro.streamer.data.remote.MyResource;
import com.pedro.streamer.data.remote.Retro;
import com.pedro.streamer.utils.Utility;
import com.pedro.streamer.utils.constants.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;

public class ForgetPsd extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail;
    Button btnSubmit;
    TextView tv_goToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psd);

        etEmail=(EditText) findViewById(R.id.etEmail);
        btnSubmit=(Button) findViewById(R.id.btnSubmit);
        tv_goToLogin=(TextView) findViewById(R.id.tv_goToLogin);

        tv_goToLogin.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_goToLogin -> {
                Intent intent = new Intent(this, LoginScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            case R.id.btnSubmit -> {
                checkAndValidateEmail(etEmail.getText().toString());

            }
        }


    }

    private void checkAndValidateEmail(String strEmail) {

        boolean isValid = true;


        if (!Utility.isCheckEmptyOrNull(strEmail)) {
            isValid = false;
            Utility.toast(this, "Please enter email address");
             } else if (!Utility.isValidEmail(strEmail)) {
            isValid = false;
            Utility.toast(this, "Please enter valid email address");
        }

        if(isValid){
            forgotPassword(strEmail,this);

        }


    }
    public void forgotPassword(String strEmail, Context context ) {
        HashMap<String, String> hashMap = new HashMap<>();
         hashMap.put("email", strEmail );

        Log.e("TAG", "forgotPassword: " + hashMap);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(hashMap)).toString());

        Retro.networkCalls(new Dialog(this),Retro.service(this).forgotPassword(body), new MyResource() {
            @Override
            public void isSuccess(String res) {

                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt(AppConstants.STATUS) == 200) {
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

                    } else  {
                        Utility.toast(context, jsonObject1.getString(AppConstants.MESSAGE));
                       // Utility.logout(context);
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