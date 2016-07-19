
package com.jake.library.http;

import org.json.JSONObject;

/**
 * 描述：json解析接口
 *
 * @author jakechen
 * @since 2016/7/18 17:31
 */

public interface IJsonParse {
    Object parseJson(JSONObject root);
}
