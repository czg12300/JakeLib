
package com.jake.library.ui.widget.swipeback;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.ViewGroup;

/**
 * Created by Mr.Jude on 2015/8/3. 每个滑动页面的管理
 */
public class SwipeBackPage implements SwipeBackLayout.SwipeListener {
    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;
    private int offset = 500;
    private boolean isSwipeBackEnable = true;

    private SwipeBackPage(Activity activity) {
        this.mActivity = activity;
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        mSwipeBackLayout = new SwipeBackLayout(mActivity);
        mSwipeBackLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public void addListener(SwipeBackLayout.SwipeListener listener) {
        mSwipeBackLayout.addSwipeListener(listener);
    }

    public void removeListener(SwipeBackLayout.SwipeListener listener) {
        mSwipeBackLayout.removeSwipeListener(listener);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public void scrollToFinishActivity() {
        mSwipeBackLayout.scrollToFinishActivity();
    }

    @Override
    public void onScroll(float percent, int px) {
        if (Build.VERSION.SDK_INT > 11) {
            SwipeBackPage page = SwipeBackHelper.getPrePage(this);
            if (page != null) {
                page.getSwipeBackLayout().setX(-offset * Math.max(1 - percent, 0));
                if (percent == 0) {
                    page.getSwipeBackLayout().setX(0);
                }
            }
        }
    }

    @Override
    public void onEdgeTouch() {

    }

    @Override
    public void onScrollToClose() {
        if (Build.VERSION.SDK_INT > 11) {
            SwipeBackPage page = SwipeBackHelper.getPrePage(this);
            if (page != null) {
                page.getSwipeBackLayout().setX(0);
            }
        }
    }

    public void setSwipeBackEnable(boolean enable) {
        isSwipeBackEnable = enable;
        if (enable) {
            mSwipeBackLayout.attachToActivity(mActivity);
        } else {
            mSwipeBackLayout.removeFromActivity(mActivity);
        }
        mSwipeBackLayout.setEnableGesture(enable);
    }

    public boolean isSwipeBackEnable() {
        return isSwipeBackEnable;
    }

    public static class Builder {
        private SwipeBackPage swipeBackPage;

        private Builder(Activity activity) {
            swipeBackPage = new SwipeBackPage(activity);
        }

        public static Builder create(Activity activity) {
            return new Builder(activity);
        }

        @TargetApi(11)
        public Builder setSwipeRelateEnable(boolean enable) {
            if (enable) {
                swipeBackPage.addListener(swipeBackPage);
            } else {
                swipeBackPage.removeListener(swipeBackPage);
            }
            return this;
        }

        public Builder setSwipeRelateOffset(int offset) {
            swipeBackPage.offset = offset;
            return this;
        }

        public Builder setShowScrim(boolean isShow) {
            swipeBackPage.mSwipeBackLayout.setShowScrim(isShow);
            return this;
        }

        // 是否可滑动关闭
        public Builder setSwipeBackEnable(boolean enable) {
            swipeBackPage.setSwipeBackEnable(enable);
            return this;
        }

        // 可滑动的范围。百分比。200表示为左边200px的屏幕
        public Builder setSwipeEdge(int swipeEdge) {
            swipeBackPage.mSwipeBackLayout.setEdgeSize(swipeEdge);
            return this;
        }

        // 可滑动的范围。百分比。0.2表示为左边20%的屏幕
        public Builder setSwipeEdgePercent(float swipeEdgePercent) {
            swipeBackPage.mSwipeBackLayout.setEdgeSizePercent(swipeEdgePercent);
            return this;
        }

        // 对横向滑动手势的敏感程度。0为迟钝 1为敏感
        public Builder setSwipeSensitivity(float sensitivity) {
            swipeBackPage.mSwipeBackLayout.setSensitivity(sensitivity);
            return this;
        }

        // 底层阴影颜色
        public Builder setScrimColor(int color) {
            swipeBackPage.mSwipeBackLayout.setScrimColor(color);
            return this;
        }

        // 触发关闭Activity百分比
        public Builder setClosePercent(float percent) {
            swipeBackPage.mSwipeBackLayout.setScrollThreshold(percent);
            return this;
        }

        public Builder setDisallowInterceptTouchEvent(boolean disallowIntercept) {
            swipeBackPage.mSwipeBackLayout.setDisallowInterceptTouchEvent(disallowIntercept);
            return this;
        }

        public SwipeBackPage build() {
            return swipeBackPage;
        }

    }

}
