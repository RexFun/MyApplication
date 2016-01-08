package com.rex.paperdiy.view.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by mac373 on 16/1/8.
 */
public class ImageFullscreenPagerActivityAdapter extends FragmentPagerAdapter {

    private Context ctx;
    private List<Fragment> mFragmentList;

    public ImageFullscreenPagerActivityAdapter(FragmentManager fm, Context ctx, List<Fragment> fragmentList) {
        super(fm);
        this.ctx = ctx;
        this.mFragmentList = fragmentList;
    }

    public void refreshItems(List list) {
        mFragmentList.clear();
        mFragmentList.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "第 "+(++position)+" 步";
    }
}
