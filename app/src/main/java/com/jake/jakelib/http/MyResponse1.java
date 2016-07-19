package com.jake.jakelib.http;

import com.jake.library.http.IJsonParse;

import org.json.JSONObject;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/7/19 15:12
 */

public class MyResponse1 implements IJsonParse{
    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public Object parseJson(JSONObject root) {
        setJson(root!=null?root.toString():"请求失败");
        return this;
    }
}
