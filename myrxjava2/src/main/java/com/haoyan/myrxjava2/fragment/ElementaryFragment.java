package com.haoyan.myrxjava2.fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.haoyan.myrxjava2.R;
import com.haoyan.myrxjava2.adapter.EDoubanAdapter;
import com.haoyan.myrxjava2.base.BaseFragment;
import com.haoyan.myrxjava2.entity.EMovieBean;
import com.haoyan.myrxjava2.https.ActivityLifeCycleEvent;
import com.haoyan.myrxjava2.https.Api;
import com.haoyan.myrxjava2.https.HttpUtil;
import com.haoyan.myrxjava2.https.ProgressSubscriber;
import com.haoyan.myrxjava2.https.Url;
import com.haoyan.myrxjava2.network.Edoubanapi;
import com.haoyan.myrxjava2.network.Network;
import com.haoyan.myrxjava2.utils.MyViewHolder;
import com.haoyan.myrxjava2.utils.rxjava.CommonObserver;
import com.haoyan.myrxjava2.utils.rxjava.Transformer;
import com.haoyan.myrxjava2.utils.http.RxHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ElementaryFragment extends BaseFragment {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.gridRv)
    RecyclerView gridRv;
    private Dialog loading_dialog;
    private List<EMovieBean.SubjectsBean> storiesList;
    private EDoubanAdapter doubanListAdapter;
    public ElementaryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_elementary, container, false);
        ButterKnife.bind(this, view);
        loading_dialog = new AlertDialog.Builder(getActivity()).setMessage("loading...").create();
        initView();
//        search();
//        initData();
        test();
        return view;
    }




    private void initView() {
        storiesList=new ArrayList<>();
        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        doubanListAdapter = new EDoubanAdapter(getActivity(),storiesList,R.layout.grid_item) {
            @Override
            public void convert(MyViewHolder holder, EMovieBean.SubjectsBean datadto) {
                Glide.with(holder.itemView.getContext()).load(datadto.getImages().getLarge())
                        .into(holder.setImageview(R.id.imageIv));
                holder.setText(R.id.descriptionTv,datadto.getTitle());
            }
        };
        gridRv.setAdapter(doubanListAdapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);


    }
    private void search() {
        //基础写法测试用
        Observer<EMovieBean> observer = new Observer<EMovieBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull EMovieBean hotMovieBean) {
                swipeRefreshLayout.setRefreshing(false);
                storiesList.addAll(hotMovieBean.getSubjects());
                doubanListAdapter.notifyDataSetChanged();
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

        Network.getdoubanapi().fetchMovieTop250(0,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    private void initData() {
        Observable ob = Api.getDefault().fetchMovieTop250(0,10);
        HttpUtil.getHttpUtil().toSubscribe(ob, new ProgressSubscriber<EMovieBean>(getActivity()) {

            @Override
            protected void _onNext(EMovieBean eMovieBean) {
                storiesList.addAll(eMovieBean.getSubjects());
                doubanListAdapter.notifyDataSetChanged();

            }

            @Override
            protected void _onError(String message) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
        },"cacheKey", ActivityLifeCycleEvent.DESTROY,lifecycleSubject,false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void test() {
        RxHttpUtils.getSInstance()
                .baseUrl(Url.BASE_URL)
                .cache(true)
                .readTimeout(500)
                .createSApi(Edoubanapi.class)
                .fetchMovieTop250(0,10)
                .compose(Transformer.<EMovieBean>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<EMovieBean>(loading_dialog) {
                    @Override
                    protected void getDisposable(Disposable d) {
                    }

                    @Override
                    protected void onError(String errorMsg) {
                    }

                    @Override
                    protected void onSuccess(EMovieBean eMovieBean) {
                        storiesList.addAll(eMovieBean.getSubjects());
                        doubanListAdapter.notifyDataSetChanged();
                        showToast("请求成功");
                    }
                });
    }
}
