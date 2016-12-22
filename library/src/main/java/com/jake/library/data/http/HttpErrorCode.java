package com.jake.library.data.http;

/**
 * 描述：用于描述请求失败的code的意义
 *
 * @author jakechen
 * @since 2016/12/22 14:29
 */

public enum HttpErrorCode {
    NETWORK_UNAVAILABLE(-0x0100, "无可用网络")
    , RESPONSE_IS_NULL(-0x0101, "没有设置返回数据")
    , IO_EXCEPTION(-0x0105, "Io读取出错")
    , REQUEST_PACKAGE_IS_NULL(-0x0103, "没有请求参数")
    , JSON_PARSE_FAIL(-0x0104, "Json解析失败")
    , URL_IS_NOT_HTTP_URL(-0x0102, "url不是http或https的url");
    private String message;
    private int code;

    private HttpErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
