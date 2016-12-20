package com.jake.library.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ResizeLayoutView extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    private Activity activity;
    private int[] mInsets = new int[4];
    private boolean mIsInputSoftShow = false;
    private boolean isWindowTranslucentStatus = false;

    public ResizeLayoutView(Context context) {
        this(context, null);
    }

    public ResizeLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ResizeLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity = ((Activity) context);
        mChildOfContent = activity.findViewById(android.R.id.content);
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.windowTranslucentStatus, typedValue, true)) {
            //data=0表示false  data=-1表示true
            isWindowTranslucentStatus = (typedValue.data == -1);
        }
        if (isWindowTranslucentStatus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setFitsSystemWindows(true);
        }
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (isWindowTranslucentStatus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;

            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }

        return super.fitSystemWindows(insets);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mChildOfContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    private View mChildOfContent;

    private int usableHeightPrevious;

    private int height = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (height == 0) {
            height = getMeasuredHeight();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public boolean isInputSoftShow() {
        return mIsInputSoftShow;
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightPrevious == 0) {
            usableHeightPrevious = usableHeightNow;
            return;
        }
        if (usableHeightNow != usableHeightPrevious) {
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point windowSize = new Point();
            windowManager.getDefaultDisplay().getSize(windowSize);
//            int usableHeightSansKeyboard = windowSize.y;// mChildOfContent.getRootView().getHeight();
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (!isWindowTranslucentStatus || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || getVirtualBarHeight() > 0) {
                heightDifference = heightDifference - getStatusBarHeight();
            }
//            heightDifference = heightDifference - getVirtualBarHeigh();
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                mIsInputSoftShow = true;
                if (onResizeListener != null) {
                    onResizeListener.onKeyboardShow(heightDifference);
                }
            } else {
                mIsInputSoftShow = false;
                if (onResizeListener != null) {
                    onResizeListener.onKeyboardHide();
                }
            }
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 获取虚拟功能键高度
     */
    public int getVirtualBarHeight() {
        int vh = 0;
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    private int getStatusBarHeight() {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    @Override
    public void onGlobalLayout() {
        possiblyResizeChildOfContent();
    }

    private OnResizeListener onResizeListener;

    public void setOnResizeListener(OnResizeListener onResizeListener) {
        this.onResizeListener = onResizeListener;
    }

    public static interface OnResizeListener {

        /**
         * 软键盘关闭
         */
        void onKeyboardHide();

        /**
         * 软键盘高度改变
         */
        void onKeyboardShow(int height);
    }
}
