// (c)2016 Flipboard Inc, All Rights Reserved.

package com.haoyan.myrxjava2.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.haoyan.myrxjava2.entity.MapEntity;
import com.haoyan.myrxjava2.network.Network;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 这种缓存策略宣告失败，内存BehaviorSubject缓存无效，文件缓存失败
 * 改用okhttp进行缓存（尝试）
 */
public class Data {
    private static final String TAG = "Data";
    private static Data instance;
    BehaviorSubject<List<MapEntity.ResultsBean>> cache;
    List<MapEntity.ResultsBean> a;
    private Disposable disposable;
    private Data() {
    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public void loadFromNetwork() {
        Network.getGankApi().getBeauties(10,2)
                .subscribeOn(Schedulers.io())
                .map(new Function<MapEntity, List<MapEntity.ResultsBean>>() {
                    @Override
                    public List<MapEntity.ResultsBean> apply(@NonNull MapEntity mapEntity) throws Exception {
                        List<MapEntity.ResultsBean> list=mapEntity.getResults();
                        return list;
                    }
                })
                .doOnNext(new Consumer<List<MapEntity.ResultsBean>>() {
                    @Override
                    public void accept(@NonNull List<MapEntity.ResultsBean> resultsBeen) throws Exception {
//                        Database.getInstance().writeItems(resultsBeen);
                        Log.i(TAG, "accept: 请求后写入数据");
                    }
                })
                .subscribe(new Consumer<List<MapEntity.ResultsBean>>() {
                    @Override
                    public void accept(@NonNull List<MapEntity.ResultsBean> resultsBeen) throws Exception {
                        cache.onNext(resultsBeen);
                        Log.i(TAG, "accept: 发送事件");
                    }

                }, new  Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void subscribeData(@NonNull Observer<List<MapEntity.ResultsBean>> observer,List<MapEntity.ResultsBean> data) {
        if (cache == null) {
            Log.i(TAG, "subscribeData: 内存为空");
            cache = BehaviorSubject.createDefault(data);
            Observable.create(new ObservableOnSubscribe<List<MapEntity.ResultsBean>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<List<MapEntity.ResultsBean>> e) throws Exception {
                    loadFromNetwork();
//                    Log.i(TAG, "subscribe: 判断硬盘缓存是否为空");
//                    List<MapEntity.ResultsBean> items = Database.getInstance().readItems();
//                    Log.i(TAG, "subscribe: 判断硬盘缓存是否为空2");
//                    if (items == null) {
//                        loadFromNetwork();
//                        Log.i(TAG, "subscribe: 执行申请数据");
//                    } else {
//                        e.onNext(items);
//                        Log.i(TAG, "subscribe: 发送硬盘数据");
//                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe(cache);
        }
        cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
