package com.jake.library.ui.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.jake.library.R;

/**
 * 描述：抽象pop的dialog
 *
 * @author jakechen
 * @since 2016/12/20 15:02
 */

public class AbsPopDialog extends BaseDialog {

    public AbsPopDialog(Context context) {
        super(context, R.style.PopDialogTheme);
    }

    public AbsPopDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void configWindow(Context context, Window window) {
        super.configWindow(context, window);
        int horizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, context.getResources().getDisplayMetrics());
        window.getDecorView().setPadding(horizontalPadding, 0, horizontalPadding, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

}
