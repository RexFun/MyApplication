package com.rexfun.greendaodemo.view.list.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.rexfun.greendaodemo.dao.DaoFactory;
import com.rexfun.greendaodemo.view.list.MyRecyclerViewAdapter;

/**
 * Created by mac373 on 15/12/2.
 */
public class RefreshTask extends AsyncTask<String, Integer, Cursor> {
    private Context ctx;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;
    private Cursor mCursor;

    public RefreshTask(Context ctx, SwipeRefreshLayout mSwipeLayout, RecyclerView mRecyclerView) {
        this.ctx = ctx;
        this.mSwipeLayout = mSwipeLayout;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    protected void onPreExecute() {
        mSwipeLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, ctx.getResources().getDisplayMetrics()));
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    protected Cursor doInBackground(String... params) {
        mCursor = DaoFactory.getWritableDatabase(ctx).query(DaoFactory.getUserDao(ctx).getTablename(), DaoFactory.getUserDao(ctx).getAllColumns(), null, null, null, null, null);
        return mCursor;
    }

    @Override
    protected void onPostExecute(final Cursor cursor) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));//这里用线性显示 类似于listview
//                mRecyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));//这里用线性宫格显示 类似于grid view
//                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
                mRecyclerView.setAdapter(new MyRecyclerViewAdapter(ctx, cursor));
                mSwipeLayout.setRefreshing(false);
            }
        }, 500);
    }
}
