
package com.jake.jakelib;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jake.jakelib.demo.CoordinatorLayoutActivity;
import com.jake.jakelib.demo.RequestDemoActivity;
import com.jake.jakelib.demo.SkinTestActivity;
import com.jake.jakelib.http.OkHttpDataFetchLoader;
import com.jake.library.data.http.HttpEngine;
import com.jake.library.data.http.HttpRequestCallback;
import com.jake.library.data.http.HttpUrlConnectDataFetchLoader;
import com.jake.library.data.http.IResponse;
import com.jake.library.data.http.RequestPackage;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MyAdapter(this, getActivityList()));
        Response response = new Response();
        RequestPackage requestPackage = new RequestPackage();
        HttpEngine.install(HttpEngine.Builder
                .create()
                .setDataFetchLoader(new OkHttpDataFetchLoader())
                .setExecutorService(null));
        HttpEngine.with(HttpEngine.Method.POST)
                .setResponse(response)
                .setRequestPackage(requestPackage)
                .setCallback(new HttpRequestCallback<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                }).request();
    }

    private List<Info> getActivityList() {
        List<Info> list = new ArrayList<Info>();
        list.add(new Info("RequestDemoActivity", RequestDemoActivity.class));
        list.add(new Info("SkinTestActivity", SkinTestActivity.class));
        list.add(new Info("CoordinatorLayoutActivity", CoordinatorLayoutActivity.class));
        return list;
    }

    static class Response implements IResponse {

        @Override
        public void parser(String result) throws JSONException {

        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Info info = (Info) l.getAdapter().getItem(position);
        startActivity(new Intent(this, info.clazz));
    }

    class Info {
        String label;

        Class<?> clazz;

        public Info(String label, Class<?> clazz) {
            this.label = label;
            this.clazz = clazz;
        }
    }

    class MyAdapter extends BaseAdapter {
        List<Info> list;

        Context context;

        public MyAdapter(Context context, List<Info> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                tv = new TextView(context);
                tv.setPadding(40, 40, 0, 40);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                tv.setTextColor(Color.parseColor("#363636"));
                convertView = tv;
            } else {
                tv = (TextView) convertView;
            }
            tv.setText(list.get(position).label);
            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }
}
