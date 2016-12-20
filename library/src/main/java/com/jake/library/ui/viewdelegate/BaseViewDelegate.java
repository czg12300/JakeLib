package com.jake.library.ui.viewdelegate;

import android.app.Activity;
import android.view.View;

/**
 * 描述：所有view层的代理基类
 *
 * @author jakechen
 * @since 2016/12/20 14:54
 */

public abstract class BaseViewDelegate {
    private View mContentView;
    private Activity mActivity;

    public BaseViewDelegate(View view) {
        this(null, view);
    }

    public BaseViewDelegate(Activity activity, View view) {
        mContentView = view;
        mActivity = activity;
        initView();
    }

    protected abstract void initView();

    public View getView() {
        return mContentView;
    }

    protected View findViewById(int id) {
        return mContentView != null ? mContentView.findViewById(id) : null;
    }

}
