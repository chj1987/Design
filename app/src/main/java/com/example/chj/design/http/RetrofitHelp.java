package com.example.chj.design.http;

import com.example.chj.design.http.interceptor.LoggingInterceptor;
import com.example.chj.design.utils.Constant;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.chj.design.http.API.BASE_URL;

/**
 * Created by ff on 2018/5/30.
 */

public class RetrofitHelp {

    private final OkHttpClient mClient;
    private final Retrofit mRetrofit;

    private static RetrofitHelp mInstance = new RetrofitHelp();

    private RetrofitHelp() {
        mClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RetrofitHelp getInstance() {
        return mInstance;
    }

    public IRetrofitService getService() {
        return mRetrofit.create(IRetrofitService.class);
    }
}
