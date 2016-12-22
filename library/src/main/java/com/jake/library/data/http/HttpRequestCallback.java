package com.jake.library.data.http;

/**
 * 描述：数据请求的返回接口
 *
 * @author jakechen
 * @since 2016/12/22 10:05
 */

public interface HttpRequestCallback<Response extends IResponse> {
    void onSuccess(Response response);

    void onFail(int code, String msg);
}
