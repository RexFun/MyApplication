package com.rex.paperdiy.view.main;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rex.paperdiy.controller.MainController;
import com.rexfun.androidlibraryhttp.HttpResultObj;

import java.util.List;
import java.util.Map;


/**
 * Created by mac373 on 15/11/25.
 */
public class MainActivityNavDrawerPullRefreshTask extends AsyncTask<String, Integer, HttpResultObj<String>> {

    private Context ctx;
    private MainController controller;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;

    public MainActivityNavDrawerPullRefreshTask(Context ctx, SwipeRefreshLayout mSwipeLayout, RecyclerView mRecyclerView) {
        this.ctx = ctx;
        this.controller = new MainController(this.ctx);
        this.mSwipeLayout = mSwipeLayout;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    protected void onPreExecute() {
        mSwipeLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, ctx.getResources().getDisplayMetrics()));
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    protected HttpResultObj<String> doInBackground(String... params) {
        HttpResultObj<String> result = controller.getDataJson();
        return result;
    }

    @Override
    protected void onPostExecute(HttpResultObj<String> result) {
        Gson gson = new Gson();
        if(!result.isSuc()) {
            Toast.makeText(ctx, result.getErrMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        mSwipeLayout.setRefreshing(false);
        List<Map<String,String>> list = (List<Map<String,String>>)gson.fromJson(result.getData(), new TypeToken<List<Map<String,String>>>(){}.getType());
        ((MainActivityNavDrawerRecyclerViewAdapter) mRecyclerView.getAdapter()).refreshItems(list);
    }
}
