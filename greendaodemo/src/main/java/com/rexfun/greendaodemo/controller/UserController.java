package com.rexfun.greendaodemo.controller;

import android.content.Context;
import android.database.Cursor;

import com.rexfun.greendaodemo.dao.user.User;
import com.rexfun.greendaodemo.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac373 on 15/12/8.
 */
public class UserController {
    private Context mContext;
    private UserService mUserService;

    public UserController(Context mContext) {
        this.mContext = mContext;
        this.mUserService = new UserService(this.mContext);
    }

    public List<Map<String,String>> getList() {
        return mUserService.getList();
    }

    public List<Map<String,String>> getPageList(int start, int limit) {
        return mUserService.getPageList(start, limit);
    }

    public void insert(final User po) {
        mUserService.insert(po);
    }
}
