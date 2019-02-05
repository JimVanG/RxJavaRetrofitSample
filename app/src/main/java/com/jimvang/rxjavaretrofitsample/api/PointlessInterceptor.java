package com.jimvang.rxjavaretrofitsample.api;

import android.util.Log;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * This Interceptor is just a dummy interceptor that does nothing other than write a log.
 * <p>
 * It's purpose is for practicing adding, removing, checking, and creating Interceptors.
 * <p>
 * Created by James Van Gaasbeck on 2/4/19.
 */
public class PointlessInterceptor implements Interceptor
{

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException
    {
        Log.i(PointlessInterceptor.class.getName(),
              "This interceptor is for practice purposes and does nothing.");
        return chain.proceed(chain.request());
    }
}
