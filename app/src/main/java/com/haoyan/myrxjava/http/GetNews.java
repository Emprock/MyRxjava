package com.haoyan.myrxjava.http;


import com.haoyan.myrxjava.entity.entity;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetNews {
    @GET("latest")
    Flowable<entity> getNews();

    @GET
    Observable<ResponseBody> getPicFromNet(@Url String url);


}
