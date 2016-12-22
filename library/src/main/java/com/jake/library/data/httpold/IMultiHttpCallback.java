
package com.jake.library.data.httpold;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：http请求结果返回
 *
 * @author jakechen
 * @since 2016/7/19 15:19
 */

public interface IMultiHttpCallback {
    public void onFailure(String exception);

    public void onResponse(ConcurrentHashMap<String, Object> responseMap);
}
