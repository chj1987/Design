package com.example.chj.design.http.interceptor;

import com.example.chj.design.utils.LogUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by ff on 2018/5/25.
 * OkHttp日志拦截
 */
public class LoggingInterceptor implements Interceptor {

    private static final String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String url = request.url().toString();

        String method = request.method();

        long time = System.nanoTime();

        LogUtils.d(TAG, String.format(Locale.getDefault(), "Send Method : %s, Request Url : %s", method, url));

        RequestBody requestBody = request.body();
        if (requestBody != null) {
            StringBuilder sb = new StringBuilder("Request Body [");
            Buffer buffer = new Buffer();

            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");

            MediaType contentType = requestBody.contentType();

            if (contentType != null) {
                charset = contentType.charset();
                if (charset != null) {
                    charset = Charset.forName("UTF-8");
                }
            }
            if (isPlainText(buffer)) {
                sb.append(buffer.readString(charset));
                if (contentType != null) {
                    sb.append("(Content-Type = ").append(contentType.toString()).append(", ")
                            .append(requestBody.contentLength()).append("-byte body");
                }
            } else {
                if (contentType != null) {
                    sb.append("(Content-Type = ").append(contentType.toString())
                            .append(", binary").append(requestBody.contentLength()).append("-byte body omitted");
                }
            }

            sb.append("]");

            LogUtils.d(TAG, String.format(Locale.getDefault(), "%s %s", method, sb.toString()));
        }
        Response response = chain.proceed(request);
        long time1 = System.nanoTime();
        //打印响应时间
        LogUtils.d(TAG, String.format(Locale.getDefault(),
                "Received response for [url = %s] in %.1fms", url, (time1 - time) / 1e6d));

        //响应状态，是否成功
        LogUtils.d(TAG, String.format(Locale.CHINA, "Received response is %s, message [%s], code[%d]",
                response.isSuccessful() ? "success" : "fail", response.message(), response.code()));

        //响应头
        LogUtils.d(TAG, String.format(Locale.getDefault(), "Received response header is %s", response.headers().toString()));

        //从网络上获取数据时打印相关信息
        LogUtils.d(TAG, "Received network response message is : " + response.networkResponse());

        //从缓存上获取数据时打印相关信息
        LogUtils.d(TAG, "Received cache response message is : " + response.cacheResponse());

        //响应数据
        ResponseBody body = response.body();
        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = Charset.defaultCharset();
        MediaType contentType = body.contentType();

        if (contentType != null) {
            charset = contentType.charset(charset);
        }

        String stringBody = buffer.clone().readString(charset);
        LogUtils.d(TAG, String.format(Locale.getDefault(), "Received response json string [%s]", stringBody));

        return response;
    }

    static boolean isPlainText(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            e.printStackTrace();
            return false;
        }
    }
}
