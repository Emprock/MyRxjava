package com.haoyan.rxjava2.rxhttputils.rxjava;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haoyan on 2017/7/26.
 */

public class RxRetrofitClient {
    private static RxRetrofitClient instance;

    private Retrofit.Builder mRetrofitBuilder;
    private OkHttpClient.Builder mOkHttpBuilder;

    public RxRetrofitClient() {

        mOkHttpBuilder = new OkHttpClient.Builder();

        mRetrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpBuilder.build());
    }


    public static RxRetrofitClient getInstance() {

        if (instance == null) {
            synchronized (RxRetrofitClient.class) {
                if (instance == null) {
                    instance = new RxRetrofitClient();
                }
            }

        }
        return instance;
    }


    public Retrofit.Builder getRetrofitBuilder() {
        return mRetrofitBuilder;
    }

    public Retrofit getRetrofit() {
        return mRetrofitBuilder.client(mOkHttpBuilder.build()).build();
    }
}
