package com.jake.library.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 描述：抽象titleBar的activity
 *
 * @author jakechen
 * @since 2016/12/19 11:15
 */

public class AbsTitleActivity extends AbsSwipeBackActivity {
    private LinearLayout mRootView;
    private FrameLayout mContentView;
    private FrameLayout mTitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (useSystemCustomTitleBar()) {
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        mRootView = new LinearLayout(this);
        mRootView.setOrientation(LinearLayout.VERTICAL);
        super.setContentView(mRootView);
        mContentView = new FrameLayout(this);
        mRootView.addView(mTitleView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (!useSystemCustomTitleBar()) {
            mTitleView = new FrameLayout(this);
            mRootView.addView(mContentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    protected boolean useSystemCustomTitleBar() {
        return false;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (useSystemCustomTitleBar()) {
            super.setTitle(title);
        } else {
            TextView textView = getTitleTextView();
            if (textView != null) {
                textView.setText(title);
            }
        }

    }

    @Override
    public void setTitle(int titleId) {
        if (useSystemCustomTitleBar()) {
            super.setTitle(titleId);
        } else {
            setTitle(getString(titleId));
        }
    }

    protected TextView getTitleTextView() {
        return null;
    }

    protected void setTitleLayoutView(@LayoutRes int layoutResID) {
        if (useSystemCustomTitleBar()) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, layoutResID);
        } else {
            setTitleLayoutView(getLayoutInflater().inflate(layoutResID, null));
        }
    }

    protected void setTitleLayoutView(View view) {
        if (mTitleView != null) {
            if (mTitleView.getChildCount() > 0) {
                mTitleView.removeAllViews();
            }
            mTitleView.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mContentView != null) {
            if (mContentView.getChildCount() > 0) {
                mContentView.removeAllViews();
            }
            mContentView.addView(view, params);
        }
    }
}
