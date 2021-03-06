package com.haoyan.rxjava2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.haoyan.rxjava2.entity.RxMovieBean;
import com.haoyan.rxjava2.network.Rxdoubanapi;
import com.haoyan.rxjava2.network.RxUrl;
import com.haoyan.rxjava2.recyclerutil.CommonAdapter;
import com.haoyan.rxjava2.recyclerutil.OnItemClickListener;
import com.haoyan.rxjava2.recyclerutil.ViewHolder;
import com.haoyan.rxjava2.refresh.SwipyRefreshLayout;
import com.haoyan.rxjava2.rxhttputils.http.RxHttpUtils;
import com.haoyan.rxjava2.rxhttputils.rxjava.RxCommonObserver;
import com.haoyan.rxjava2.rxhttputils.rxjava.RxTransformer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class RxjavaActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    // private TextView mTextView;
    private CommonAdapter<RxMovieBean.SubjectsBean> commonadapter;
    private SwipyRefreshLayout swipeRefreshLayout;

    private Dialog loading_dialog;
    private List<RxMovieBean.SubjectsBean> storiesList;

    private final int TOP_REFRESH = 1;
    private final int BOTTOM_REFRESH = 2;
    int start=0;   //开始请求位置
    int pages=10;   //需要请求的数据数量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        initView();
        dataView(start,pages);
        setListener();
    }



    private void initView() {
        storiesList=new ArrayList<>();
        swipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView)findViewById(R.id.id_stickynavlayout_innerscrollview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        loading_dialog = new AlertDialog.Builder(this).setMessage("loading...").create();
        commonadapter = new CommonAdapter<RxMovieBean.SubjectsBean>(this,R.layout.rxjava_grid_item,storiesList) {
            @Override
            public void convert(ViewHolder holder, RxMovieBean.SubjectsBean subjectsBean) {
                ImageView imgview=holder.getView(R.id.imageIv);
                Glide.with(holder.itemView.getContext())
                        .load(subjectsBean.getImages().getLarge()).into(imgview);
                holder.setText(R.id.descriptionTv,subjectsBean.getTitle());
            }
        };
        mRecyclerView.setAdapter(commonadapter);
        commonadapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }
    private void dataView(int start,int pag) {
        RxHttpUtils.getSInstance()
                .baseUrl(RxUrl.BASE_URL)
                .cache(true)
                .readTimeout(500)
                .createSApi(Rxdoubanapi.class)
                .fetchMovieTop250(start, pag)
                .compose(RxTransformer.<RxMovieBean>switchSchedulers(loading_dialog))
                .subscribe(new RxCommonObserver<RxMovieBean>(loading_dialog) {
                    @Override
                    protected void getDisposable(Disposable d) {
                    }

                    @Override
                    protected void onError(String errorMsg) {
                    }

                    @Override
                    protected void onSuccess(RxMovieBean eMovieBean) {
                        storiesList.addAll(eMovieBean.getSubjects());
                        commonadapter.notifyDataSetChanged();
                        showToast("请求成功");
                    }
                });
    }
    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(int index) {
                Log.i(TAG, "onRefresh: "+index);
                dataOption(TOP_REFRESH);
            }

            @Override
            public void onLoad(int index) {
                Log.i(TAG, "onLoad: "+index);
                dataOption(BOTTOM_REFRESH);
            }
        });
    }
    private void dataOption(int option){
        switch (option) {
            case TOP_REFRESH:
                pages=10;
                dataView(0,pages);
                storiesList.clear();
                break;
            case BOTTOM_REFRESH:
                if(start>30){
                    Toast.makeText(RxjavaActivity.this, "到头了亲", Toast.LENGTH_SHORT).show();
                }else{
                    start=start+10;
                    dataView(start,pages);
                }
                break;
        }
        swipeRefreshLayout.setRefreshing(false);
        commonadapter.notifyDataSetChanged();
    }
}
