package com.rexfun.greendaodemo.view.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rexfun.androidlibrarytool.InjectUtil;
import com.rexfun.androidlibrarytool.InjectUtil.InjectView;
import com.rexfun.greendaodemo.R;
import com.rexfun.greendaodemo.view.add.AddActivity;
import com.rexfun.greendaodemo.view.list.asynctask.RefreshTask;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    @InjectView(id=R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(id=R.id.recycler_view) RecyclerView mRecyclerView;
    @InjectView(id=R.id.toolbar) Toolbar toolbar;
    @InjectView(id=R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.injectView(this);
        setSupportActionBar(toolbar);

        initSwipeRefreshLayout();
        refreshRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(MainActivity.this, AddActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
        refreshRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        refreshRecyclerView();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    private void refreshRecyclerView() {
        new RefreshTask(this, mSwipeRefreshLayout, mRecyclerView).execute();
    }
}
