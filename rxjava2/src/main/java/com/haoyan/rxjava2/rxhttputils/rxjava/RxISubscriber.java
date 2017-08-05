package com.haoyan.rxjava2.rxhttputils.rxjava;

import io.reactivex.disposables.Disposable;

/**
 * Created by haoyan on 2017/7/28.
 */

public interface RxISubscriber<T> {
    void doOnSubscribe(Disposable d);

    void doOnError(String errorMsg);

    void doOnNext(T t);

    void doOnCompleted();
}
