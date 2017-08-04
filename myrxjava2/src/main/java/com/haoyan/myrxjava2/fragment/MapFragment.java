package com.haoyan.myrxjava2.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.haoyan.myrxjava2.R;
import com.haoyan.myrxjava2.adapter.MapListAdapter;
import com.haoyan.myrxjava2.base.BaseFragment;
import com.haoyan.myrxjava2.data.Data;
import com.haoyan.myrxjava2.entity.MapEntity;
import com.haoyan.myrxjava2.utils.MyViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * 使用Glide显示图片，开启多个线程
 */
public class MapFragment extends BaseFragment {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.gridRv)
    RecyclerView gridRv;

    private List<MapEntity.ResultsBean> mlist;
    MapListAdapter madapter;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        initView();
        search();
        return view;
    }



    private void initView() {
        mlist=new ArrayList<>();
        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        madapter = new MapListAdapter(getActivity(),mlist,R.layout.grid_item) {
            @Override
            public void convert(MyViewHolder holder, MapEntity.ResultsBean datadto) {
                Glide.with(holder.itemView.getContext()).load(datadto.getUrl())
                        .into(holder.setImageview(R.id.imageIv));
                holder.setText(R.id.descriptionTv,datadto.get_id());
            }
        };

        gridRv.setAdapter(madapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }

    private void search() {
        Observer<List<MapEntity.ResultsBean>> observer = new Observer<List<MapEntity.ResultsBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<MapEntity.ResultsBean> resultsBeen) {
                swipeRefreshLayout.setRefreshing(false);
                mlist.addAll(resultsBeen);
                madapter.notifyDataSetChanged();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
        swipeRefreshLayout.setRefreshing(true);
        //多处代码这样写，猜测是及时关闭io线程并切换回主线程
        Data.getInstance().subscribeData(observer,mlist);
//        Network.getGankApi().getBeauties(10,2)
//                .map(new Function<MapEntity,List<MapEntity.ResultsBean>>() {
//                    @Override
//                    public List<MapEntity.ResultsBean> apply(@NonNull MapEntity mapEntity) throws Exception {
//                        return mapEntity.getResults();
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
    }
}
