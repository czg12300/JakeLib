package com.jake.library.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter {

    protected Context mContext;


    protected ArrayList<T> mDataList;


    protected Context getContext() {
        return mContext;
    }

    public BaseViewPagerAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<T>();
    }

    public View inflate(int layoutId) {
        return View.inflate(mContext, layoutId, null);
    }

    public void addDatas(ArrayList<T> datas) {
        if (datas != null && datas.size() > 0) {
            mDataList.addAll(datas);
        }
    }

    public void setDatas(ArrayList<T> datas) {
        if (datas != null && datas.size() > 0) {
            mDataList = datas;
        }
    }

    public void addData(T data) {
        if (data != null) {
            this.mDataList.add(data);
        }
    }

    public boolean deleteData(T data) {
        if (mDataList != null && data != null) {
            return mDataList.remove(data);
        }
        return false;
    }

    public void clearDatas() {
        if (mDataList != null) {
            mDataList.clear();
        }
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        } else
            return 0;
    }

    public ArrayList<T> getDataList() {
        return mDataList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = getView(position);
        container.addView(convertView);
        return convertView;
    }

    /**
     * 创建view
     *
     * @param position
     * @return
     */
    protected abstract View getView(int position);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

}