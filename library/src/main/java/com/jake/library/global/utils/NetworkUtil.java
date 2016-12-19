
package com.jake.library.global.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 描述：网络相关的工具
 *
 * @author jakechen
 * @since 2016/5/25 11:19
 */
public class NetworkUtil {
    public static boolean isHttpUrl(String url) {
        return (null != url) && (url.length() > 6)
                && url.substring(0, 7).equalsIgnoreCase("http://");
    }

    public static boolean isHttpsUrl(String url) {
        return (null != url) && (url.length() > 7)
                && url.substring(0, 8).equalsIgnoreCase("https://");
    }

    public static boolean isWebUrl(String url) {
        return isHttpsUrl(url) || isHttpUrl(url);
    }

    /**
     * 描述:网络类型
     *
     * @author chenys
     * @since 2013-7-22 上午11:40:42
     */
    public static class NetworkType {

        public static final String UNKNOWN = "unknown";

        public static final String NET_2G = "2G";

        public static final String NET_3G = "3G";

        public static final String WIFI = "wifi";

        public static final String NET_CMNET = "cmnet";

        public static final String NET_CMWAP = "cmwap";
    }

    /**
     * 当前是否有可用网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取当前的网络类型
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        try {
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
        }
        if (netInfo == null) {
            return NetworkType.UNKNOWN;
        }
        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WIFI;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int netType = tm.getNetworkType();

        if (netType == TelephonyManager.NETWORK_TYPE_GPRS
                || netType == TelephonyManager.NETWORK_TYPE_EDGE
                || netType == TelephonyManager.NETWORK_TYPE_CDMA
                || netType == TelephonyManager.NETWORK_TYPE_1xRTT || netType == 11) {
            return NetworkType.NET_2G;
        }
        return NetworkType.NET_3G;
    }

    /**
     * 是否cmwap
     *
     * @param context
     * @return
     */
    public static boolean isCmwap(Context context) {
        String currentNetworkType = getNetworkType(context);

        if (NetworkType.NET_2G.equals(currentNetworkType)) {
            try {
                ConnectivityManager connectMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectMgr != null) {
                    NetworkInfo mobNetInfo = connectMgr
                            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mobNetInfo != null && mobNetInfo.isConnected()) {
                        if ("cmwap".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        return false;
    }
}
