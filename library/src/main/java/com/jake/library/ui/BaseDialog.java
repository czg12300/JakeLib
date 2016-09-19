
package com.jake.library.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

/**
 * 描述：所以弹窗的基类
 *
 * @author jakechen
 * @since 2016/9/17 12:51
 */

public class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context);
        configWindow(context, getWindow());
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        configWindow(context, getWindow());
    }

    /**
     * 是否由后台创建
     *
     * @param context
     * @return
     */
    private boolean isCreatedByBack(Context context) {
        return !(context instanceof Activity);
    }

    /**
     * 配置window的样式,子类重写一定要调用super方法
     *
     * @param context
     * @param window
     */
    protected void configWindow(Context context, Window window) {
        if (isCreatedByBack(context)) {
            this.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        }
    }
}
