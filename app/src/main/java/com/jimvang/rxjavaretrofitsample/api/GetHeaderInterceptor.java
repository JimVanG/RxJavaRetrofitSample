package com.jimvang.rxjavaretrofitsample.api;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by James Van Gaasbeck on 2/4/19.
 */
public class GetHeaderInterceptor implements Interceptor
{
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException
    {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("User-Agent", RetrofitClient.DEFAULT_USER_AGENT)
                .header("Accept", "application/json");

        return chain.proceed(requestBuilder.build());
    }
}
