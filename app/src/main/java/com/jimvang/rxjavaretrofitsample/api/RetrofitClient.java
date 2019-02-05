package com.jimvang.rxjavaretrofitsample.api;

import android.os.Build;

import com.jimvang.rxjavaretrofitsample.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.Version;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.Locale.US;

/**
 * This class is a singleton used for creating, managing, and interfacing with our API calls.
 * <p>
 * All Networking objects are only built on the first run or after a change has been made to one
 * of the objects. All Interceptors are efficiently accounted for as well. This helps with the
 * app's speed and memory.
 * <p>
 * Created by James Van Gaasbeck on 2/4/19.
 */
public class RetrofitClient
{
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private static volatile RetrofitClient sInstance;
    // Lock for synchronizing the singleton creation and making it thread safe
    private static final Object mutex = new Object();

    private final OkHttpClient.Builder okHttpBuilder;
    private final Retrofit.Builder retrofitBuild;
    private Retrofit retrofitObject;

    private final HttpLoggingInterceptor loggingInterceptor;

    public static final String DEFAULT_USER_AGENT =
            String.format(
                    "Android Version: %s (%s - %s) ; ",
                    Build.VERSION.RELEASE, Build.VERSION.SDK_INT, Build.VERSION.CODENAME)
                    +
                    "Device Manufacturer: " + Build.MANUFACTURER.toUpperCase(US) + " ; "
                    +
                    "Device Model: " + Build.MODEL.toUpperCase(US) + " ; "
                    +
                    Version.userAgent()
                    +
                    ".";

    private RetrofitClient()
    {
        loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG)
        {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        else
        {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        okHttpBuilder = new OkHttpClient.Builder()
                .addInterceptor(new PointlessInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);


        retrofitBuild = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    public static RetrofitClient getInstance()
    {
        if (sInstance == null)
        {
            synchronized (mutex)
            {
                if (sInstance == null)
                {
                    sInstance = new RetrofitClient();
                }
            }
        }
        return sInstance;
    }

    /**
     * Convenience method used to create the api class. This method will handle Interceptor logic
     * to make sure that they are being added and without multiple instances.
     *
     * @param apiClass Interface with API calls.
     * @return The api class with it's networking methods ready to use.
     */
    public <S> S createService(Class<S> apiClass)
    {
        return createService(apiClass, -1);
    }

    /**
     * Method for creating an API. This method takes care of adding request Interceptors, making sure
     * duplicate instances are not included. All while only building the okhttp and retrofit objects
     * after a change has been made. This increases speed and saves valuable memory.
     *
     * @param apiClass Interface with API calls.
     * @param index    Supposed to be the index of the list item that would then be used to decide
     *                 whether we'd check if the PointlessInterceptor
     * @return The api class with it's networking methods ready to use.
     */
    public <S> S createService(Class<S> apiClass, int index)
    {
        boolean shouldBuild = false, shouldAddPointless = true;

        GetHeaderInterceptor getInterceptor = new GetHeaderInterceptor();
        if (!okHttpBuilder.interceptors().contains(getInterceptor))
        {
            okHttpBuilder.addInterceptor(getInterceptor);
            shouldBuild = true;
        }
        {

        }
        // If it's an even number lets check the status of the PointlessInterceptor.
        // We will remove it if it's present, and add it if it is absent.
        if (index != -1 && index % 2 == 0)
        {
            List<Interceptor> interceptors = new ArrayList<>(okHttpBuilder.interceptors());
            for (int i = 0, interceptorsSize = interceptors.size(); i < interceptorsSize; i++)
            {
                Interceptor interceptor = interceptors.get(i);
                if (interceptor instanceof PointlessInterceptor)
                {
                    // Remove the interceptor from the http client instance since we found one.
                    okHttpBuilder.interceptors().remove(i);
                    shouldAddPointless = false;
                    shouldBuild = true;
                }
            }

            if (shouldAddPointless)
            {
                // Add an interceptor to the http client instance since we didn't find one.
                okHttpBuilder.addInterceptor(new PointlessInterceptor());
                shouldBuild = true;
            }
        }

        if (!okHttpBuilder.interceptors().contains(loggingInterceptor))
        {
            // Add loggingInterceptor to the http client instance since we didn't find one.
            okHttpBuilder.addInterceptor(loggingInterceptor);
            shouldBuild = true;
        }

        if (shouldBuild || retrofitObject == null)
        {
            /*
            Only build our client objects when needed! Doing so is 
            much better for overall performance.
             */
            retrofitBuild.client(okHttpBuilder.build());
            retrofitObject = retrofitBuild.build();
        }

        return retrofitObject.create(apiClass);
    }
}
