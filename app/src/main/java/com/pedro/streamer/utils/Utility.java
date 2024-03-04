package com.pedro.streamer.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;

import com.pedro.streamer.auth.LoginScreen;
import com.pedro.streamer.data.local.SessionManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void logout(Context activity) {
        SessionManager.setAuthenticationToken(activity, "");
        SessionManager.clearSession(activity);
        Intent intent = new Intent(activity, LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public static boolean isCheckEmptyOrNull(String value) {
        return value != null && !value.equals("");
    }
    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String Password) {
        //String expression = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";  //--TOdo ws like --> "(?=.*[0-9@#$%^&+=])"  "^(?=.*[a-zA-Z])(?=.*[0-9@#$%^&+=]).{8,}$"
        String expression = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=_()/?;:'-])(?=.*[0-9]).{8,}$";  //--TOdo ws like --> "(?=.*[0-9@#$%^&+=])"  "^(?=.*[a-zA-Z])(?=.*[0-9@#$%^&+=]).{8,}$"
        Pattern pattern = Pattern.compile(expression );
        Matcher matcher = pattern.matcher(Password);
        return matcher.matches();
    }

}
