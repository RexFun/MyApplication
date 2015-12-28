package com.rex.paperdiy.view.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.rex.paperdiy.controller.MainController;
import com.rexfun.androidlibraryhttp.HttpResultObj;

import java.util.List;
import java.util.Map;


/**
 * Created by mac373 on 15/11/25.
 */
public class GetAsyncDrawerItemsTask extends AsyncTask<String, Integer, HttpResultObj<String>> {

    private Context ctx;
    private MainController controller;
    private Drawer drawer;

    public GetAsyncDrawerItemsTask(Context _ctx, Drawer _drawer) {
        this.ctx = _ctx;
        this.drawer = _drawer;
        this.controller = new MainController(_ctx);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected HttpResultObj<String> doInBackground(String... params) {
        HttpResultObj<String> result = controller.getDataJson();
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(HttpResultObj<String> result) {
        Gson gson = new Gson();
        if(!result.isSuc()) {
            Toast.makeText(ctx, result.getErrMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        List<Map<String,String>> list = (List<Map<String,String>>)gson.fromJson(result.getData(),  new TypeToken<List<Map<String,String>>>(){}.getType());
        for(int i=0; i<list.size(); i++) {
            PrimaryDrawerItem item = new PrimaryDrawerItem();
            Map<String,String> m = list.get(i);
            for (String key : m.keySet()) {
                item.withIdentifier(Integer.valueOf(m.get("ID").toString()));
                item.withName(m.get("NAME").toString());
                item.withTag(m.get("NAME").toString());
                item.withIcon(android.R.drawable.ic_menu_gallery);
            }
            drawer.addItem(item);
        }
    }
}
