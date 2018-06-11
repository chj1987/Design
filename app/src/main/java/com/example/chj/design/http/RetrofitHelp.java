package com.example.chj.design.http;

import com.example.chj.design.http.interceptor.LoggingInterceptor;
import com.example.chj.design.utils.Constant;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ff on 2018/5/30.
 */

public class RetrofitHelp {
    private final Retrofit mRetrofit;
    private String BASE_URL;

    private RetrofitHelp(Builder builder) {
        this.BASE_URL = builder.BASE_URL;
        this.mRetrofit = builder.mRetrofit;
    }

    public static class Builder {
        private String BASE_URL;
        private final OkHttpClient mClient;
        public final Retrofit mRetrofit;

        public Builder(String BASE_URL) {
            this.BASE_URL = BASE_URL;
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

        public RetrofitHelp build() {
            return new RetrofitHelp(this);
        }
    }

    public IRetrofitService getService() {
        return mRetrofit.create(IRetrofitService.class);
    }
}
