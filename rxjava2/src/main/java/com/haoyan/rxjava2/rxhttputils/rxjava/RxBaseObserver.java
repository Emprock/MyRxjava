package com.haoyan.rxjava2.rxhttputils.rxjava;

import android.widget.Toast;

import com.haoyan.rxjava2.RxApp;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by haoyan on 2017/7/28.
 */

public abstract class RxBaseObserver<T> implements Observer<T>, RxISubscriber<T> {
    private Toast mToast;
    public static final String errorMsg_SocketTimeoutException = "网络链接超时，请检查您的网络状态，稍后重试！";
    public static final String errorMsg_ConnectException = "网络链接异常，请检查您的网络状态";
    public static final String errorMsg_UnknownHostException = "网络异常，请检查您的网络状态";

    protected void doOnNetError() {
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        doOnNext(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof SocketTimeoutException) {
            setError(errorMsg_SocketTimeoutException);
        } else if (e instanceof ConnectException) {
            setError(errorMsg_ConnectException);
        } else if (e instanceof UnknownHostException) {
            setError(errorMsg_UnknownHostException);
        } else {

            String error = e.getMessage();
            showToast(error);
            doOnError(error);
        }
    }


    @Override
    public void onComplete() {
        doOnCompleted();
    }


    private void setError(String errorMsg) {
        showToast(errorMsg);
        doOnError(errorMsg);
        doOnNetError();
    }


    /**
     * Toast提示
     *
     * @param msg 提示内容
     */
    protected void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(RxApp.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 错误处理
     *
     * @param e
     * @return
     */
    private String handleError(Throwable e) {
        String error = null;
        try {
            ResponseBody errorBody = ((HttpException) e).response().errorBody();
            error = errorBody.string();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return error;
    }
}
