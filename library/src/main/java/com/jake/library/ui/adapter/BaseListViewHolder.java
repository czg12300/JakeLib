package com.jake.library.ui.adapter;

import android.view.View;

/**
 * 描述：用于list adapter 的viewHolder基类
 *
 * @author jakechen
 * @since 2016/12/20 16:24
 */

public class BaseListViewHolder {

    public View itemView;

    public BaseListViewHolder(View itemView) {
        this.itemView = itemView;
    }

    protected View findViewById(int id) {
        return itemView != null ? itemView.findViewById(id) : null;
    }
}
