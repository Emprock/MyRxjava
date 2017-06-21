package com.haoyan.myrxjava2.https;


import android.content.Context;

import com.haoyan.myrxjava2.view.ProgressDialogHandler;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;



/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * 调用dialog显示隐藏及网络请求失败原因，为观察者
 */

public  abstract class ProgressSubscriber<T> implements ProgressCancelListener,Observer<T>{

    private Disposable mDisposable; //关闭订阅
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;
    public ProgressSubscriber(Context context) {
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        showProgressDialog();
        _onNext(t);
    }

    /**
     * 显示Dialog
     */
    public void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    /**
     * 隐藏Dialog
     */
    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }
    @Override
    public void onCancelProgress() {

        mDisposable.dispose();
        //以下代码在Subscriber尝试一下
//        if (!this.isUnsubscribed()) {
//            this.unsubscribe();
//        }
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (false) { //这里自行替换判断网络的代码
            _onError("网络不可用");
        } else if (e instanceof ApiException) {
            _onError(e.getMessage());
        } else {
            _onError("请求失败，请稍后再试...");
        }
        dismissProgressDialog();
    }
    protected abstract void _onNext(T t);
    protected abstract void _onError(String message);
}
