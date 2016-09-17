package com.jake.library.ui;

import android.app.Dialog;
import android.content.Context;

/**
 * 描述：所以弹窗的基类
 *
 * @author jakechen
 * @since 2016/9/17 12:51
 */

public class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
