package com.rex.paperdiy.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.rex.paperdiy.R;
import com.rex.paperdiy.view.asynctask.GetAsyncDrawerItemsTask;
import com.rex.paperdiy.view.asynctask.MainActivityPullRefreshTask;
import com.rexfun.androidlibrarytool.InjectUtil;
import com.rexfun.androidlibraryui.RexRecyclerView;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @InjectUtil.InjectView(id=R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectUtil.InjectView(id=R.id.recycler_view) RexRecyclerView mRecyclerView;
    @InjectUtil.InjectView(id=R.id.toolbar) Toolbar mToolbar;
    @InjectUtil.InjectView(id=R.id.fab) FloatingActionButton fab;

    private RecyclerView.LayoutManager mLayoutManager;
    private AccountHeader mDrawHeader;
    private Drawer mDrawer;
    private int curNavId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.injectView(this);
        initToolbar();
        initSwipeRefreshLayout();
        initRecyclerView();
        initNavDrawer();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        pullDownRefresh(curNavId,0, 5);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void initNavDrawer() {
        //drawer header
        mDrawHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.side_nav_bar)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Mike Penz")
                                .withEmail("mikepenz@gmail.com")
                                .withIcon(getResources().getDrawable(android.R.drawable.sym_def_app_icon))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //drawer item
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(mDrawHeader)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        curNavId = drawerItem.getIdentifier();
                        Toast.makeText(getApplicationContext(), drawerItem.getIdentifier() + "", Toast.LENGTH_SHORT).show();
                        pullDownRefresh(drawerItem.getIdentifier(), 0, 5);
                        return true;
                    }
                })
                .build();
        getAsyncDrawerItems();
        mDrawer.openDrawer();

//        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("home");
//        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("item_setting");
//        mDrawer = new DrawerBuilder()
//                    .withActivity(this)
//                    .withToolbar(mToolbar)
//                    .addDrawerItems(
//                            item1,
//                            new DividerDrawerItem(),
//                            item2,
//                            new SecondaryDrawerItem().withName("item_setting")
//                    )
//                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                        @Override
//                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                            // do something with the clicked item :D
//                            return false;
//                        }
//                    })
//                    .build();
//        mDrawer.openDrawer();
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
        mRecyclerView.setOnPullUpRefreshListener(new RexRecyclerView.OnPullUpRefreshListener() {
            @Override
            public void doRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                pullUpRefresh(curNavId, mRecyclerView.getAdapter().getItemCount(), 5);
            }
        });
    }

    private void getAsyncDrawerItems() {
        new GetAsyncDrawerItemsTask(this, mDrawer).execute();
    }

    private void pullDownRefresh(int navId, int start, int limit) {
        new MainActivityPullRefreshTask(this, mSwipeRefreshLayout, mRecyclerView).execute(navId + "", start + "", limit + "");
    }

    private void pullUpRefresh(int navId, int start, int limit) {
        new MainActivityPullRefreshTask(this, mSwipeRefreshLayout, mRecyclerView, "up").execute(navId + "", start + "", limit + "");
    }
}
