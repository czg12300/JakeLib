
package com.jake.library.utils;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.widget.TextView;

/**
 * 描述：view 的工具类 作者：jake on 2016/1/17 11:05
 */
public final class ViewUtil {
    private ViewUtil() {
    }

    ;

    public static void setText2TextView(TextView tv, CharSequence sequence) {
        if (tv != null && sequence != null) {
            tv.setText(sequence);
        }
    }

    public static ColorFilter color2ColorFilter(int color) {
        int red = (color & 0xFF0000) / 0xFFFF;
        int green = (color & 0xFF00) / 0xFF;
        int blue = color & 0xFF;

        float[] matrix = {
                0, 0, 0, 0, red, 0, 0, 0, 0, green, 0, 0, 0, 0, blue, 0, 0, 0, 1, 0
        };

        return new ColorMatrixColorFilter(matrix);
    }

}
