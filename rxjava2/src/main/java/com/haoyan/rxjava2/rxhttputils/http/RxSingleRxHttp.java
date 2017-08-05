package com.haoyan.rxjava2.rxhttputils.http;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.haoyan.rxjava2.rxhttputils.Interceptor.RxAddCookiesInterceptor;
import com.haoyan.rxjava2.rxhttputils.Interceptor.RxCacheInterceptor;
import com.haoyan.rxjava2.rxhttputils.Interceptor.RxHeaderInterceptor;
import com.haoyan.rxjava2.rxhttputils.Interceptor.RxReceivedCookiesInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haoyan on 2017/7/28.
 * 网络请求-----可以对每个请求单独配置参数
 */

public class RxSingleRxHttp {
    private static RxSingleRxHttp instance;

    private String baseUrl;

    private Map<String, Object> headerMaps = new HashMap<>();

    private boolean isShowLog = true;
    private boolean cache = false;
    private boolean saveCookie = true;

    private String cachePath;
    private long cacheMaxSize;

    private long readTimeout;
    private long writeTimeout;
    private long connectTimeout;

//    private SSLUtils.SSLParams sslParams;

    private OkHttpClient okClient;

    public static RxSingleRxHttp getInstance() {
        if (instance == null) {
            synchronized (RxSingleRxHttp.class) {
                if (instance == null) {
                    instance = new RxSingleRxHttp();
                }
            }

        }
        return instance;
    }

    public RxSingleRxHttp baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RxSingleRxHttp addHeaders(Map<String, Object> headerMaps) {
        this.headerMaps = headerMaps;
        return this;
    }

    public RxSingleRxHttp log(boolean isShowLog) {
        this.isShowLog = isShowLog;
        return this;
    }

    public RxSingleRxHttp cache(boolean cache) {
        this.cache = cache;
        return this;
    }

    public RxSingleRxHttp saveCookie(boolean saveCookie) {
        this.saveCookie = saveCookie;
        return this;
    }

    public RxSingleRxHttp cachePath(String cachePath, long maxSize) {
        this.cachePath = cachePath;
        this.cacheMaxSize = maxSize;
        return this;
    }

    public RxSingleRxHttp readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public RxSingleRxHttp writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public RxSingleRxHttp connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

//    /**
//     * 信任所有证书,不安全有风险
//     *
//     * @return
//     */
//    public SingleRxHttp sslSocketFactory() {
//        sslParams = SSLUtils.getSslSocketFactory();
//        return this;
//    }
//
//    /**
//     * 使用预埋证书，校验服务端证书（自签名证书）
//     *
//     * @param certificates
//     * @return
//     */
//    public SingleRxHttp sslSocketFactory(InputStream... certificates) {
//        sslParams = SSLUtils.getSslSocketFactory(certificates);
//        return this;
//    }
//
//    /**
//     * 使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//     *
//     * @param bksFile
//     * @param password
//     * @param certificates
//     * @return
//     */
//    public SingleRxHttp sslSocketFactory(InputStream bksFile, String password, InputStream... certificates) {
//        sslParams = SSLUtils.getSslSocketFactory(bksFile, password, certificates);
//        return this;
//    }

    public RxSingleRxHttp client(OkHttpClient okClient) {
        this.okClient = okClient;
        return this;
    }

    /**
     * 使用自己自定义参数创建请求
     *
     * @param cls
     * @param <K>
     * @return
     */
    public <K> K createSApi(Class<K> cls) {
        return getSingleRetrofitBuilder().build().create(cls);
    }


    /**
     * 单个RetrofitBuilder
     *
     * @return
     */
    public Retrofit.Builder getSingleRetrofitBuilder() {

        Retrofit.Builder singleRetrofitBuilder = new Retrofit.Builder();
        singleRetrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient == null ? getSingleOkHttpBuilder().build() : okClient);

        if (!TextUtils.isEmpty(baseUrl)) {
            singleRetrofitBuilder.baseUrl(baseUrl);
        }
        return singleRetrofitBuilder;
    }

    /**
     * 获取单个 OkHttpClient.Builder
     *
     * @return
     */
    public OkHttpClient.Builder getSingleOkHttpBuilder() {

        OkHttpClient.Builder singleOkHttpBuilder = new OkHttpClient.Builder();

        singleOkHttpBuilder.retryOnConnectionFailure(true);

        singleOkHttpBuilder.addInterceptor(new RxHeaderInterceptor(headerMaps));

        if (cache) {
            RxCacheInterceptor cacheInterceptor = new RxCacheInterceptor();
            Cache cache;
            if (!TextUtils.isEmpty(cachePath) && cacheMaxSize > 0) {
                cache = new Cache(new File(cachePath), cacheMaxSize);
            } else {
                cache = new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "/rxHttpCacheData")
                        , 1024 * 1024 * 100);
            }
            singleOkHttpBuilder.addInterceptor(cacheInterceptor)
                    .addNetworkInterceptor(cacheInterceptor)
                    .cache(cache);
        }
        if (isShowLog) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("RxHttpUtils", message);

                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            singleOkHttpBuilder.addInterceptor(loggingInterceptor);
        }

        if (saveCookie) {
            singleOkHttpBuilder
                    .addInterceptor(new RxAddCookiesInterceptor())
                    .addInterceptor(new RxReceivedCookiesInterceptor());
        }

        singleOkHttpBuilder.readTimeout(readTimeout > 0 ? readTimeout : 10, TimeUnit.SECONDS);

        singleOkHttpBuilder.writeTimeout(writeTimeout > 0 ? writeTimeout : 10, TimeUnit.SECONDS);

        singleOkHttpBuilder.connectTimeout(connectTimeout > 0 ? connectTimeout : 10, TimeUnit.SECONDS);

//        if (sslParams != null) {
//            singleOkHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        }

        return singleOkHttpBuilder;
    }

}
