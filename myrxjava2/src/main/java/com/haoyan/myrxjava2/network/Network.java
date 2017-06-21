package com.haoyan.myrxjava2.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haoyan on 2017/6/16.
 */

public class Network {
    private static Edoubanapi mdoubanapi;
    private static MGankApi gankApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    //首页用数据
    public static Edoubanapi getdoubanapi() {
        if (mdoubanapi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.douban.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            mdoubanapi = retrofit.create(Edoubanapi.class);
        }
        return mdoubanapi;
    }
//    public void a(){
//        https(doubanapi.class,mdoubanapi,"aa");
//    }
//    public  <T> T https(Class<T> lei, T t,String url){
//        if(t==null){
//            Retrofit retrofit = new Retrofit.Builder()
//                    .client(okHttpClient)
//                    .baseUrl(url)
//                    .addConverterFactory(gsonConverterFactory)
//                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
//                    .build();
//            t = retrofit.create(lei);
//        }
//        return t;
//    }
    //map用数据
    public static MGankApi getGankApi() {
        if (gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(MGankApi.class);
        }
        return gankApi;
    }


}
