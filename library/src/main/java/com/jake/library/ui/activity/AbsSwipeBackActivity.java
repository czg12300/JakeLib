package com.jake.library.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.jake.library.R;
import com.jake.library.ui.widget.swipeback.SwipeBackHelper;
import com.jake.library.ui.widget.swipeback.SwipeBackPage;

import java.lang.ref.WeakReference;

/**
 * 描述：具有swipe back的activity
 *
 * @author jakechen
 * @since 2016/12/19 11:27
 */

public class AbsSwipeBackActivity extends AbsBroadcastActivity {
    private WeakReference<SwipeBackPage> mSwipeBackPage;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (willUseSwipeBack()) {
            SwipeBackPage.Builder builder = SwipeBackPage.Builder.create(this);
            builder.setSwipeBackEnable(true)
                    .setSwipeSensitivity(1f)
                    .setShowScrim(false)
                    .setSwipeEdge(getResources().getDisplayMetrics().widthPixels)
                    .setSwipeRelateOffset(getResources().getDisplayMetrics().widthPixels / 4)
                    .setSwipeRelateEnable(true);
            mSwipeBackPage = new WeakReference<>(builder.build());
            SwipeBackHelper.addActivity(getSwipeBackPage());
            SwipeBackPage swipeBackPage = getSwipeBackPage();
            if (swipeBackPage != null && swipeBackPage.isSwipeBackEnable()) {
                overridePendingTransition(R.anim.swipe_back_activity_open, R.anim.swipe_back_activity_exit);
            }
        }
    }

    /**
     * 配置swipe back的自定义选项
     *
     * @param builder
     */
    protected void configSwipeBack(SwipeBackPage.Builder builder) {

    }

    protected boolean willUseSwipeBack() {
        return true;
    }

    /**
     * 设置swipe back是否可用
     *
     * @param enable
     */
    public void setSwipeEnable(boolean enable) {
        SwipeBackPage swipeBackPage = getSwipeBackPage();
        if (swipeBackPage != null) {
            swipeBackPage.setSwipeBackEnable(enable);
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        SwipeBackPage swipeBackPage = getSwipeBackPage();
        if (swipeBackPage != null) {
            swipeBackPage.onPostCreate();
        }
    }

    protected SwipeBackPage getSwipeBackPage() {
        return mSwipeBackPage != null ? mSwipeBackPage.get() : null;
    }

    @Override
    public void finish() {
        super.finish();
        SwipeBackPage swipeBackPage = getSwipeBackPage();
        if (swipeBackPage != null && swipeBackPage.isSwipeBackEnable()) {
            overridePendingTransition(R.anim.swipe_back_activity_open, R.anim.swipe_back_activity_exit);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.removeActivity(getSwipeBackPage());
        if (mSwipeBackPage != null) {
            mSwipeBackPage.clear();
            mSwipeBackPage = null;
        }
    }
}
