
package com.jake.library.data.http;

import android.text.TextUtils;

import com.jake.library.global.LibraryController;
import com.jake.library.utils.LogUtils;
import com.jake.library.utils.NetworkUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：http网络数据请求
 *
 * @author jakechen
 * @since 2016/5/24 11:37
 */
public abstract class BaseSingleHttpRequest {
    private static final String TAG_REQUEST = "request_general";

    private static final String TAG_RESPONSE = "response_general";

    private String mUrl;

    private boolean mIsPost = false;

    private boolean mIsCancel = false;

    private Class<? extends IJsonParse> mResponseClass;

    private ConcurrentHashMap<String, Object> mParams = new ConcurrentHashMap<>();

    private BaseSingleHttpRequest(Class<? extends IJsonParse> responseClass, String url,
                                  boolean isPost) {
        mUrl = url;
        mIsPost = isPost;
        mResponseClass = responseClass;
        setupPublicParams(mParams);
    }

    private void appendGetParams(Request.Builder reqBuilder) throws UnsupportedEncodingException {
        reqBuilder.get();
        String server = parseUrl();
        StringBuilder urlBuilder = new StringBuilder(server);
        if (TextUtils.isEmpty(server)) {
            return;
        }
        if (server.contains("?")) {
            urlBuilder.append("&");
        } else {
            urlBuilder.append("?");
        }
        for (String key : mParams.keySet()) {
            urlBuilder.append(key).append("=").append(NetworkUtils.encode(mParams.get(key) + ""))
                    .append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        String url = urlBuilder.toString();
        LogUtils.d(TAG_REQUEST, "Get :" + url);
        reqBuilder.url(url);
    }

    private String parseUrl() {
        if (!TextUtils.isEmpty(mUrl)) {
            if (NetworkUtils.isHttpUrl(mUrl) || NetworkUtils.isHttpsUrl(mUrl)) {
                return mUrl;
            } else {
                return getServerUrl() + mUrl;
            }
        }
        return "";
    }

    protected abstract void setupPublicParams(ConcurrentHashMap<String, Object> publicParams);

    protected abstract String getServerUrl();

    private void appendPostParams(Request.Builder reqBuilder) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : mParams.keySet()) {
            builder.addEncoded(key, mParams.get(key) + "");
        }
        String url = parseUrl();
        LogUtils.d(TAG_REQUEST, "Post :" + url);
        reqBuilder.url(url);
        reqBuilder.post(builder.build());
    }

    public Object removeParam(String name) {
        return mParams.remove(name);
    }

    public void clearParams() {
        mParams.clear();
    }

    public void addParam(String name, String value) {
        mParams.put(name, value);
    }

    public void addParams(Hashtable<String, String> params) {
        mParams.putAll(params);
    }

    protected void noNetwork() {
    }

    public Object request() {
        if (!NetworkUtils.isNetworkAvailable(LibraryController.getInstance().getContext())) {
            noNetwork();
            return null;
        }
        Request.Builder reqBuilder = new Request.Builder();
        try {
            if (mIsPost) {
                appendPostParams(reqBuilder);
            } else {
                appendGetParams(reqBuilder);
            }
            Response response = OkHttpClientManager.getInstance().getOkHttpClient()
                    .newCall(reqBuilder.build()).execute();
            if (response.isSuccessful() && !mIsCancel) {
                IJsonParse baseResponse = mResponseClass.newInstance();
                String result = response.body().string();
                LogUtils.d(TAG_RESPONSE, result);
                if (baseResponse.parseJson(result)) {
                    return baseResponse;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cancel() {
        mIsCancel = true;
    }

}
