
package com.jake.library.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jake.library.R;
import com.jake.library.global.IController;
import com.jake.library.global.LibraryController;

/**
 * 描述：用于显示toast
 *
 * @author Created by jakechen on 2015/8/11.
 */
public class ToastUtil {
    protected static Toast mToast;

    private static final int MSG_SHOW = 0x001;

    private static Handler mMainHandle = new Handler(Looper.getMainLooper(),
            new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == MSG_SHOW && msg.obj != null) {
                        showToast((String) msg.obj);
                    }
                    return true;
                }
            });

    public static void show(int stringId) {
        show(LibraryController.getInstance().getContext().getString(stringId));
    }

    public static void show(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mMainHandle.sendMessage(mMainHandle.obtainMessage(MSG_SHOW, msg));
        } else {
            showToast(msg);
        }
    }

    private static void showToast(String msg) {
        if (mToast == null) {
            Context context = LibraryController.getInstance().getContext();
            mToast = new Toast(context);
            mToast.setView(View.inflate(context, R.layout.toast_layout, null));
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
}
