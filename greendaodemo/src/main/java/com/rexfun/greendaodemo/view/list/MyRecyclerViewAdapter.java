package com.rexfun.greendaodemo.view.list;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rexfun.androidlibrarytool.InjectUtil;
import com.rexfun.greendaodemo.R;

/**
 * Created by mac373 on 15/11/30.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private Cursor mCursor;
    private String[] mTitles;

    public MyRecyclerViewAdapter(Context context, Cursor cursor) {
        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mCursor = cursor;

    }

    @Override
    public MyTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyTextViewHolder(mLayoutInflater.inflate(R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyTextViewHolder holder, int position) {
        System.out.println("数据量：" + mCursor.getCount());
        if (mCursor.getCount() > 0) {
            mCursor.moveToPosition(position);
            String _tcCode = mCursor.getString(mCursor.getColumnIndex("tc_code"));
            String _tcPassword = mCursor.getString(mCursor.getColumnIndex("tc_password"));
            holder.mTextViewUserCode.setText(_tcCode);
            holder.mTextViewUserPassword.setText(_tcPassword);
        }
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }

    public static class MyTextViewHolder extends RecyclerView.ViewHolder {
        @InjectUtil.InjectView(id=R.id.user_code) TextView mTextViewUserCode;
        @InjectUtil.InjectView(id=R.id.user_password) TextView mTextViewUserPassword;
        MyTextViewHolder(View view) {
            super(view);
            InjectUtil.injectView(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}
