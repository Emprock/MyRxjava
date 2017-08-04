// (c)2016 Flipboard Inc, All Rights Reserved.

package com.haoyan.rxjava2.utils;

import android.app.Application;
import android.content.Context;

//全局Context
public class App extends Application {
    private static Context context;

    public static Context getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
