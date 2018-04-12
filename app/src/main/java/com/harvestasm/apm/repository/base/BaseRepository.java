package com.harvestasm.apm.repository.base;

import typany.apm.okhttp3.Interceptor;
import typany.apm.okhttp3.OkHttpClient;
import typany.apm.retrofit2.Retrofit;
import typany.apm.retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yangfeng on 2018/1/14.
 */

public class BaseRepository<T> {
    private static final String BASE_URI = "http://10.152.102.239:8080/api/";

    private final Retrofit retrofit;
    private final Interceptor requestInterceptor;

    private T source;

    public BaseRepository(Class<? extends T> clsName) {
        this(BASE_URI, clsName);
    }

    public BaseRepository(String baseUri, Class<? extends T> clsName) {
        this(baseUri, clsName, null);
    }

    public BaseRepository(String baseUri, Class<? extends T> clsName, Interceptor interceptor) {
        this.requestInterceptor = null == interceptor ? new RequestInterceptor() : interceptor;

        retrofit = new Retrofit.Builder().baseUrl(baseUri)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createClient())
                .build();
        source = retrofit.create(clsName);
    }

    private OkHttpClient createClient() {
//        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new Logger() {
//            @Override
//            public void log(String message) {
////                Log.d("API", message);
//            }
//        });
//        logger.setLevel(Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build();
        return client;
    }

    protected T getSource() {
        return source;
    }
}
