package com.haoyan.myrxjava.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haoyan.myrxjava.entity.entity;
import com.haoyan.myrxjava.utils.MyViewHolder;

import java.util.List;

/**
 * Created by 11981 on 2016/5/26.
 * recyclerview_list_grid适配器
 */
public abstract class SimpleAdapters extends RecyclerView.Adapter<MyViewHolder> {

    private LayoutInflater mInflater;
    public List<entity.StoriesBean> mDatas;
    private int mlayoutId;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public SimpleAdapters(Context context, List<entity.StoriesBean> datas, int layoutId) {
        this.mDatas = datas;
        this.mlayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        convert(holder,mDatas.get(position));
        setUpItemEvent(holder);
    }
    public abstract  void convert(MyViewHolder holder,entity.StoriesBean datadto);

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

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(mInflater.inflate(mlayoutId, parent, false));
        return viewHolder;
    }

    public void addData(int pos,entity.StoriesBean datas) {
        mDatas.add(pos, datas);
        notifyItemInserted(pos);
    }

    public void deleteData(int pos) {
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }
}
