package com.haoyan.myrxjava2.base;

import android.app.Fragment;
import android.os.Bundle;

import com.haoyan.myrxjava2.https.ActivityLifeCycleEvent;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by haoyan on 2017/6/16.
 */

public abstract class BaseFragment extends Fragment{
    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
    }

    //MapFragment使用
    protected Disposable disposable;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }
    protected void unsubscribe() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
