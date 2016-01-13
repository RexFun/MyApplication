package com.rex.paperdiy.view.imageList;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;
import com.rex.paperdiy.R;
import com.rexfun.androidlibrarytool.InjectUtil;
import com.rexfun.androidlibraryui.RexRecyclerView;

import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @InjectUtil.InjectView(id = R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @InjectUtil.InjectView(id = R.id.toolbar) Toolbar toolbar;
    @InjectUtil.InjectView(id = R.id.fab) FloatingActionButton fab;
    @InjectUtil.InjectView(id = R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectUtil.InjectView(id = R.id.recycler_view) RexRecyclerView mRecyclerView;

    private CharSequence nav_name = "";
    private Long model_id = 0l;
    private CharSequence model_name = "";

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        InjectUtil.injectView(this);
        receiveBundle();
        initToolbar();
        initSwipeRefreshLayout();
        initRecyclerView();
        initFab();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home://返回
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void receiveBundle() {
        Bundle b = getIntent().getBundleExtra("info");
        nav_name = b.getCharSequence("nav_name");
        model_id = b.getLong("model_id");
        model_name = b.getCharSequence("model_name");
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);//actionbar主按键可以被点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左上角返回键
        mToolbarLayout.setTitle(" " + nav_name + " > " + model_name);
    }

    @Override
    public void onRefresh() {
        pullDownRefresh(model_id, 0, 5);
    }

    /**
     * 初始化SwipeRefreshLayout
     */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        //设置布局
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);//这里用线性显示 类似于listview
        mRecyclerView.setAdapter(new ImageListActivityRecyclerViewAdapter(this, new ArrayList()));
        mRecyclerView.setOnPullUpRefreshListener(new RexRecyclerView.OnPullUpRefreshListener() {
            @Override
            public void doRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                pullUpRefresh(model_id, mRecyclerView.getAdapter().getItemCount(), 5);
            }
        });
        pullDownRefresh(model_id, 0, 5);
    }

    /**
     * 初始化FloatingButton
     */
    private void initFab() {
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pullDownRefresh(model_id, 0, 5);
            }
        });
    }

    private void pullDownRefresh(Long model_id, int start, int limit) {
        new ImageListActivityPullRefreshTask(this, mSwipeRefreshLayout, mRecyclerView).execute(model_id + "", start + "", limit + "");
    }

    private void pullUpRefresh(Long model_id, int start, int limit) {
        new ImageListActivityPullRefreshTask(this, mSwipeRefreshLayout, mRecyclerView, "up").execute(model_id + "", start + "", limit + "");
    }

}
