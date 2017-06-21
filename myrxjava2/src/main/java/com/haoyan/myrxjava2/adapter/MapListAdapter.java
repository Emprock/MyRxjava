package com.haoyan.myrxjava2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.haoyan.myrxjava2.entity.MapEntity;
import com.haoyan.myrxjava2.utils.MyViewHolder;

import java.util.List;

/**
 * Created by haoyan on 2017/6/17.
 */

public abstract class MapListAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<MapEntity.ResultsBean> mDatas;
    private int mlayoutId;

    public MapListAdapter(Context context, List<MapEntity.ResultsBean> datas, int layoutId) {
        this.mDatas = datas;
        this.mlayoutId = layoutId;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(layoutInflater.inflate(mlayoutId, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        convert(holder,mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public abstract  void convert(MyViewHolder holder, MapEntity.ResultsBean datadto);
}
