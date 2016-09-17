package com.jake.library.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jake.library.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：recycleView的adapter基类
 *
 * @author jakechen
 * @since 2016/9/17 13:14
 */

public abstract class BaseRecycleViewAdapter<Entity, ViewHolder extends BaseRecycleViewAdapter.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;

    protected List<Entity> mList = new ArrayList<>();

    public BaseRecycleViewAdapter(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }


    /**
     * 获取所有数据
     *
     * @return
     */
    public List<Entity> getDatas() {
        return mList;
    }


    /**
     * 添加新数据到末尾
     *
     * @param data
     */
    public void add(Entity data) {
        if (data != null) {
            checkDataIsNull();
            mList.add(data);
        }
    }

    private void checkDataIsNull() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
    }

    /**
     * 添加数据到指定位置
     *
     * @param position
     * @param data
     */
    public void add(int position, Entity data) {
        if (data != null) {
            checkDataIsNull();
            mList.add(position, data);
        }
    }

    /**
     * 添加新数据到末尾
     *
     * @param datas
     */
    public void add(List<Entity> datas) {
        if (ObjectUtil.isNotNull(datas)) {
            checkDataIsNull();
            mList.addAll(datas);
        }
    }

    /**
     * 添加数据到指定位置
     *
     * @param position
     * @param data
     */
    public void add(int position, List<Entity> data) {
        if (ObjectUtil.isNotNull(data)) {
            checkDataIsNull();
            mList.addAll(position, data);
        }
    }

    /**
     * 清空列表，
     * 放入新数据
     *
     * @param data
     */
    public void set(Entity data) {
        if (ObjectUtil.isNotNull(mList)) {
            mList.clear();
        } else {
            mList = new ArrayList<>();
        }
        if (ObjectUtil.isNotNull(data)) {
            mList.add(data);
        }
    }


    /**
     * 清空列表，
     * 放入新数据
     *
     * @param datas
     */
    public void set(List<Entity> datas) {
        if (ObjectUtil.isNotNull(datas)) {
            mList = datas;
        }
    }


    /**
     * 删除指定的位置数据
     *
     * @param position
     * @return
     */
    public Entity del(int position) {
        if (ObjectUtil.isNotNull(mList) && position < mList.size()) {
            Entity t = mList.remove(position);
            return t;
        }
        return null;
    }


    public void clear() {
        if (ObjectUtil.isNotNull(mList)) {
            mList.clear();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        protected View findViewById(int id) {
            return itemView != null ? itemView.findViewById(id) : null;
        }
    }


}
