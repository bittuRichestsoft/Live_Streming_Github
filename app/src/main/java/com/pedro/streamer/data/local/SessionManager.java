package com.pedro.streamer.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.pedro.streamer.utils.constants.AppConstants;


public class SessionManager {

    public static final String MyPREFERENCES = "AppSession";
    private static SharedPreferences sharedpreferences;
    private static SharedPreferences.Editor editor;

    // clear sessions
    public static void clearSession(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String getAuthenticationToken(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(AppConstants.AUTHENTICATION, "");
    }

    public static void setAuthenticationToken(Context context, String token) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(AppConstants.AUTHENTICATION, token);
        editor.apply();
    }
}