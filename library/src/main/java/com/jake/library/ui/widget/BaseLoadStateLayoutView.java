package com.jake.library.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 描述:拥有加载状态的layout view
 *
 * @author jakechen
 * @since 2016/8/10 15:58
 */
public abstract class BaseLoadStateLayoutView extends RelativeLayout {

    public BaseLoadStateLayoutView(Context context) {
        this(context, null);
    }

    public BaseLoadStateLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLoadStateLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView();
    }

    protected abstract void initView();

    /**
     * 显示加载失败的view
     */
    public abstract void showLoadFailView();

    /**
     * 显示加载成功的view
     */
    public abstract void showLoadSuccessView();

    /**
     * 显示加载中的view
     */
    public abstract void showLoadingView();

    /**
     * 显示页面为空的view
     */
    public abstract void showNoDataView();


    /**
     * 设置内容布局
     *
     * @param layoutId
     */
    public void setContentView(int layoutId) {
        setContentView(View.inflate(getContext(), layoutId, null));
    }

    public abstract void setContentView(View view);

    /**
     * 自定义空页面
     *
     * @param layoutId
     */
    public void setNoDataView(int layoutId) {
        setContentView(View.inflate(getContext(), layoutId, null));
    }

    public abstract void setNoDataView(View view);

    /**
     * 设置刷新按钮点击事件
     *
     * @param listener
     */
    public abstract void setOnRefreshButtonClickListener(OnClickListener listener);
}
