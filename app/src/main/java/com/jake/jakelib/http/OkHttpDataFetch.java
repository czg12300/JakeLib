package com.jake.jakelib.http;

import com.jake.library.data.http.HttpDataFetch;
import com.jake.library.data.http.HttpRequestCallback;
import com.jake.library.data.http.IResponse;
import com.jake.library.data.http.RequestPackage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/12/22 17:58
 */

public class OkHttpDataFetch extends HttpDataFetch {
    public OkHttpDataFetch(ExecutorService executorService) {
        super(executorService);
    }

    @Override
    protected void requestByPost(RequestPackage requestPackage, IResponse response, HttpRequestCallback callback) throws IOException {

    }

    @Override
    protected void requestByGet(RequestPackage requestPackage, IResponse response, HttpRequestCallback callback) throws IOException {

    }
}
