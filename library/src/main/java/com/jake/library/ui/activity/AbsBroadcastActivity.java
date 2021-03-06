package com.jake.library.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jake.library.utils.LocalBroadcastUtils;

import java.util.ArrayList;

/**
 * 描述：具有广播接收器的activity,子类重写需要调用父类的onCreate和onDestroy方法
 *
 * @author jakechen
 * @since 2016/12/19 11:00
 */

public class AbsBroadcastActivity extends BaseActivity {
    private BroadcastReceiver mReceiver;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> actions = new ArrayList<>();
        setupBroadcastActions(actions);
        if (actions.size() > 0) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    handleReceive(context, intent.getAction(), intent);
                }
            };
            IntentFilter filter = new IntentFilter();
            for (String action : actions) {
                if (!TextUtils.isEmpty(action)) {
                    filter.addAction(action);
                }
            }
            if (isUsingLocalBroadcast()) {
                LocalBroadcastUtils.registerReceiver(getApplicationContext(), mReceiver, filter);
            } else {
                registerReceiver(mReceiver, filter);
            }
        }
    }

    protected boolean isUsingLocalBroadcast() {
        return true;
    }

    /**
     * 添加广播监听的action
     *
     * @param actions
     */
    protected void setupBroadcastActions(ArrayList<String> actions) {
    }


    /**
     * 处理广播接收到的事情
     *
     * @param context
     * @param action
     * @param intent
     */
    protected void handleReceive(Context context, String action, Intent intent) {
    }


    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            if (isUsingLocalBroadcast()) {
                LocalBroadcastUtils.unregisterReceiver(mReceiver);
                mReceiver = null;
            } else {
                unregisterReceiver(mReceiver);
                mReceiver = null;
            }

        }
    }
}
