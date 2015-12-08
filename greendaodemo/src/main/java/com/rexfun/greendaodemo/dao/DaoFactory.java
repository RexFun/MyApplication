package com.rexfun.greendaodemo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rexfun.greendaodemo.dao.user.DaoMaster;
import com.rexfun.greendaodemo.dao.user.DaoSession;
import com.rexfun.greendaodemo.dao.user.UserDao;


/**
 * Created by mac373 on 15/12/2.
 */
public class DaoFactory {

    private static DaoMaster.DevOpenHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private static DaoMaster.DevOpenHelper getDevOpenHelper(Context ctx) {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(ctx, "user-db", null);
        }
        return mHelper;
    }

    public static SQLiteDatabase getWritableDatabase(Context ctx) {
        return getDevOpenHelper(ctx).getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase(Context ctx) {
        return getDevOpenHelper(ctx).getReadableDatabase();
    }

    public static DaoMaster getDaoMaster(Context ctx) {
        if (mDaoMaster == null) {
            mDaoMaster = new DaoMaster(getWritableDatabase(ctx));
        }
        return mDaoMaster;
    }

    public static DaoSession getDaoSession(Context ctx) {
        if (mDaoSession == null) {
            mDaoSession = getDaoMaster(ctx).newSession();
        }
        return mDaoSession;
    }

    public static UserDao getUserDao(Context ctx) {
        return getDaoSession(ctx).getUserDao();
    }
}
