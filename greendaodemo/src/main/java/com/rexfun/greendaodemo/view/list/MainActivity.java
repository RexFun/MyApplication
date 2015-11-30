package com.rexfun.greendaodemo.view.list;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rexfun.androidlibrarytool.InjectUtil;
import com.rexfun.androidlibrarytool.InjectUtil.InjectView;
import com.rexfun.greendaodemo.R;
import com.rexfun.greendaodemo.dao.user.DaoMaster;
import com.rexfun.greendaodemo.dao.user.DaoSession;
import com.rexfun.greendaodemo.dao.user.UserDao;

public class MainActivity extends AppCompatActivity {

    @InjectView(id=R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(id=R.id.recycler_view) RecyclerView mRecyclerView;

    private DaoMaster.DevOpenHelper helper;
    private DaoMaster master;
    private DaoSession session;
    private UserDao dao;
    private Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.injectView(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDatas();
        initRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initDatas() {
        //第一个参数为Context，第二个参数为数据表的文件名，这个我们可以以"表名+s+'-db'"来命名，第三个通常为null
        helper = new DaoMaster.DevOpenHelper(this, "user-db", null);
        db = helper.getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        //得到StudentDAO对象，所以在这看来，对于这三个DAO文件，我们更能接触到的是StudentDao文件，进行CRUD操作也是通过StudentDao对象来操作
        dao = session.getUserDao();

        //遍历表中的所有数据
        cursor = db.query(dao.getTablename(), dao.getAllColumns(), null, null, null, null, null);
        //通过StudentDao的静态内部类得到Name字段对应表中的列名
//        String[] from = {UserDao.Properties.Number.columnName, UserDao.Properties.Password.columnName};
//        int[] to = {android.R.id.text1, android.R.id.text2};
//        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, SimpleCursorAdapter.NO_SELECTION);
//        mListView.setAdapter(adapter);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        mRecyclerView.setAdapter(new MyRecyclerViewAdapter(this, cursor));
    }
}
