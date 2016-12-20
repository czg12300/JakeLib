
package com.jake.library.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.jake.library.R;

/**
 * 描述:抽象底部弹窗的dialog
 *
 * @author jakechen
 * @since 2016/9/19 10:57
 */
public class AbsBottomDialog extends BaseDialog {
    private static final int DEFAULT_THEME = R.style.BottomDialogTheme;

    public AbsBottomDialog(Context context) {
        this(context, DEFAULT_THEME);
    }

    public AbsBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void configWindow(Context context, Window window) {
        super.configWindow(context, window);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
    }
}
