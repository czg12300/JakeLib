
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
import com.jake.jakelib.demo.DownloadActivity;
import com.jake.jakelib.demo.RequestDemoActivity;
import com.jake.jakelib.demo.SkinTestActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MyAdapter(this, getActivityList()));
    }

    private List<Info> getActivityList() {
        List<Info> list = new ArrayList<Info>();
        list.add(new Info("DownloadActivity", DownloadActivity.class));
        list.add(new Info("RequestDemoActivity", RequestDemoActivity.class));
        list.add(new Info("SkinTestActivity", SkinTestActivity.class));
        list.add(new Info("CoordinatorLayoutActivity", CoordinatorLayoutActivity.class));
        return list;
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
