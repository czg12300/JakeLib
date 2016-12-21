
package com.jake.jakelib.http;

import com.jake.library.data.http.BaseMultiHttpRequest;
import com.jake.library.utils.MD5Utils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/7/19 14:53
 */

public class RequestTask extends BaseMultiHttpRequest {
    @Override
    protected String getServerUrl() {
        return "http://gcapi.sy.kugou.com/index.php?r=GameCenter/apiV2";
    }

    @Override
    protected ConcurrentHashMap<String, Object> getPublicParams() {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("platform", "1");
        map.put("clienttype", "2");
        map.put("userid", "0");
        map.put("clientversion", "1");
        map.put("gcClientVersion", "420");
        map.put("token", "");
        map.put("clientAppid", "1005");
        map.put("systemVersion", "22");
        String pTime = String.valueOf(System.currentTimeMillis()) + "";
        map.put("ptime", pTime);
        map.put("imei", "355009079507556");
        map.put("mid", "88147860145912326438488853528637666168");
        map.put("uuid", "c24d96f50beb4fac86f1803305974832");
        map.put("resolution", "1080*1920");
        map.put("nettype", "1");
        map.put("model", "SM-A7100");
        map.put("spid", "4");
        map.put("sysversion", "5.1.1");
        map.put("csign",
                MD5Utils.getMd5(
                        "1" + "0" + pTime + getPrivateParams().toString() + getSecurityKeyForSign())
                        .toLowerCase());
        return map;
    }

    /**
     * 获取sign校验字段用到的密钥
     *
     * @return
     */
    public static String getSecurityKeyForSign() {
        byte[] data = MD5Utils
                .hexStringToByte("2D197D7B685F59187F0E6A2D2D180C7D1A5D23697A7E182D2F68087D6D5A2B13");
        unEncrypt(data);
        return new String(data);
    }

    public static void unEncrypt(byte[] source) {
        if (source != null && source.length > 0) {
            int length = source.length;
            byte[] encrypt_key = MD5Utils.hexStringToByte("1A2B3C4D5E6F");
            int keyLen = encrypt_key.length;
            for (int i = 0; i < length; i++) {
                source[i] ^= encrypt_key[i % keyLen];
            }
        }
    }

    @Override
    protected String getPrivateParamsKey() {
        return "jsonparam";
    }

    @Override
    protected String handleHttpOnFailure(IOException e) {
        return e.getMessage();
    }
}
