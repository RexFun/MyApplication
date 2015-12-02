package com.rexfun.greendaodemo.dao.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rexfun.greendaodemo.R;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1 ;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        UserDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        UserDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {
        public Context context;
        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
            importData(context, db);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
            importData(context, db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(UserDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    private static void importData(Context context, SQLiteDatabase db) {
        String[] aImportDataSql= context.getResources().getStringArray(R.array.import_data);
        for(int i=0; i<aImportDataSql.length; i++) {
            db.execSQL(aImportDataSql[i]);
        }
    }
}