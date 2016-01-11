package com.rexfun.greendaodemo.view.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rexfun.androidlibrarytool.InjectUtil;
import com.rexfun.greendaodemo.R;

import java.util.List;
import java.util.Map;

/**
 * Created by mac373 on 15/11/30.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private final Context ctx;
    private final List<Map<String, String>> mList;
    private final LayoutInflater mLayoutInflater;

    public MyRecyclerViewAdapter(Context context, List list) {
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

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println("数据量:"+mList.size());
        if (mList.size() > 0) {
            holder.mTextViewUserCode.setText(mList.get(position).get("TC_CODE"));
            holder.mTextViewUserPassword.setText(mList.get(position).get("TC_PASSWORD"));
            holder.mTextViewUserAge.setText(mList.get(position).get("TC_AGE") );
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectUtil.InjectView(id=R.id.user_code) TextView mTextViewUserCode;
        @InjectUtil.InjectView(id=R.id.user_password) TextView mTextViewUserPassword;
        @InjectUtil.InjectView(id=R.id.user_age) TextView mTextViewUserAge;
        MyViewHolder(View view) {
            super(view);
            InjectUtil.injectView(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    v.setSelected(true);
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}
