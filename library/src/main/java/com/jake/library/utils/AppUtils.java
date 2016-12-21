
package com.jake.library.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jakechen on 2015/8/11.
 */
public class AppUtils {

    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 同步消息执行器
     */
    public static interface UiExecutor {
        /**
         * 执行
         */
        void execute();
    }

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

    public static void adjustStatusBar(View view, Context context) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(0, getStatusBarHeight(context), 0, 0);
        }
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25,
                context.getResources().getDisplayMetrics());
    }

    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }

    public static void convertActivityToTranslucent(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains(
                        "TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            if (Build.VERSION.SDK_INT < 21) {
                Method method = Activity.class.getDeclaredMethod(
                        "convertToTranslucent",
                        translucentConversionListenerClazz);
                method.setAccessible(true);
                method.invoke(activity, new Object[]{null});
            } else {
                Method method = Activity.class.getDeclaredMethod(
                        "convertToTranslucent",
                        translucentConversionListenerClazz,
                        ActivityOptions.class);
                method.setAccessible(true);
                method.invoke(activity, new Object[]{null, null});
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
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
            LogUtils.e("hide soft input is fail, context is not instance of activity");
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
                    LogUtils.e("show soft input is fail, context is not instance of activity");
                }
            }

        }, 500);

    }


    /**
     * 获取版本号
     *
     * @return
     */
    public static int getAppVersionCode(Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info != null) {
            return info.versionCode;
        }
        return 1;
    }

    public static String getAppVersionName(Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info != null) {
            return info.versionName;
        }
        return "1.0";
    }

    public static String getPackageName(Context context) {
        if (context != null) {
            return context.getPackageName();
        }
        return null;
    }

    public static PackageInfo getPackageInfo(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bundle getApplicationMetaData(Context context) {
        ApplicationInfo info = null;
        if (context != null) {
            try {
                info = context.getPackageManager().getApplicationInfo(getPackageName(context), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return info != null ? info.metaData : null;
    }


    public static boolean isListAvailable(List<?> list) {
        return list != null && list.size() > 0;
    }

    public static boolean isNotNull(Object object) {
        return object != null;
    }

    public static boolean isSameObject(Object obj1, Object obj2) {
        return obj1 == obj2;
    }
}
