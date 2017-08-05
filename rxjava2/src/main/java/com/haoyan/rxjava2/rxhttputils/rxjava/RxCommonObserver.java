package com.haoyan.rxjava2.rxhttputils.rxjava;

import android.app.Dialog;

import io.reactivex.disposables.Disposable;

/**
 * Created by haoyan on 2017/7/28.
 * 通用的Observer
 */

public abstract class RxCommonObserver<T> extends RxBaseObserver<T> {
    private Dialog mProgressDialog;

    public RxCommonObserver() {
    }

    public RxCommonObserver(Dialog progressDialog) {
        mProgressDialog = progressDialog;
    }

    /**
     * 获取disposable 在onDestroy方法中取消订阅disposable.dispose()
     */
    protected abstract void getDisposable(Disposable d);

    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);


    @Override
    public void doOnSubscribe(Disposable d) {
        getDisposable(d);
    }

    @Override
    public void doOnError(String errorMsg) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        onError(errorMsg);
    }

    @Override
    public void doOnNext(T t) {
        onSuccess(t);
    }

    @Override
    public void doOnCompleted() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
