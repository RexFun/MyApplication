package com.rex.paperdiy.view.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.rex.paperdiy.R;
import com.rex.paperdiy.view.asynctask.MainActivityNavDrawerPullRefreshTask;
import com.rex.paperdiy.view.asynctask.MainActivityPullRefreshTask;
import com.rexfun.androidlibrarytool.InjectUtil;
import com.rexfun.androidlibraryui.RexRecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @InjectUtil.InjectView(id = R.id.nav_drawer_layout) DrawerLayout mNavDrawerLayout;
    @InjectUtil.InjectView(id = R.id.nav_drawer_swipe_refresh_layout) SwipeRefreshLayout mNavDrawerSwipeRefreshLayout;
    @InjectUtil.InjectView(id = R.id.nav_drawer_recycler_view) RexRecyclerView mNavDrawerRecyclerView;
    @InjectUtil.InjectView(id = R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectUtil.InjectView(id = R.id.recycler_view) RexRecyclerView mRecyclerView;
    @InjectUtil.InjectView(id = R.id.toolbar) Toolbar mToolbar;
    @InjectUtil.InjectView(id = R.id.fab) FloatingActionButton fab;

    public static ImageLoader mImageLoader;
    public static DisplayImageOptions mDisplayImageOptions;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView.LayoutManager mLayoutManager;
//    private AccountHeader mDrawHeader;
//    private Drawer mDrawer;
    private int curNavId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.injectView(this);
        initImageLoader();
        initToolbar();
        initSwipeRefreshLayout();
        initRecyclerView();
        initNavDrawer();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pullDownRefresh(curNavId,0,5);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRefresh() {
        pullDownRefresh(curNavId, 0, 5);
    }

    /**
     * 初始化ImageLoader
     */
    private void initImageLoader() {
//        Configuration
//        所有的选项在配置建造器中都是可选择的。使用那些你真正想要定制的。

//        File cacheDir = StorageUtils.getCacheDirectory(this);//默认缓存路径
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");//自定义缓存路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
        mImageLoader = ImageLoader.getInstance();

//        Display Option
//        显示选项，可以被任何一个显示线程请求。
        mDisplayImageOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//                .delayBeforeLoading(1000)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(50))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);//actionbar主按键可以被点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左上角返回键
    }

    /**
     * 初始化抽屉式导航
     */
    private void initNavDrawer() {
        initNavDrawerToogle();
        initNavDrawerSwipeRefreshLayout();
        initNavDrawerRecyclerView();
    }
//    private void initNavDrawer() {
//        //drawer header
//        mDrawHeader = new AccountHeaderBuilder()
//                .withActivity(this)
//                .withHeaderBackground(R.drawable.header)
//                .addProfiles(
//                        new ProfileDrawerItem()
//                                .withName("Rex Fun")
//                                .withEmail("https://github.com/RexFun")
//                                .withIcon(R.mipmap.ic_account_header)
//                )
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
//                        startActivity(new Intent().setClass(MainActivity.this, WebActivity.class));
//                        return false;
//                    }
//                })
//                .build();
//        //drawer item
//        mDrawer = new DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(mToolbar)
//                .withAccountHeader(mDrawHeader)
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        curNavId = drawerItem.getIdentifier();
//                        pullDownRefresh(curNavId, 0, 5);
////                        以下setTitle无效 ？
////                        Toast.makeText(MainActivity.this, ((Nameable)drawerItem).getName().getText(MainActivity.this)+"", Toast.LENGTH_SHORT).show();
////                        getSupportActionBar().setTitle(((Nameable) drawerItem).getName().getText(MainActivity.this));
////                        if (drawerItem instanceof Nameable) {
////                            getSupportActionBar().setTitle(((Nameable) drawerItem).getName().getText(MainActivity.this));
////                        }
//                        return false;
//                    }
//                })
//                .build();
//        getAsyncDrawerItems();
//        mDrawer.openDrawer();
//    }

    /**
     * 初始化NavDrawerToogle
     */
    private void initNavDrawerToogle() {
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mNavDrawerLayout,         /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getActionBar().setTitle(mDrawerTitle);
            }
        };
        mNavDrawerLayout.openDrawer(findViewById(R.id.left_drawer));
        mNavDrawerLayout.setDrawerListener(mDrawerToggle);
        pullDownRefreshNavDrawer();
    }
    /**
     * 初始化NavDrawerSwipeRefreshLayout
     */
    private void initNavDrawerSwipeRefreshLayout() {
        mNavDrawerSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownRefreshNavDrawer();
            }
        });
        mNavDrawerSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }
    /**
     * 初始化NavDrawerRecyclerView
     */
    private void initNavDrawerRecyclerView() {
        //设置布局
        mLayoutManager = new LinearLayoutManager(this);
        mNavDrawerRecyclerView.setLayoutManager(mLayoutManager);//这里用线性显示 类似于listview
        mNavDrawerRecyclerView.setAdapter(new MainActivityNavDrawerRecyclerViewAdapter(this, new ArrayList()));
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
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);//这里用线性显示 类似于listview
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);//这里用线性宫格显示 类似于grid view
        mRecyclerView.setAdapter(new MainActivityRecyclerViewAdapter(this, new ArrayList()));
        mRecyclerView.setOnPullUpRefreshListener(new RexRecyclerView.OnPullUpRefreshListener() {
            @Override
            public void doRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                pullUpRefresh(curNavId, mRecyclerView.getAdapter().getItemCount(), 5);
            }
        });
    }

    private void getAsyncDrawerItems() {
//        new MainActivityGetDrawerDataTask(this, mDrawer).execute();
    }

    private void pullDownRefreshNavDrawer() {
        new MainActivityNavDrawerPullRefreshTask(this, mNavDrawerSwipeRefreshLayout, mNavDrawerRecyclerView).execute();
    }

    private void pullDownRefresh(int navId, int start, int limit) {
        new MainActivityPullRefreshTask(this, mSwipeRefreshLayout, mRecyclerView).execute(navId + "", start + "", limit + "");
    }

    private void pullUpRefresh(int navId, int start, int limit) {
        new MainActivityPullRefreshTask(this, mSwipeRefreshLayout, mRecyclerView, "up").execute(navId + "", start + "", limit + "");
    }

}