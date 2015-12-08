package com.rexfun.greendaodemo.view.list.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;

import com.rexfun.greendaodemo.controller.UserController;
import com.rexfun.greendaodemo.view.list.MyRecyclerViewAdapter;

import java.util.List;

/**
 * Created by mac373 on 15/12/2.
 */
public class PullRefreshTask extends AsyncTask<String, Integer, List> {
    private Context ctx;
    private UserController mUserController;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;
    private List mList;
    private String direction = "down";//pull方向(up/down)，默认"down"

    public PullRefreshTask(Context ctx, SwipeRefreshLayout mSwipeLayout, RecyclerView mRecyclerView) {
        this.ctx = ctx;
        this.mUserController = new UserController(this.ctx);
        this.mSwipeLayout = mSwipeLayout;
        this.mRecyclerView = mRecyclerView;
    }
    public PullRefreshTask(Context ctx, SwipeRefreshLayout mSwipeLayout, RecyclerView mRecyclerView, String direction) {
        this.ctx = ctx;
        this.mUserController = new UserController(this.ctx);
        this.mSwipeLayout = mSwipeLayout;
        this.mRecyclerView = mRecyclerView;
        this.direction = direction;
    }

    @Override
    protected void onPreExecute() {
        mSwipeLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, ctx.getResources().getDisplayMetrics()));
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    protected List doInBackground(String... params) {
        mList = mUserController.getPageList(Integer.valueOf(params[0]), Integer.valueOf(params[1]));
        return mList;
    }

    @Override
    protected void onPostExecute(final List list) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
                if ("down".equals(direction)) {
                    mRecyclerView.setAdapter(new MyRecyclerViewAdapter(ctx, list));
                } else {
                    ((MyRecyclerViewAdapter)mRecyclerView.getAdapter()).addListItem(list);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }, 500);
    }
}
