package com.pedro.streamer.data.remote;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.pedro.streamer.data.remote.api.UrlServices;
import com.pedro.streamer.data.remote.interceptor.RequestInterceptor;
import com.pedro.streamer.utils.AppProgressDialog;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.HttpException;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {
    private static final String TAG = "Retro";
    public static  String BASEURL_API = Environment.getBaseUrl();

    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static UrlServices service(Context context) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(logging)
         //       .authenticator(new TokenAuthenticator(context))
                .cache(provideCache(context))
                .addInterceptor(new RequestInterceptor(context))
               // .addInterceptor(new NetworkInterceptor(context))
                .build();

        final UrlServices urlServices = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASEURL_API)
                .client(okHttpClient)
                .build().create(UrlServices.class);
        return urlServices;
    }


    private static Cache provideCache(Context application) {
        long cacheSize = 10 * 1024 * 1024; // 10 MB
        File httpCacheDirectory = new File(application.getCacheDir(), "http-cache");
        return new Cache(httpCacheDirectory, cacheSize);
    }

    public static void networkCalls(final Dialog dialog,final Observable<ResponseBody> method, MyResource myResource) {
        if (dialog != null)
            AppProgressDialog.show(dialog);
          addToDisposable(method.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        if (dialog != null)
                            AppProgressDialog.hide(dialog);
                        String res = null;
                        try {
                            res = responseBody.string().trim();
                           // Log.e(TAG, "onSuccess: " + res);
                            myResource.isSuccess(res);
                            // myResource.isLoading(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (dialog != null)
                            AppProgressDialog.hide(dialog);
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            int errorcode = response.code();
                            ResponseBody responseBody = ((HttpException) e).response().errorBody();
                            String errormesg = getErrorMessage(responseBody);
                           // Log.e(TAG, "onError: " + errormesg + "   errorcode   " + errorcode);
                            myResource.isError(errormesg, errorcode);
                            //   myResource.isLoading(false);
                        }

                        Log.e(TAG, "onError: "+e.getMessage() );
                    }

                    @Override
                    public void onComplete() {
                        if (dialog != null)
                            AppProgressDialog.hide(dialog);
                    }
                }));
    }

    private static String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected static void addToDisposable(Disposable disposable) {
        compositeDisposable.remove(disposable);
        compositeDisposable.add(disposable);
    }

    public static void onStop() {
        compositeDisposable.clear();
    }



}
