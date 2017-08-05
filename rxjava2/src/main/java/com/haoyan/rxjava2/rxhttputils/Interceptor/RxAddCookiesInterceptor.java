package com.haoyan.rxjava2.rxhttputils.Interceptor;

import android.util.Log;

import com.haoyan.rxjava2.rxhttputils.utilss.RxSPUtils;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by haoyan on 2017/7/28.
 * 请求头里边添加cookie
 */

public class RxAddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet<String>) RxSPUtils.get(RxSPKeys.COOKIE, new HashSet<String>());
        if (preferences != null) {
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("RxHttpUtils", "Adding Header Cookie--->: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }
        }

        return chain.proceed(builder.build());
    }
}
