package com.rex.paperdiy.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rex.paperdiy.R;
import com.rex.paperdiy.view.imageList.ImageListActivity;
import com.rexfun.androidlibrarytool.InjectUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by mac373 on 15/11/30.
 */
public class MainActivityRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityRecyclerViewAdapter.MyViewHolder> {
    private final Context ctx;
    private final List<Map<String, String>> mList;
    private final LayoutInflater mLayoutInflater;

    public MainActivityRecyclerViewAdapter(Context context, List list) {
        this.ctx = context;
        this.mList = list;
        this.mLayoutInflater = LayoutInflater.from(this.ctx);
    }

    public void insertItems(List list) {
        if(list.size() > 0) {
            mList.addAll(list);
            this.notifyItemInserted(mList.size() - 1);
        }
    }

    public void refreshItems(List list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.activity_main_content_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mList.size() > 0) {
            holder.tvNavName.setText(mList.get(position).get("NAV_NAME"));
            holder.tvPaperModelId.setText(mList.get(position).get("ID"));
            holder.tvPaperModelName.setText(mList.get(position).get("NAME"));
            MainActivity.mImageLoader.displayImage(
                    ctx.getString(R.string.app_path)+"/client/paperimage/getPaperImageByPidAndMaxSort.action?pid=" + mList.get(position).get("ID"),
                    holder.ivPaperModelBmp,
                    MainActivity.mDisplayImageOptions);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectUtil.InjectView(id=R.id.tv_nav_name) TextView tvNavName;
        @InjectUtil.InjectView(id=R.id.tv_paper_model_id) TextView tvPaperModelId;
        @InjectUtil.InjectView(id=R.id.tv_paper_model_name) TextView tvPaperModelName;
        @InjectUtil.InjectView(id=R.id.iv_paper_model_bmp) ImageView ivPaperModelBmp;
        MyViewHolder(View view) {
            super(view);
            InjectUtil.injectView(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putCharSequence("nav_name", tvNavName.getText());
                    b.putLong("model_id", Long.valueOf(tvPaperModelId.getText().toString()));
                    b.putCharSequence("model_name", tvPaperModelName.getText());
                    MainActivityRecyclerViewAdapter.this.ctx.startActivity(new Intent().setClass(MainActivityRecyclerViewAdapter.this.ctx, ImageListActivity.class).putExtra("info", b));
                }
            });
        }
    }
}
