package com.jake.library.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.jake.library.global.LibraryController;

/**
 * 本地广播工具
 *
 * @author jake
 * @since 2017/5/8 上午10:17
 */

public class LocalBroadcastUtils {
    private static Context mAppContext;

    static {
        if (LibraryController.get().isInstall()) {
            mAppContext = LibraryController.get().getApplicationContext();
        }
    }

    private static void checkAndAddAppContext(Context context) {
        if (mAppContext == null) {
            if (context != null) {
                mAppContext = context.getApplicationContext();
            }
        }
    }

    public static boolean hasAppContext() {
        return mAppContext != null;
    }

    public static void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        registerReceiver(null, receiver, filter);
    }

    public static void registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter filter) {
        checkAndAddAppContext(context);
        if (hasAppContext()) {
            LocalBroadcastManager.getInstance(mAppContext).registerReceiver(receiver, filter);
        }
    }

    public static void unregisterReceiver(BroadcastReceiver receiver) {
        unregisterReceiver(null, receiver);
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        checkAndAddAppContext(context);
        if (hasAppContext()) {
            LocalBroadcastManager.getInstance(mAppContext).unregisterReceiver(receiver);
        }
    }

    public static void sendBroadcast(Intent intent) {
        sendBroadcast(null, intent);
    }

    public static void sendBroadcast(Context context, Intent intent) {
        checkAndAddAppContext(context);
        if (hasAppContext()) {
            LocalBroadcastManager.getInstance(mAppContext).sendBroadcast(intent);
        }
    }

    public static void sendBroadcast(String action) {
        sendBroadcast(null, action);
    }

    public static void sendBroadcast(Context context, String action) {
        checkAndAddAppContext(context);
        if (hasAppContext() && !TextUtils.isEmpty(action)) {
            LocalBroadcastManager.getInstance(mAppContext).sendBroadcast(new Intent(action));
        }
    }

    public static void sendBroadcastSync(Intent intent) {
        sendBroadcastSync(null, intent);
    }

    public static void sendBroadcastSync(Context context, Intent intent) {
        checkAndAddAppContext(context);
        if (hasAppContext()) {
            LocalBroadcastManager.getInstance(mAppContext).sendBroadcastSync(intent);
        }
    }

    public static void sendBroadcastSync(String action) {
        sendBroadcastSync(null, action);
    }

    public static void sendBroadcastSync(Context context, String action) {
        checkAndAddAppContext(context);
        if (hasAppContext() && !TextUtils.isEmpty(action)) {
            LocalBroadcastManager.getInstance(mAppContext).sendBroadcastSync(new Intent(action));
        }
    }
}
