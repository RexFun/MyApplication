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
            String _tcAge = mCursor.getString(mCursor.getColumnIndex("TC_AGE"));
            holder.mTextViewUserCode.setText(_tcCode);
            holder.mTextViewUserPassword.setText(_tcPassword);
            holder.mTextViewUserAge.setText(_tcAge);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
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
                    v.setSelected(true);
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}
