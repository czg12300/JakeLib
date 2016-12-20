package com.jake.library.ui.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitleList;

    public CommonFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.mFragments = list;
    }

    public ArrayList<String> getTitleList() {
        return mTitleList;
    }

    public void setTitleList(ArrayList<String> titleList) {
        this.mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (mFragments != null && position > -1 && position < mFragments.size()) {
            fragment = mFragments.get(position);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String result = null;
        try {
            if (mTitleList != null) {
                result = mTitleList.get(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        }
        return result;
    }

    public void release() {
        if (mFragments != null) {
            mFragments.clear();
        }
        if (mTitleList != null) {
            mTitleList.clear();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    /**
     * 在调用notifyDataSetChanged()方法后，随之会触发该方法，根据该方法返回的值来确定是否更新
     * object对象为Fragment，具体是当前显示的Fragment和它的前一个以及后一个
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;   // 返回发生改变，让系统重新加载
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}