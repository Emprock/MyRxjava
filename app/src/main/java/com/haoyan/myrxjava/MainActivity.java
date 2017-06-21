package com.haoyan.myrxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.haoyan.myrxjava.adapter.SimpleAdapters;
import com.haoyan.myrxjava.entity.entity;
import com.haoyan.myrxjava.http.GetNews;
import com.haoyan.myrxjava.http.RetrofitManager;
import com.haoyan.myrxjava.http.Url;
import com.haoyan.myrxjava.utils.MyViewHolder;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 使用url转bitmap的形式转换数据，依旧使用Observable
 * 网络请求为泛型封装，Rxjava2的Flowable+Subscriber
 * 接口为知乎接口
 * 基于Rxjava2+Retrofit2
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SimpleAdapters mAdapter;
    private List<entity.StoriesBean> storiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storiesList = new ArrayList<>();
        initViews();
        getDataByRetrofit();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        mAdapter = new SimpleAdapters(this, storiesList, R.layout.item_rock) {

            @Override
            public void convert(final MyViewHolder holder, final entity.StoriesBean storiesList) {
                //这里用rxjava的map将url转为bitmap
                String url = storiesList.getImages().get(0);
                RetrofitManager.getNew().getPicFromNet(url)
                        .map(new Function<ResponseBody, Bitmap>() {
                            @Override
                            public Bitmap apply(@NonNull ResponseBody responseBody) throws Exception {
                                return BitmapFactory.decodeStream(responseBody.byteStream());
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Bitmap>() {
                            @Override
                            public void accept(@NonNull Bitmap bitmap) throws Exception {
                                holder.setText(R.id.tv_title,storiesList.getTitle())
                                        .setImageBitamp(R.id.iv_img,bitmap);
                            }

                        });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        //设置recyclerview布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickListener(new SimpleAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "click:" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "longclick:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //get data by Retrofit & RxJava
    private void getDataByRetrofit() {
        Subscriber<entity> observer =new Subscriber<entity>(){

            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(@NonNull entity entity) {
                storiesList.addAll(entity.getStories());
                mAdapter.notifyDataSetChanged();//必须刷适配器
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
               RetrofitManager.https(GetNews.class,RetrofitManager.api, Url.BASE_URL)
                       .getNews()
                       .compose(RetrofitManager.<entity>applySchedulers())
                       .subscribe(observer);

    }

}
