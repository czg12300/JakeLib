
package com.jake.library.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 描述:程序统一使用一个http client
 *
 * @author jakechen
 * @since 2016/7/29
 */
public class OkHttpClientManager {
    private OkHttpClientManager() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(10, TimeUnit.SECONDS);
        okBuilder.readTimeout(10, TimeUnit.SECONDS);
        okBuilder.writeTimeout(10, TimeUnit.SECONDS);
        mClient = okBuilder.build();
    }

    private static OkHttpClientManager mInstance;

    public synchronized static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpClientManager();
        }
        return mInstance;
    }

    private OkHttpClient mClient;

    public OkHttpClient getOkHttpClient() {
        return mClient;
    }

}
