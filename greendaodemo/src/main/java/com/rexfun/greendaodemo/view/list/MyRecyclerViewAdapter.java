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
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private Cursor mCursor;

    public MyRecyclerViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mCursor = cursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mCursor.getCount() > 0) {
            mCursor.moveToPosition(position);
            String _tcCode = mCursor.getString(mCursor.getColumnIndex("TC_CODE"));
            String _tcPassword = mCursor.getString(mCursor.getColumnIndex("TC_PASSWORD"));
            holder.mTextViewUserCode.setText(_tcCode);
            holder.mTextViewUserPassword.setText(_tcPassword);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectUtil.InjectView(id=R.id.user_code) TextView mTextViewUserCode;
        @InjectUtil.InjectView(id=R.id.user_password) TextView mTextViewUserPassword;
        MyViewHolder(View view) {
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
