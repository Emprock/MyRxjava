package com.haoyan.rxjava2.network;


import com.haoyan.rxjava2.entity.EMovieBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by haoyan on 2017/6/17.
 */

public interface Edoubanapi {
    /**
     * 获取豆瓣电影top250
     *  @param start 从多少开始，如从"0"开始
     * @param count 一次请求的数目，如"10"条，最多100
     */
    @GET("v2/movie/top250")
    Observable<EMovieBean> fetchMovieTop250(@Query("start") int start, @Query("count") int count);
}
