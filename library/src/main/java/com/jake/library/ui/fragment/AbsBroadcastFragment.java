package com.jake.library.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.text.TextUtils;

import com.jake.library.utils.LocalBroadcastUtils;

import java.util.ArrayList;

/**
 * 具有广播的fragment
 *
 * @author jake
 * @since 2017/5/8 上午10:49
 */

public class AbsBroadcastFragment extends BaseFragment {
    private BroadcastReceiver mReceiver;

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                LocalBroadcastUtils.registerReceiver(getActivity(), mReceiver, filter);
            } else {
                getActivity().registerReceiver(mReceiver, filter);
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
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            if (isUsingLocalBroadcast()) {
                LocalBroadcastUtils.unregisterReceiver(mReceiver);
                mReceiver = null;
            } else {
                getActivity().unregisterReceiver(mReceiver);
                mReceiver = null;
            }

        }
    }
}
