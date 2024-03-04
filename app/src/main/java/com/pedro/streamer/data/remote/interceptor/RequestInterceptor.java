package com.pedro.streamer.data.remote.interceptor;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.pedro.streamer.data.local.SessionManager;
import com.pedro.streamer.utils.Utility;
import com.pedro.streamer.utils.constants.AppConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {
    private Context mContext;
    public RequestInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!ConnectivityStatus.isConnected(mContext)) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> Toast.makeText(mContext, "Whoops! Slow or no internet connection.", Toast.LENGTH_LONG).show(), 500);
        }
        Request originalRequest = chain.request();
        Request request = originalRequest.newBuilder()
               /* .header(AppConstants.API_KEY, AppConstants.API_KEY_VALUE)*/
               // .header(AppConstants.DEVICE_ID, "test123" /*Utility.getDeviceId(mContext)*/)
                .header("Content-Type", "application/json")
              //  .header(AppConstants.AUTHORIZATION, SessionManager.getAuthenticationToken(mContext))
                .build();
        Log.e("url = ",""+request);
        return chain.proceed(request);
    }
}
