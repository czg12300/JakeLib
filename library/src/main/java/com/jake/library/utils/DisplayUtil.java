
package com.jake.library.utils;


import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.jake.library.BaseApplication;

/**
 * 描述：用于计算尺寸的工具
 * 
 * @author Created by jakechen on 2015/8/11.
 */
public class DisplayUtil {
    public static Point getSreenDimens() {
        Point point = new Point();
        point.set(getDisplayMetrics().widthPixels, getDisplayMetrics().heightPixels);
        return point;
    }

    public static float dip(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getDisplayMetrics());
    }

    public static int dip(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getDisplayMetrics());
    }

    public static DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    public static Resources getResources() {
        return BaseApplication.getInstance().getResources();
    }

}
