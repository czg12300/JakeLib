package com.jake.library.data.http;

import org.json.JSONException;

/**
 * 描述：数据请求返回
 *
 * @author jakechen
 * @since 2016/12/22 10:10
 */

public interface IResponse {
    void parser(String result) throws JSONException;
}
