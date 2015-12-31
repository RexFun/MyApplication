package com.rex.paperdiy.view.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rex.paperdiy.R;
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

    public void addListItem(List list) {
        mList.addAll(list);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.activity_image_list_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mList.size() > 0) {
            holder.tvPaperImageId.setText(mList.get(position).get("ID"));
            holder.tvPaperImageSort.setText("第 "+mList.get(position).get("SORT")+" 步");
            MainActivity.mImageLoader.displayImage(
                    ctx.getString(R.string.app_path)+"/client/paperimage/getPaperImageById.action?id=" + mList.get(position).get("ID"),
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
        @InjectUtil.InjectView(id=R.id.tv_paper_image_sort) TextView tvPaperImageSort;
        @InjectUtil.InjectView(id=R.id.iv_paper_image_bmp) ImageView ivPaperImageBmp;
        MyViewHolder(View view) {
            super(view);
            InjectUtil.injectView(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Bundle b = new Bundle();
//                    b.putLong("model_id", Long.valueOf(tvPaperImageId.getText().toString()));
//                    b.putCharSequence("model_name", tvPaperImageName.getText());
//                    ImageListActivityRecyclerViewAdapter.this.ctx.startActivity(new Intent().setClass(ImageListActivityRecyclerViewAdapter.this.ctx, ImageListActivity.class).putExtra("info", b));
                }
            });
        }
    }
}
