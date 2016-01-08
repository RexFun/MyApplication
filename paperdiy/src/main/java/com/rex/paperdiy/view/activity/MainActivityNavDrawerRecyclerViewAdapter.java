package com.rex.paperdiy.view.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rex.paperdiy.R;
import com.rexfun.androidlibrarytool.InjectUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by mac373 on 15/11/30.
 */
public class MainActivityNavDrawerRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityNavDrawerRecyclerViewAdapter.MyViewHolder> {
    private final Context ctx;
    private final List<Map<String, String>> mList;
    private final LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;
    private int curSelectedPosition = 0;

    public MainActivityNavDrawerRecyclerViewAdapter(Context context, List list) {
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
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.activity_main_nav_drawer_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mList.size() > 0) {
            holder.tvNavId.setText(mList.get(position).get("ID"));
            holder.tvNavName.setText(mList.get(position).get("NAME"));
            holder.itemView.setTag(position);
            if (position != curSelectedPosition) {
                holder.itemView.setSelected(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectUtil.InjectView(id=R.id.tv_nav_id) TextView tvNavId;
        @InjectUtil.InjectView(id=R.id.tv_nav_name) TextView tvNavName;
        MyViewHolder(View view) {
            super(view);
            InjectUtil.injectView(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v);
                    v.setSelected(true);
                    curSelectedPosition = Integer.valueOf(String.valueOf(v.getTag()));
                    notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * Item点击事件
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public interface OnItemClickListener
    {
        public void onClick(View v);
    }
}
