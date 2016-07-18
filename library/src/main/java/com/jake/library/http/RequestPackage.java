
package com.jake.library.http;

import java.util.Hashtable;

/**
 * 描述：请求包
 *
 * @author jakechen
 * @since 2016/7/18 17:35
 */

public class RequestPackage {

    private Class<? extends IJsonParse> mRespClass;

    private Hashtable<String, Object> mParams;

    public RequestPackage(Class<? extends IJsonParse> respClass) {
        if (respClass == null) {
            throw new NullPointerException("返回数据类型不能为空");
        }
        this.mRespClass = respClass;
        mParams = new Hashtable<>();
    }

    public void addParam(String key, Object value) {
        mParams.put(key, value);
    }

    public void addParams(Hashtable<String, Object> params) {
        if (params != null && params.size() > 0) {
            this.mParams.putAll(params);
        }
    }

    public void setParams(Hashtable<String, Object> params) {
        if (params != null && params.size() > 0) {
            this.mParams = params;
        }
    }

    public void clear() {
        mParams.clear();
    }

    public Object remove(String key) {
        return mParams.remove(key);
    }
}
