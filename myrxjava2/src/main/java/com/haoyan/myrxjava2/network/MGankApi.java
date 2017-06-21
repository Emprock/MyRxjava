// (c)2016 Flipboard Inc, All Rights Reserved.

package com.haoyan.myrxjava2.network;


import com.haoyan.myrxjava2.entity.MapEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MGankApi {
    /**
     *
     * @param number 请求数据数量
     * @param page 请求页数
     * @return
     */
    @GET("data/福利/{number}/{page}")
    Observable<MapEntity> getBeauties(@Path("number") int number, @Path("page") int page);
}
