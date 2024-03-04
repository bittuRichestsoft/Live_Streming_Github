package com.pedro.streamer.data.local;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;


public class Controller extends Application {
    private static final String TAG = "Controller";
    @SuppressLint("StaticFieldLeak")
    public static  Context mCurrentActivity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = this;
       /* RealmController.InitRealm(context);
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(AppConstants.REGION).build();
        UIKitSettings uiKitSettings = new UIKitSettings(context);
        uiKitSettings.addConnectionListener(TAG);
        createNotificationChannel();*/
    }




    @Override
    public void onTerminate() {
        super.onTerminate();
     }

    public static  Context getCurrentActivity () {
        return mCurrentActivity ;
    }
    public void setCurrentActivity (Activity CurrentActivity) {
        mCurrentActivity = CurrentActivity ;
    }

}
