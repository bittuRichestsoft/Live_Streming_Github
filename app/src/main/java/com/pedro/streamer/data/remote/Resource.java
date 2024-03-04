package com.pedro.streamer.data.remote;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;
    @Nullable
    public final int code;
    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message,int code) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
    }



    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null,0);
    }

    public static <T> Resource<T> error(String msg,int code, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg,code);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null,0);
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS && data != null;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    public boolean isLoaded() {
        return status != Status.LOADING;
    }
}