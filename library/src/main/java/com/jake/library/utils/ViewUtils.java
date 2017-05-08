
package com.jake.library.utils;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jake.library.global.LibraryController;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Date;

/**
 * 描述：view 的工具类 作者：jake on 2016/1/17 11:05
 */
public class ViewUtils {
    /**
     * 时间格式化：如2016-08-31  15:55:20
     */
    public static final String TIME_FORMAT_YMD_HMS = "yyyy-MM-dd  HH:mm:ss";
    public static final String TIME_FORMAT_COUNT_DOWN = "mm:ss:SS";
    public static final long COUNTDOWN_SPIT = 10;

    /**
     * 给TextView设置数据,view必须是TextView的子类
     *
     * @param tv
     * @param stringId
     */
    public static void setTextToView(View tv, int stringId) {
        if (tv != null) {
            if (tv instanceof TextView) {
                ((TextView) tv).setText(stringId);
            }
        }
    }

    public static void setTextToView(View tv, String text) {
        if (tv != null && text != null) {
            if (tv instanceof TextView) {
                ((TextView) tv).setText(text);
            }
        }
    }

    public static void setTextToView(View tv, CharSequence text) {
        if (tv != null && text != null) {
            if (tv instanceof TextView) {
                ((TextView) tv).setText(text);
            }
        }
    }

    /**
     * 创建特殊字体颜色的字符串
     *
     * @param color
     * @param charSequence
     * @return
     */
    public static CharSequence buildColorSpennableText(int color, CharSequence charSequence) {
        if (charSequence != null) {
            SpannableString ss = new SpannableString(charSequence);
            ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        }
        return null;

    }

    /**
     * 创建特殊字体大小的字符串
     *
     * @param textSize
     * @param charSequence
     * @return
     */
    public static CharSequence buildSizeSpennableText(float textSize, CharSequence charSequence) {
        if (charSequence != null) {
            SpannableString ss = new SpannableString(charSequence);
            ss.setSpan(new AbsoluteSizeSpan((int) textSize, true), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        }
        return null;

    }

    /**
     * 拼接不同的特殊字体
     *
     * @param css
     * @return
     */
    public static SpannableStringBuilder buildSpennableText(CharSequence... css) {
        if (css != null && css.length > 0) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            for (CharSequence charSequence : css) {
                if (charSequence != null) {
                    builder.append(charSequence);
                }
            }
            return builder;
        }
        return null;
    }

    /**
     * 从resource中获取多个string
     *
     * @param stringIds
     * @return
     */
    public static String getString(int... stringIds) {
        if (stringIds != null && stringIds.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stringIds.length; i++) {
                builder.append(LibraryController.get().getApplicationContext().getString(stringIds[i]));
            }
            return builder.toString();
        }
        return null;
    }

    /**
     * 解析时间
     *
     * @param format
     * @param time
     * @return
     */
    public static String formatTime(String format, long time) {
        return new java.text.SimpleDateFormat(format).format(new Date(time));
    }

    public static String formatCountdownTime(long timeSeconds) {
        StringBuilder result = new StringBuilder();
// 负数转为零
        timeSeconds = Math.max(timeSeconds, 0);
        // 各单位的秒数
        long mSpit = 60000;
        long sSpit = 1000;
        long SSpit = 1;
        // 计各单位的数值
        long m = timeSeconds / mSpit;
        long s = (timeSeconds % mSpit) / sSpit;
        long S = (timeSeconds % mSpit % sSpit) / SSpit;
        appendTimeWithZero(m, result);
        result.append(":");
        appendTimeWithZero(s, result);
        result.append(":");
        appendTimeWithZero(S, result);
        return result.toString();
    }

    private static void appendTimeWithZero(long time, StringBuilder builder) {
        if (time < 10) {
            builder.append("0" + time);
        } else {
            if (time < 100) {
                builder.append(time);
            } else {
                builder.append(String.valueOf(time).substring(0, 2));
            }
        }
    }

    /**
     * 解析时间，格式为：2016-08-31  15:55:20
     *
     * @param time
     * @return
     */
    public static String formatTimeYDMHMS(long time) {
        return formatTime(TIME_FORMAT_YMD_HMS, time);
    }

    /**
     * m:lorss
     * 关闭辅助功能，针对4.2.1和4.2.2 崩溃问题
     * java.lang.NullPointerException
     * at android.webkit.AccessibilityInjector$TextToSpeechWrapper$1.onInit(AccessibilityInjector.java:753)
     * ... ...
     * at android.webkit.CallbackProxy.handleMessage(CallbackProxy.java:321)
     */
    public static void disableWebViewAccessibility(@NonNull Context context) {
        if (context == null) {
            return;
        }
        Context appContext = context.getApplicationContext();
        if (Build.VERSION.SDK_INT == 17/*4.2 (Build.VERSION_CODES.JELLY_BEAN_MR1)*/) {
            try {
                AccessibilityManager am = (AccessibilityManager) appContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
                if (!am.isEnabled()) {
                    //Not need to disable accessibility
                    return;
                }
                Method setState = am.getClass().getDeclaredMethod("setState", int.class);
                setState.setAccessible(true);
                setState.invoke(am, 0);/**{@link AccessibilityManager#STATE_FLAG_ACCESSIBILITY_ENABLED}*/
            } catch (Exception ignored) {
            } catch (Error ignored) {

            }
        }
    }

    /**
     * fix 4.1系统加载网页时出现IllegalArgumentException
     *
     * @param webView
     * @param url
     */
    public static void disableJsIfUrlEncodedFailed(WebView webView, String url) {
        if (Build.VERSION.SDK_INT != 16) {
            return;
        }
        try {
            Class<?> uRLEncodedUtils = Class.forName("org.apache.http.client.utils.URLEncodedUtils");
            Method method = uRLEncodedUtils.getMethod("parse", new Class[]{URI.class, String.class});
            method.invoke(null, new Object[]{new URI(url), null});
            webView.getSettings().setJavaScriptEnabled(true);
        } catch (Exception e) {
            webView.getSettings().setJavaScriptEnabled(false);
        }
    }

    /**
     * 为view请求焦点
     *
     * @param view
     */
    public static void requestFocusForView(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public static void setText2TextView(TextView tv, CharSequence sequence) {
        if (tv != null && sequence != null) {
            tv.setText(sequence);
        }
    }

    /**
     * color转换成colorFilter
     *
     * @param color
     * @return
     */
    public static ColorFilter color2ColorFilter(int color) {
        int red = (color & 0xFF0000) / 0xFFFF;
        int green = (color & 0xFF00) / 0xFF;
        int blue = color & 0xFF;
        float[] matrix = {0, 0, 0, 0, red, 0, 0, 0, 0, green, 0, 0, 0, 0,
                blue, 0, 0, 0, 1, 0};
        return new ColorMatrixColorFilter(matrix);
    }

    public static void setImageViewDrawableColorFilter(int color, ImageView imageView) {
        if (imageView != null) {
            setDrawableColorFilter(color2ColorFilter(color), imageView.getDrawable());
        }
    }

    public static void setTextViewDrawableColorFilter(int color, TextView textView) {
        if (textView != null) {
            Drawable[] drawables = textView.getCompoundDrawables();
            if (drawables != null && drawables.length > 0) {
                for (Drawable drawable : drawables) {
                    setDrawableColorFilter(color2ColorFilter(color), drawable);
                }
            }
        }
    }

    public static void setTextViewLeftDrawableColorFilter(int color, TextView textView) {
        if (textView != null) {
            Drawable[] drawables = textView.getCompoundDrawables();
            if (drawables != null) {
                setDrawableColorFilter(color2ColorFilter(color), drawables[0]);
            }
        }
    }

    public static void setTextViewRightDrawableColorFilter(int color, TextView textView) {
        if (textView != null) {
            Drawable[] drawables = textView.getCompoundDrawables();
            if (drawables != null) {
                setDrawableColorFilter(color2ColorFilter(color), drawables[2]);
            }
        }
    }

    public static void setTextViewTopDrawableColorFilter(int color, TextView textView) {
        if (textView != null) {
            Drawable[] drawables = textView.getCompoundDrawables();
            if (drawables != null) {
                setDrawableColorFilter(color2ColorFilter(color), drawables[1]);
            }
        }
    }

    public static void setTextViewBottomDrawableColorFilter(int color, TextView textView) {
        if (textView != null) {
            Drawable[] drawables = textView.getCompoundDrawables();
            if (drawables != null) {
                setDrawableColorFilter(color2ColorFilter(color), drawables[3]);
            }
        }
    }

    public static void setDrawableColorFilter(int color, Drawable drawable) {
        setDrawableColorFilter(color2ColorFilter(color), drawable);
    }

    public static void setDrawableColorFilter(ColorFilter colorFilter, Drawable drawable) {
        if (colorFilter != null && drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
    }
}
