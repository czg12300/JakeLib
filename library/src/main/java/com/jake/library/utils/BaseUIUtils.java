package com.jake.library.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述:ui的工具
 *
 * @author jakechen
 * @since 2016/9/13 18:57
 */
public class BaseUIUtils {

    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * 在主线程执行
     *
     * @param executor
     */
    public static void executeInUiThread(final UiExecutor executor) {
        if (isInUiThread()) {
            executor.execute();
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    executor.execute();
                }
            });
        }
    }

    public static void postDelayed(Runnable runnable, long delayed) {
        mHandler.postDelayed(runnable, delayed);
    }


    /**
     * 判断是否可以显示dialog,仅限与context的类型为Activity，也就是前台弹窗
     *
     * @param context
     * @return
     */
    public static boolean canShowDialogOnlyActivity(Context context) {
        if (context != null && context instanceof Activity) {
            Activity activity = (Activity) context;
            return activity != null && !activity.isFinishing();
        }
        return false;
    }

    /**
     * 判断是否可以显示dialog,包括context的类型为application，
     *
     * @param context
     * @return
     */
    public static boolean canShowDialog(Context context) {
        boolean canShow = false;
        if (context != null) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                canShow = !activity.isFinishing();
            } else {
                canShow = true;
            }
        }
        return canShow;
    }

    /**
     * 用于启动Activity的时候，判断当前的context是否可以启动Activity到同一个ActivityTask任务站里面
     *
     * @param context
     * @return
     */
    public static boolean canStartActivityToSameTask(Context context) {
        return context != null && context instanceof Activity;
    }

    /**
     * 是否在主线程
     *
     * @return
     */
    public static boolean isInUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Context context) {
        if (context instanceof Activity) {
            InputMethodManager manager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
            if (((Activity) context).getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } else {
            LogUtil.e("hide soft input is fail, context is not instance of activity");
        }

    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(final Context context) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (context != null && context instanceof Activity) {
                    InputMethodManager manager = (InputMethodManager) ((Activity) context).getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    boolean isOpen = manager.isActive();// isOpen若返回true，则表示输入法打开
                    if (isOpen) {
                        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } else {
                    LogUtil.e("show soft input is fail, context is not instance of activity");
                }
            }

        }, 500);

    }

    /**
     * 同步消息执行器
     */
    public static interface UiExecutor {
        /**
         * 执行
         */
        void execute();
    }
}