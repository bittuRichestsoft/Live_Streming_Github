package com.pedro.streamer.data.remote;


/**
 * Created by Deepak Kumar Verma on 22-01-2021.
 * Company Shantiinfotech.
 */
public interface MyResource {
    void isSuccess(String res);
    void isError(String err,int code);
}
