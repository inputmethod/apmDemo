package com.harvestasm.apm.repository.base;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import okhttp3.logging.HttpLoggingInterceptor.Logger;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

/**
 * Created by yangfeng on 2018/1/14.
 */

public class BaseRepository<T> {
    private static final String BASE_URI = "http://10.152.102.239:8080/api/";

    private final Retrofit retrofit;
    private final Interceptor requestInterceptor;

    private T source;

    public BaseRepository(Class<? extends T> clsName) {
        this(BASE_URI, clsName, false);
    }

    public BaseRepository(Class<? extends T> clsName, boolean proto) {
        this(BASE_URI, clsName, proto);
    }

    public BaseRepository(String baseUri, Class<? extends T> clsName) {
        this(baseUri, clsName, false);
    }

    public BaseRepository(String baseUri, Class<? extends T> clsName, boolean proto) {
        this(baseUri, clsName, proto, null);
    }

    public BaseRepository(String baseUri, Class<? extends T> clsName, boolean proto, Interceptor interceptor) {
        this.requestInterceptor = null == interceptor ? new RequestInterceptor(proto) : interceptor;

        retrofit = new Builder().baseUrl(baseUri)
                .addConverterFactory(proto ? ProtoConverterFactory.create() :
                        GsonConverterFactory.create())
                .client(createClient())
                .build();
        source = retrofit.create(clsName);
    }

    private OkHttpClient createClient() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new Logger() {
            @Override
            public void log(String message) {
//                Log.d("API", message);
            }
        });
        logger.setLevel(Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(requestInterceptor)
                .build();
        return client;
    }

    protected T getSource() {
        return source;
    }
}
