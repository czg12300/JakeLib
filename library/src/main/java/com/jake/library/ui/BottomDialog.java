
package com.jake.library.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.jake.library.R;

/**
 * 描述:
 *
 * @author jakechen
 * @since 2016/9/19 10:57
 */
public class BottomDialog extends BaseDialog {
    private static final int DEFAULT_THEME = R.style.BottomDialogTheme;

    public BottomDialog(Context context) {
        this(context, DEFAULT_THEME);
    }

    public BottomDialog(Context context, int themeResId) {
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
