package com.rexfun.greendaodemo.view.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.rexfun.androidlibrarytool.InjectUtil;
import com.rexfun.androidlibrarytool.InjectUtil.InjectView;
import com.rexfun.greendaodemo.R;
import com.rexfun.greendaodemo.view.add.AddActivity;
import com.rexfun.greendaodemo.view.list.asynctask.PullRefreshTask;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(id=R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(id=R.id.recycler_view) RecyclerView mRecyclerView;
    @InjectView(id=R.id.toolbar) Toolbar toolbar;
    @InjectView(id=R.id.fab) FloatingActionButton fab;

    private int lastVisibleItem;
    private RecyclerView.LayoutManager mLayoutManager;

    private float y_tmp1, y_tmp2;

    private static final int PULL_UP = 0;
    private static final int PULL_DOWN = 1;
    private static int PULL_DIRECTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.injectView(this);
        setSupportActionBar(toolbar);

        initSwipeRefreshLayout();
        initRecyclerView();
        pullDownRefresh(0, 5);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(MainActivity.this, AddActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStop() {
        System.out.println("*** onStop ***");
        super.onStop();
    }

    @Override
    protected void onPause() {
        System.out.println("*** onPause ***");
        super.onPause();
    }

    @Override
    protected void onStart() {
        System.out.println("*** onStart ***");
        super.onStart();
    }

    @Override
    protected void onResume() {
        System.out.println("*** onResume ***");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        System.out.println("*** onRestart ***");
        super.onRestart();
        //刷新列表
//        pullDownRefresh(0, 5);
    }

    /**
     * 判断是向左还是滑动方向
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        //获取当前坐标
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                y_tmp1 = y;
                break;
            case MotionEvent.ACTION_UP:
                y_tmp2 = y;
                System.out.println("滑动参值 y1=" + y_tmp1 + "; y2=" + y_tmp2);
                if(y_tmp1 != 0){
                    if(y_tmp1 - y_tmp2 > 8){
                        PULL_DIRECTION = PULL_UP;
                        System.out.println("pull_up");
                    }
                    if(y_tmp2 - y_tmp1 > 8){
                        PULL_DIRECTION = PULL_DOWN;
                        System.out.println("pull_down");
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.layout1) {
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);//这里用线性显示 类似于listview
            return true;
        } else if (id == R.id.layout2) {
            mLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);//这里用线性宫格显示 类似于grid view
            return true;
        } else if (id == R.id.layout3) {
            mLayoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);//这里用线性宫格显示 类似于瀑布流
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        pullDownRefresh(0, 5);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    private void initRecyclerView() {
        //设置布局
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);//这里用线性显示 类似于listview

        //设置上拉动作
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mRecyclerView.getAdapter().getItemCount() && PULL_DIRECTION == PULL_UP) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    pullUpRefresh(mRecyclerView.getAdapter().getItemCount(), 5);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager instanceof LinearLayoutManager) {
                    lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                } else if (mLayoutManager instanceof GridLayoutManager) {
                    lastVisibleItem = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(lastPositions);
                    lastVisibleItem = findMax(lastPositions);
                }
            }
        });
    }

    private void pullDownRefresh(int start, int limit) {
        new PullRefreshTask(this, mSwipeRefreshLayout, mRecyclerView).execute(start + "", limit + "");
    }
    private void pullUpRefresh(int start, int limit) {
        new PullRefreshTask(this, mSwipeRefreshLayout, mRecyclerView, "up").execute(start + "", limit + "");
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
