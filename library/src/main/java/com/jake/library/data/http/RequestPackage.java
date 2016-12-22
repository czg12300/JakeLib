package com.jake.library.data.http;

import java.io.File;
import java.util.HashMap;

/**
 * 描述：请求类
 *
 * @author jakechen
 * @since 2016/12/22 9:54
 */

public class RequestPackage {
    private File[] files;
    private HashMap<String, Object> params;
    private String url;
    private HashMap<String, String> headers;

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> header) {
        this.headers = header;
    }

    public void addHeader(String key, String value) {
        checkHeadersIsNotNull();
        headers.put(key, value);
    }

    public void addHeaders(HashMap<String, String> headers) {
        checkHeadersIsNotNull();
        if (headers != null) {
            this.headers.putAll(headers);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void addParams(String key, Object value) {
        checkParamsIsNotNull();
        params.put(key, value);
    }

    public void addParams(HashMap<String, Object> params) {
        checkParamsIsNotNull();
        if (params != null) {
            this.params.putAll(params);
        }
    }

    private void checkParamsIsNotNull() {
        if (params == null) {
            params = new HashMap<>();
        }
    }

    private void checkHeadersIsNotNull() {
        if (headers == null) {
            headers = new HashMap<>();
        }
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
}
