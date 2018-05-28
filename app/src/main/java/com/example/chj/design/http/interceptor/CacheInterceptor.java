package com.example.chj.design.http.interceptor;

import com.example.chj.design.utils.Constant;
import com.example.chj.design.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ff on 2018/5/28.
 */

public class CacheInterceptor implements Interceptor {

    private Request build;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!NetworkUtils.isConnected()) {
            build = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(build);

        if (NetworkUtils.isConnected()) {
            int maxAge = Constant.MAX_AGE;

            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = Constant.MAX_STALE;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-stale=" + maxStale)
                    .build();
        }
    }
}
