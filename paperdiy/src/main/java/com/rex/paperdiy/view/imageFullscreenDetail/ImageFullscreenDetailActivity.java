package com.rex.paperdiy.view.imageFullscreenDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.rex.paperdiy.R;
import com.rexfun.androidlibrarytool.InjectUtil;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFullscreenDetailActivity extends AppCompatActivity {
    @InjectUtil.InjectView(id=R.id.fullscreen_layout) View mFullscreenLayout;
    @InjectUtil.InjectView(id=R.id.toolbar) Toolbar mToolbar;
    @InjectUtil.InjectView(id = R.id.iv_paper_image_bmp) ImageView mTvPaperImageBmp;

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
        setContentView(R.layout.activity_image_fullscreen_detail);
        InjectUtil.injectView(this);
        receiveBundle();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);//actionbar主按键可以被点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左上角返回键
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //全屏隐藏statusBar
        mFullscreenLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //加载图片
        Picasso.with(this).load(getString(R.string.app_path) + "/client/paperimage/getPaperImageById.action?id=" + image_id).into(mTvPaperImageBmp);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(mTvPaperImageBmp);
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
}
