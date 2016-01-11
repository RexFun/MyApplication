package com.rex.paperdiy.view.imageFullscreenPager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.rex.paperdiy.R;
import com.rexfun.androidlibrarytool.InjectUtil;

import java.util.ArrayList;

public class ImageFullscreenPagerActivity extends AppCompatActivity implements ImageFullscreenPagerActivityFragment.OnClickListener{
    @InjectUtil.InjectView(id = R.id.image_view_pager) ViewPager mViewPager;

    private boolean mVisible;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * 接收自ImageListActivity的Bundle
     */
    private Long image_id = 0l;
    private Long image_pid = 0l;
    private int image_sort = 0;

    private void receiveBundle() {
        Bundle b = getIntent().getBundleExtra("info");
        image_id = b.getLong("image_id");
        image_pid = b.getLong("image_pid");
        image_sort = b.getInt("image_sort");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fullscreen_pager);
        InjectUtil.injectView(this);
        receiveBundle();
        getSupportActionBar().setHomeButtonEnabled(true);//actionbar主按键可以被点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左上角返回键
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mVisible = true;

        initViewPager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home://返回
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mViewPager.setAdapter(new ImageFullscreenPagerActivityAdapter(getSupportFragmentManager(), this, new ArrayList<Fragment>()));
        loadData();
    }

    private void loadData() {
        new ImageFullscreenPagerActivityGetPagerDataTask(this, mViewPager, image_sort-1).execute(image_pid.toString());
    }

    @Override
    public void onClick(View v) {
        toggle();
    }
}
