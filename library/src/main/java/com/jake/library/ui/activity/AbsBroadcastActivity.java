package com.jake.library.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

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
        addActions(actions);
        if (actions.size() > 0) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    handleReceive(context, intent);
                }
            };
            IntentFilter filter = new IntentFilter();
            for (String action : actions) {
                if (!TextUtils.isEmpty(action)) {
                    filter.addAction(action);
                }
            }
            registerReceiver(mReceiver, filter);
        }
    }

    /**
     * 添加广播监听的action
     *
     * @param actions
     */
    protected void addActions(ArrayList<String> actions) {
    }

    /**
     * 处理广播接收到的事情
     *
     * @param context
     * @param intent
     */
    protected void handleReceive(Context context, Intent intent) {
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
