package com.jimvang.rxjavaretrofitsample.api;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    /**
     * We are doing a quick and dirty cheap way of allowing our {@code GetHeaderInterceptor}
     * instances to be compared easily via the {@link java.util.List#contains(java.lang.Object)} method.
     * This code would not run in a production app.
     *
     * @param obj Another GetHeaderInterceptor instance.
     * @return If the object is an instance of GetHeaderInterceptor.
     */
    @Override
    public boolean equals(@Nullable Object obj)
    {
        return obj instanceof GetHeaderInterceptor;
    }


    /**
     * We are going to just return an int that will be common amongst all instances, so we we can
     * use the {@code equals(Object)} function for comparison in
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public int hashCode()
    {
        return 42;
    }
}
