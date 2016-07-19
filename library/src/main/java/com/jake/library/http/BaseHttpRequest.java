
package com.jake.library.http;

import android.text.TextUtils;

import com.jake.library.BaseApplication;
import com.jake.library.utils.LogUtil;
import com.jake.library.utils.NetworkUtil;
import com.jake.library.utils.UrlEncodeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：http请求的基类
 *
 * @author jakechen
 * @since 2016/7/18 17:29
 */

public abstract class BaseHttpRequest {
    private static final String TAG_REQUEST = "request_general";

    private static final String TAG_RESPONSE = "response_general";

    private OkHttpClient mClient;

    private boolean mIsPost = false;

    private boolean mIsCancel = false;

    private BaseRequestPackage[] mBaseRequestPackages;

    public BaseHttpRequest() {
        this(false);
    }

    public BaseHttpRequest(boolean isPost) {
        mIsPost = isPost;
        mClient = new OkHttpClient();
    }

    private void appendGetParams(Request.Builder reqBuilder) throws UnsupportedEncodingException {
        reqBuilder.get();
        String server = getServerUrl();
        if (TextUtils.isEmpty(server)) {
            return;
        }
        StringBuilder urlBuilder = new StringBuilder(server);
        if (server.contains("?")) {
            urlBuilder.append("&");
        } else {
            urlBuilder.append("?");
        }
        ConcurrentHashMap<String, Object> publicParams = getPublicParams();
        if (publicParams != null && publicParams.size() > 0) {
            for (String key : publicParams.keySet()) {
                urlBuilder.append(key).append("=")
                        .append(UrlEncodeUtil.encode(publicParams.get(key) + "")).append("&");
            }

        }
        String privateParamsKey = getPrivateParamsKey();
        if (!TextUtils.isEmpty(privateParamsKey) && mBaseRequestPackages != null
                && mBaseRequestPackages.length > 0) {
            urlBuilder.append(privateParamsKey).append("=");
            JSONArray array = getPrivateParams();
            if (array.length() > 0) {
                urlBuilder.append(UrlEncodeUtil.encode(array.toString()));
            }
        } else {
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        String url = urlBuilder.toString();
        // url =
        // "http://gcapi.sy.kugou.com/index.php?r=GameCenter/apiV2&platform=1&clienttype=2&userid=0&clientversion=1&gcClientVersion=420&token=&clientAppid=1005&systemVersion=22&ptime=1468911667267&imei=355009079507556&mid=88147860145912326438488853528637666168&uuid=c24d96f50beb4fac86f1803305974832&resolution=1080*1920&nettype=1&model=SM-A7100&spid=4&sysversion=5.1.1&jsonparam=%5B%7B%22type%22%3A%221%22%2C%22i%22%3A46%2C%22itemtypeid%22%3A138%7D%5D&csign=5563efdad54f5ca36ef9fcbfece1a23f";
        LogUtil.d(TAG_REQUEST, "Get :" + url);
        reqBuilder.url(url);
    }

    protected JSONArray getPrivateParams() {
        if (mBaseRequestPackages != null && mBaseRequestPackages.length > 0) {
            JSONArray array = new JSONArray();
            for (BaseRequestPackage baseRequestPackage : mBaseRequestPackages) {
                if (baseRequestPackage != null && baseRequestPackage.getParamsJson() != null) {
                    array.put(baseRequestPackage.getParamsJson());
                }
            }
            return array;
        }
        return null;
    }

    private void appendPostParams(Request.Builder reqBuilder) {
        FormBody.Builder builder = new FormBody.Builder();
        ConcurrentHashMap<String, Object> publicParams = getPublicParams();
        if (publicParams != null && publicParams.size() > 0) {
            for (String key : publicParams.keySet()) {
                builder.addEncoded(key, publicParams.get(key) + "");
            }
        }
        String privateParamsKey = getPrivateParamsKey();
        if (!TextUtils.isEmpty(privateParamsKey)) {
            JSONArray array = getPrivateParams();
            if (array.length() > 0) {
                builder.addEncoded(privateParamsKey, array.toString());
            }
        }
        String url = getServerUrl();
        LogUtil.d(TAG_REQUEST, "Post :" + url);
        reqBuilder.url(url);
        reqBuilder.post(builder.build());
    }

    protected abstract String getServerUrl();

    protected abstract ConcurrentHashMap<String, Object> getPublicParams();

    protected abstract String getPrivateParamsKey();

    protected abstract String handleHttpOnFailure(IOException e);

    protected void noNetwork() {
    }

    public void cancel() {
        mIsCancel = true;
    }

    public void setRequestPackages(BaseRequestPackage... baseRequestPackages) {
        this.mBaseRequestPackages = baseRequestPackages;
    }

    public void request(final IHttpCallback callback) {
        if (!NetworkUtil.isNetworkAvailable(BaseApplication.getInstance().getApplicationContext())) {
            noNetwork();
            return;
        }
        Request.Builder reqBuilder = new Request.Builder();
        try {
            if (mIsPost) {
                appendPostParams(reqBuilder);
            } else {
                appendGetParams(reqBuilder);
            }
            if (callback != null) {
                mClient.newCall(reqBuilder.build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onFailure(handleHttpOnFailure(e));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ConcurrentHashMap<String, Object> responsesMap = handleResponse(response,
                                callback);
                        if (callback != null) {
                            callback.onResponse(responsesMap);
                        }
                    }
                });
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private ConcurrentHashMap<String, Object> handleResponse(Response httpResponse,
            IHttpCallback callback) throws IOException {
        if (httpResponse != null && httpResponse.isSuccessful() && !mIsCancel) {
            String result = httpResponse.body().string();
            LogUtil.d(TAG_RESPONSE, result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONArray array = new JSONArray(result);
                    if (array.length() > 0 && mBaseRequestPackages != null
                            && mBaseRequestPackages.length > 0) {
                        ConcurrentHashMap<String, Object> responsesMap = new ConcurrentHashMap<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            if (object != null) {
                                for (BaseRequestPackage baseRequestPackage : mBaseRequestPackages) {
                                    if (TextUtils.equals(
                                            object.optString(baseRequestPackage.getCmdKey()),
                                            baseRequestPackage.getCmdValue())
                                            && baseRequestPackage.getJsonParse() != null) {
                                        Object cmdResponse = baseRequestPackage.getJsonParse()
                                                .parseJson(object);
                                        if (cmdResponse != null) {
                                            responsesMap.put(baseRequestPackage.markKey(),
                                                    cmdResponse);
                                        }

                                    }
                                }
                            }
                        }
                        if (responsesMap.size() > 0) {
                            return responsesMap;
                        } else {
                            if (callback != null) {
                                callback.onFailure(result);
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
