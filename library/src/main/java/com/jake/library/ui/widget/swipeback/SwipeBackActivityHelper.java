
package com.jake.library.ui.widget.swipeback;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.jake.library.utils.ThemeUtil;

public class SwipeBackActivityHelper {
//    private Activity mActivity;
//
//    private SwipeBackLayout mSwipeBackLayout;
//
//    public SwipeBackActivityHelper(Activity activity) {
//        mActivity = activity;
//    }
//
//    @SuppressWarnings("deprecation")
//    public void onActivityCreate() {
//        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
//        mSwipeBackLayout = new SwipeBackLayout(mActivity);
//        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
//            @Override
//            public void onScrollStateChange(int state, float scrollPercent) {
//                if (state == SwipeBackLayout.STATE_IDLE && scrollPercent == 0) {
//                    ThemeUtil.convertActivityFromTranslucent(mActivity);
//                }
//            }
//
//            @Override
//            public void onEdgeTouch(int edgeFlag) {
//                ThemeUtil.convertActivityToTranslucent(mActivity);
//            }
//
//            @Override
//            public void onScrollOverThreshold() {
//
//            }
//        });
//    }
//
//    public void onPostCreate() {
//        mSwipeBackLayout.attachToActivity(mActivity);
//        ThemeUtil.convertActivityFromTranslucent(mActivity);
//    }
//
//    public View findViewById(int id) {
//        if (mSwipeBackLayout != null) {
//            return mSwipeBackLayout.findViewById(id);
//        }
//        return null;
//    }
//
//    public SwipeBackLayout getSwipeBackLayout() {
//        return mSwipeBackLayout;
//    }
}
