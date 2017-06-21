package com.haoyan.myrxjava2.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haoyan.myrxjava2.entity.EMovieBean;
import com.haoyan.myrxjava2.utils.MyViewHolder;

import java.util.List;

/**
 * Created by haoyan on 2017/6/15.
 */

public abstract class EDoubanAdapter extends RecyclerView.Adapter<MyViewHolder>  {
    private LayoutInflater mInflater;
    public List<EMovieBean.SubjectsBean> mDatas;
    private int mlayoutId;

    public EDoubanAdapter(Context context, List<EMovieBean.SubjectsBean> datas, int layoutId) {
        this.mDatas = datas;
        this.mlayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(mInflater.inflate(mlayoutId, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        convert(holder,mDatas.get(position));
        setUpItemEvent(holder);
    }

    public abstract  void convert(MyViewHolder holder,EMovieBean.SubjectsBean datadto);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setUpItemEvent(final MyViewHolder holder) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这个获取位置的方法，防止添加删除导致位置不变
                    int layoutPosition = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(holder.itemView, layoutPosition);

                }
            });
            //longclick
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition = holder.getAdapterPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, layoutPosition);
                    return false;
                }
            });
        }
    }
}
