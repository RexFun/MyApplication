package com.rex.paperdiy.view.imageFullscreenPager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rex.paperdiy.controller.MainController;
import com.rexfun.androidlibraryhttp.HttpResultObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mac373 on 16/1/8.
 */
public class ImageFullscreenPagerActivityGetPagerDataTask extends AsyncTask<String, Integer, HttpResultObj<String>> {
    private Context ctx;
    private MainController controller;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int currentItem;

    public ImageFullscreenPagerActivityGetPagerDataTask(Context ctx, TabLayout tabLayout, ViewPager viewPager, int currentItem) {
        this.ctx = ctx;
        this.controller = new MainController(this.ctx);
        this.mTabLayout = tabLayout;
        this.mViewPager = viewPager;
        this.currentItem = currentItem;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected HttpResultObj<String> doInBackground(String... params) {
        HttpResultObj<String> result = controller.getPaperImageListByPid(params[0]);
        return result;
    }

    @Override
    protected void onPostExecute(HttpResultObj<String> result) {
        Gson gson = new Gson();
        if(!result.isSuc()) {
            Toast.makeText(ctx, result.getErrMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        List<Map<String,String>> list = (List<Map<String,String>>)gson.fromJson(result.getData(), new TypeToken<List<Map<String,String>>>(){}.getType());
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        for(Map<String,String> m : list) {
            fragmentList.add(ImageFullscreenPagerActivityFragment.newInstance(m.get("ID")));
        }
        ((ImageFullscreenPagerActivityAdapter)mViewPager.getAdapter()).refreshItems(fragmentList);
        mViewPager.setCurrentItem(currentItem);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
