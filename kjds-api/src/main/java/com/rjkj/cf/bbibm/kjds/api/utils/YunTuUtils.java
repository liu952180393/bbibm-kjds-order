package com.rjkj.cf.bbibm.kjds.api.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class YunTuUtils {
    private static final String USER_CODE = "";
    private static final String APISECRET = "";
    private static final String BASE64 ="Basic QzAwNzQ1JitUVnUyRWZZajI4PQ==";
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
    private final static int READ_TIMEOUT = 200;
    private final static int CONNECT_TIMEOUT = 120;
    private final static int WRITE_TIMEOUT = 120;

    public static   String sendGet(String requestUrl) throws Exception {
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("Authorization", BASE64)
                .addHeader("Accept", "application/json")
                .build();
        //读取超时
        ClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        //连接超时
        ClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        //写入超时
        ClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient =  ClientBuilder.build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("错误" + response);
        }
    }
    public static String sendPost(String url,String orderInfo) throws Exception {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),orderInfo);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", BASE64)
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
        //读取超时
        ClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        //连接超时
        ClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        //写入超时
        ClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient =  ClientBuilder.build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("错误" + response);
        }
    }

}
