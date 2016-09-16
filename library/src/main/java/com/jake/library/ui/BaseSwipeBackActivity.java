package com.jake.library.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.jake.library.ui.widget.swipeback.SwipeBackActivityHelper;
import com.jake.library.ui.widget.swipeback.SwipeBackLayout;
import com.jake.library.utils.ThemeUtil;

/**
 * 描述：右滑退出的activity
 *
 * @author jakechen
 * @since 2016/9/16 15:35
 */

public class BaseSwipeBackActivity extends BaseActivity {
    protected SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SwipeBackHelper.onCreate(this);
//        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true).setSwipeSensitivity(0.5f)
//                .setSwipeRelateEnable(false);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        getSwipeBackLayout().setEdgeSize(width / 8);
    }

//    public void setSwipeBackEnable(boolean enable) {
//        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(enable);
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        SwipeBackHelper.onPostCreate(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SwipeBackHelper.onDestroy(this);
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    public void scrollToFinishActivity() {
        ThemeUtil.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
