package com.jake.library.data.http;

import android.content.Context;

import com.jake.library.global.LibraryController;
import com.jake.library.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * 描述：http 数据提取器
 *
 * @author jakechen
 * @since 2016/12/22 10:03
 */

public abstract class HttpDataFetch {
    protected ExecutorService executorService;
    private HttpEngine.Method method;
    private HttpRequestCallback callback;
    private RequestPackage requestPackage;
    private IResponse response;
    private Context mAppContext;

    public HttpDataFetch(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public HttpDataFetch setResponse(IResponse response) {
        this.response = response;
        return this;
    }

    protected HttpDataFetch setMethod(HttpEngine.Method method) {
        this.method = method;
        return this;
    }


    public HttpDataFetch setCallback(HttpRequestCallback callback) {
        this.callback = callback;
        return this;
    }

    public HttpDataFetch setRequestPackage(RequestPackage requestPackage) {
        this.requestPackage = requestPackage;
        return this;
    }

    public void setContext(Context context) {
        if (context != null) {
            mAppContext = context.getApplicationContext();
        }
    }

    public void request() {
        if (!NetworkUtils.isNetworkAvailable(mAppContext)) {
            if (callback != null) {
                callback.onFail(HttpErrorCode.NETWORK_UNAVAILABLE.getCode(), HttpErrorCode.NETWORK_UNAVAILABLE.getMessage());
            }
            return;
        }
        if (requestPackage != null) {
            if (NetworkUtils.isHttpOrHttpsUrl(requestPackage.getUrl())) {
                switch (method) {
                    case GET:
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    requestByGet(requestPackage, response, callback);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if (callback != null) {
                                        callback.onFail(HttpErrorCode.IO_EXCEPTION.getCode(), HttpErrorCode.IO_EXCEPTION.getMessage());
                                    }
                                }
                            }
                        });
                        break;
                    case POST:
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    requestByPost(requestPackage, response, callback);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if (callback != null) {
                                        callback.onFail(HttpErrorCode.IO_EXCEPTION.getCode(), HttpErrorCode.IO_EXCEPTION.getMessage());
                                    }
                                }
                            }
                        });
                        break;
                }

            } else {
                if (callback != null) {
                    callback.onFail(HttpErrorCode.URL_IS_NOT_HTTP_URL.getCode(), HttpErrorCode.URL_IS_NOT_HTTP_URL.getMessage());
                }
            }
        } else {
            if (callback != null) {
                callback.onFail(HttpErrorCode.REQUEST_PACKAGE_IS_NULL.getCode(), HttpErrorCode.REQUEST_PACKAGE_IS_NULL.getMessage());
            }
        }
    }

    protected void handleResponseResult(IResponse response, HttpRequestCallback callback, String resultData) {
        if (response != null) {
            try {
                response.parser(resultData);
                if (callback != null) {
                    callback.onSuccess(response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onFail(HttpErrorCode.JSON_PARSE_FAIL.getCode(), HttpErrorCode.JSON_PARSE_FAIL.getMessage());
                }
            }
        } else {
            if (callback != null) {
                callback.onFail(HttpErrorCode.RESPONSE_IS_NULL.getCode(), HttpErrorCode.RESPONSE_IS_NULL.getMessage());
            }
        }
    }

    protected abstract void requestByPost(RequestPackage requestPackage, IResponse response, HttpRequestCallback callback) throws IOException;

    protected abstract void requestByGet(RequestPackage requestPackage, IResponse response, HttpRequestCallback callback) throws IOException;


}
