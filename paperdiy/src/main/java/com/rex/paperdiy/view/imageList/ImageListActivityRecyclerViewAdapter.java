package com.rex.paperdiy.view.imageList;

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
import com.rex.paperdiy.view.imageFullscreenPager.ImageFullscreenPagerActivity;
import com.rex.paperdiy.view.main.MainActivity;
import com.rexfun.androidlibrarytool.InjectUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by mac373 on 15/11/30.
 */
public class ImageListActivityRecyclerViewAdapter extends RecyclerView.Adapter<ImageListActivityRecyclerViewAdapter.MyViewHolder> {
    private final Context ctx;
    private final List<Map<String, String>> mList;
    private final LayoutInflater mLayoutInflater;

    public ImageListActivityRecyclerViewAdapter(Context context, List list) {
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
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.activity_image_list_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mList.size() > 0) {
            holder.tvPaperImageId.setText(mList.get(position).get("ID"));
            holder.tvPaperImagePid.setText(mList.get(position).get("PID"));
            holder.tvPaperImageSort.setText(mList.get(position).get("SORT"));
            holder.tvStep.setText("第 "+mList.get(position).get("SORT")+" 步");
            MainActivity.mImageLoader.displayImage(
                    ctx.getString(R.string.app_path) + "/client/paperimage/getPaperImageById.action?id=" + mList.get(position).get("ID"),
                    holder.ivPaperImageBmp,
                    MainActivity.mDisplayImageOptions);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectUtil.InjectView(id=R.id.tv_paper_image_id) TextView tvPaperImageId;
        @InjectUtil.InjectView(id=R.id.tv_paper_image_pid) TextView tvPaperImagePid;
        @InjectUtil.InjectView(id=R.id.tv_paper_image_sort) TextView tvPaperImageSort;
        @InjectUtil.InjectView(id=R.id.iv_paper_image_bmp) ImageView ivPaperImageBmp;
        @InjectUtil.InjectView(id=R.id.tv_step) TextView tvStep;
        MyViewHolder(View view) {
            super(view);
            InjectUtil.injectView(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putLong("image_id", Long.valueOf(tvPaperImageId.getText().toString()));
                    b.putLong("image_pid", Long.valueOf(tvPaperImagePid.getText().toString()));
                    b.putInt("image_sort", Integer.valueOf(tvPaperImageSort.getText().toString()));
                    ImageListActivityRecyclerViewAdapter.this.ctx.startActivity(new Intent().setClass(ImageListActivityRecyclerViewAdapter.this.ctx, ImageFullscreenPagerActivity.class).putExtra("info", b));
                }
            });
        }
    }
}
