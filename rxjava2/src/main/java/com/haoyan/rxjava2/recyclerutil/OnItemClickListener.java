package com.haoyan.rxjava2.recyclerutil;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by haoyan on 2017/7/29.
 */

public interface OnItemClickListener<T> {
    void onItemClick(ViewGroup parent, View view, T t, int position);
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
