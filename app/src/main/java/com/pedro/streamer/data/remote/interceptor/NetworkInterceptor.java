package com.pedro.streamer.data.remote.interceptor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.pedro.streamer.data.local.SessionManager;
import com.pedro.streamer.utils.constants.AppConstants;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkInterceptor implements Interceptor {
    private static final String TAG = "NetworkInterceptor";
    private Context context;
    public NetworkInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!ConnectivityStatus.isConnected(context)) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> Toast.makeText(context, "Whoops! Slow or no internet connection.", Toast.LENGTH_LONG).show(), 500);
        }
        Request originalRequest = chain.request();
        Headers headers = originalRequest.headers().newBuilder().add(AppConstants.authenticate, "Bearer "+ SessionManager.getAuthenticationToken(context)).build();
        originalRequest = originalRequest.newBuilder().headers(headers).build();
        Log.e("url = ",""+originalRequest);
        return chain.proceed(originalRequest);
    }
}
