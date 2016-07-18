
package com.jake.library.http;

/**
 * 描述：http请求的基类
 *
 * @author jakechen
 * @since 2016/7/18 17:29
 */

public class BaseHttpRequest {
 private    RequestPackage[] mRequestPackages;
    public BaseHttpRequest(RequestPackage... requestPackages) {
        this.mRequestPackages = requestPackages;
    }

    public void request() {

    }
}
