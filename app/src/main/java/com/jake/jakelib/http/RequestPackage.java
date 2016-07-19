package com.jake.jakelib.http;

import com.jake.library.http.BaseRequestPackage;
import com.jake.library.http.IJsonParse;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/7/19 14:53
 */

public class RequestPackage extends BaseRequestPackage{
    public RequestPackage(String cmd, Class<? extends IJsonParse> respClass) {
        super(cmd, respClass);
    }

    @Override
    protected String getCmdKey() {
        return "i";
    }
}
