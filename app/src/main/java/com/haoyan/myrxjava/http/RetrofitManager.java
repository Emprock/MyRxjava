package com.haoyan.myrxjava.http;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.haoyan.myrxjava.http.Url.BASE_URL;

public class RetrofitManager {
    public static GetNews api;
    /**
     * 以api为介质封装
     * @return api
     */
    public static GetNews getNew(){
        if(api==null){
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.connectTimeout(1000,TimeUnit.SECONDS);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api= retrofit.create(GetNews.class);
        }
        return api;
    }

    /**
     *泛型封装
     * @param lei  api类
     * @param t    api类对象
     * @param url  网络请求地址
     * @param <T>  泛型
     * @return
     */
    public  static<T> T https(Class<T> lei, T t,String url){
        if(t==null){
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.connectTimeout(1000,TimeUnit.SECONDS);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            t = retrofit.create(lei);
        }
        return t;
    }

    /**
     * 官方推荐写法主要是省去两句线程的调用，由操作符compose调用
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applySchedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }






}
