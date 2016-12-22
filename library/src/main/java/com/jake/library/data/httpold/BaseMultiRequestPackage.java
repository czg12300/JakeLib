
package com.jake.library.data.httpold;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：请求包
 *
 * @author jakechen
 * @since 2016/7/18 17:35
 */

public abstract class BaseMultiRequestPackage {

    private Class<? extends IMultiJsonParse> mRespClass;

    private ConcurrentHashMap<String, Object> mParams;

    private String mCmd;

    public BaseMultiRequestPackage(String cmd, Class<? extends IMultiJsonParse> respClass) {
        if (TextUtils.isEmpty(cmd)) {
            throw new NullPointerException("接口名不能为空");
        }
        if (respClass == null) {
            throw new NullPointerException("返回数据类型不能为空");
        }
        this.mCmd = cmd;
        this.mRespClass = respClass;
        mParams = new ConcurrentHashMap<>();
    }

    public void addParam(String key, Object value) {
        mParams.put(key, value);
    }

    public void addParams(Hashtable<String, Object> params) {
        if (params != null && params.size() > 0) {
            this.mParams.putAll(params);
        }
    }

    public void setParams(ConcurrentHashMap<String, Object> params) {
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

    public JSONObject getParamsJson() {
        try {
            JSONObject object = new JSONObject();
            if (!TextUtils.isEmpty(getCmdKey())) {
                object.putOpt(getCmdKey(), mCmd);
            }
            if (mParams != null && mParams.size() > 0) {
                for (String key : mParams.keySet()) {
                    object.putOpt(key, mParams.get(key));
                }
            }
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IMultiJsonParse getJsonParse() {
        try {
            return mRespClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String markKey() {
        return mRespClass != null ? mRespClass.getSimpleName() : null;
    }

    protected abstract String getCmdKey();

    protected String getCmdValue() {
        return mCmd;
    }
}
