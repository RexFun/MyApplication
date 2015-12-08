package com.rexfun.greendaodemo.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rexfun.greendaodemo.dao.DaoFactory;
import com.rexfun.greendaodemo.dao.user.User;
import com.rexfun.greendaodemo.dao.user.UserDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac373 on 15/12/8.
 */
public class UserService {
    private Context ctx;
    private SQLiteDatabase wdb;
    private SQLiteDatabase rdb;
    private UserDao mUserDao;

    public UserService(Context ctx) {
        this.ctx = ctx;
        this.wdb = DaoFactory.getWritableDatabase(ctx);
        this.rdb = DaoFactory.getReadableDatabase(ctx);
        this.mUserDao = DaoFactory.getUserDao(ctx);
    }

    public Cursor getCursor() {
        return rdb.query(mUserDao.getTablename(), mUserDao.getAllColumns(), null, null, null, null, null);
    }

    public Cursor getPageCursor(int start, int limit) {
        return rdb.query(mUserDao.getTablename(), mUserDao.getAllColumns(), null, null, null, null, null, start+","+limit);
    }

    public List<Map<String,String>> getList() {
        List<Map<String,String>> mList = new ArrayList<Map<String,String>>();
        Cursor c = this.getCursor();
        while(c.moveToNext()) {
            Map m = new HashMap<String, String>();
            m.put("TC_CODE", c.getString(c.getColumnIndex("TC_CODE")));
            m.put("TC_PASSWORD", c.getString(c.getColumnIndex("TC_PASSWORD")));
            m.put("TC_AGE", c.getString(c.getColumnIndex("TC_AGE")));
            mList.add(m);
        }
        c.close();
        return mList;
    }

    public List<Map<String,String>> getPageList(int start, int limit) {
        List<Map<String,String>> mList = new ArrayList<Map<String,String>>();
        Cursor c = this.getPageCursor(start, limit);
        while(c.moveToNext()) {
            Map m = new HashMap<String, String>();
            m.put("TC_CODE", c.getString(c.getColumnIndex("TC_CODE")));
            m.put("TC_PASSWORD", c.getString(c.getColumnIndex("TC_PASSWORD")));
            m.put("TC_AGE", c.getString(c.getColumnIndex("TC_AGE")));
            mList.add(m);
        }
        c.close();
        return mList;
    }

    public void insert(final User po) {
        DaoFactory.getDaoSession(ctx).runInTx(new Runnable() {
            @Override
            public void run() {
                mUserDao.insert(po);
            }
        });
    }
}
