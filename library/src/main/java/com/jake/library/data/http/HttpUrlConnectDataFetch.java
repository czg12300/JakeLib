package com.jake.library.data.http;

import android.support.annotation.NonNull;

import com.jake.library.utils.IoUtils;
import com.jake.library.utils.NetworkUtils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;


/**
 * 描述：httpUrlConnection
 *
 * @author jakechen
 * @since 2016/12/22 14:19
 */

public class HttpUrlConnectDataFetch extends HttpDataFetch {
    protected static final int DEFAULT_TIME_OUT = 10000;

    public HttpUrlConnectDataFetch(ExecutorService executorService) {
        super(executorService);
    }

    @Override
    protected void requestByPost(RequestPackage requestPackage, IResponse response, HttpRequestCallback callback) throws IOException {
        String BOUNDARY = "----WebKitFormBoundaryDwvXSRMl0TBsL6kW"; // 定义数据分隔线
        HttpURLConnection connection = createHttpURLConnection(requestPackage.getUrl(), requestPackage);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        boolean isUpdateFile = requestPackage.getFiles() != null && requestPackage.getFiles().length > 0;
        OutputStream out = null;
        if (isUpdateFile) {
            out = handleUpdateFile(BOUNDARY, connection, requestPackage);
        }
        if (out == null) {
            out = new DataOutputStream(connection.getOutputStream());
        }
        HashMap<String, Object> params = requestPackage.getParams();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append("--").append(BOUNDARY).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + key + "\"");
                sb.append("\r\n").append("\r\n").append(params.get(key)).append("\r\n");
                byte[] data = sb.toString().getBytes();
                out.write(data);
            }
        }
        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
        out.write(end_data);
        out.flush();
        IoUtils.close(out);
        handleResponse(connection, response, callback);
        connection.disconnect();
    }

    @Override
    protected void requestByGet(RequestPackage requestPackage, IResponse response, HttpRequestCallback callback) throws IOException {
        StringBuilder builder = new StringBuilder(requestPackage.getUrl());
        HashMap<String, Object> params = requestPackage.getParams();
        if (params != null && params.size() > 0) {
            if (requestPackage.getUrl().contains("?")) {
                builder.append("&");
            } else {
                builder.append("?");
            }
            for (String key : params.keySet()) {
                builder.append(key).append("=").append(NetworkUtils.encode(params.get(key) + "")).append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
        }
        String reqUrl = builder.toString();
        HttpURLConnection connection = createHttpURLConnection(reqUrl, requestPackage);
        connection.setRequestMethod("GET");
        connection.connect();
        handleResponse(connection, response, callback);
        connection.disconnect();
    }


    /**
     * 创建HttpURLConnection
     *
     * @param reqUrl
     * @param requestPackage
     * @return
     * @throws IOException
     */
    private HttpURLConnection createHttpURLConnection(String reqUrl, RequestPackage requestPackage) throws IOException {
        URL url = new URL(reqUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (requestPackage.getHeaders() != null && requestPackage.getHeaders().size() > 0) {
            for (String key : requestPackage.getHeaders().keySet()) {
                connection.setRequestProperty(key, requestPackage.getHeaders().get(key));
            }
        }
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "UTF-8");

//        connection.setRequestProperty("Accept", "*/*");
//        connection.setRequestProperty("Accept-Charset", "UTF-8,*;q=0.5");
//        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
//        connection.setRequestProperty("Accept-Language", "zh-CN");
//        connection.setRequestProperty("User-Agent", "Android WYJ");
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 头字段

        // 设置连接超时为8秒
        connection.setConnectTimeout(DEFAULT_TIME_OUT);
        // 设置读取超时为8秒
        connection.setReadTimeout(DEFAULT_TIME_OUT);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        return connection;
    }


    /**
     * 处理post上传文件逻辑
     *
     * @param BOUNDARY
     * @param connection
     * @param requestPackage
     * @return
     * @throws IOException
     */
    @NonNull
    private OutputStream handleUpdateFile(String BOUNDARY, HttpURLConnection connection, RequestPackage requestPackage) throws IOException {
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        OutputStream out = new DataOutputStream(connection.getOutputStream());
        for (File file : requestPackage.getFiles()) {
            String fileName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("//") + 1);
            StringBuilder sb = new StringBuilder();
            sb.append("--").append(BOUNDARY).append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"" + fileName + "\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type: image/jpg\r\n\r\n");
            byte[] data = sb.toString().getBytes();
            out.write(data);
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
            IoUtils.close(in);
        }
        return out;
    }

    /**
     * 处理结果返回
     *
     * @param connection
     * @param response
     * @param callback
     * @throws IOException
     */
    private void handleResponse(HttpURLConnection connection, IResponse response, HttpRequestCallback callback) throws IOException {
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader buffer = new BufferedReader(in);
            String inputLine = null;
            String resultData = null;
            while (((inputLine = buffer.readLine()) != null)) {
                resultData = inputLine + "\n";
            }
            //处理response的结果回调，基类已经做了处理了
            handleResponseResult(response, callback, resultData);
            IoUtils.close(in, buffer);
        } else {
            if (callback != null) {
                callback.onFail(connection.getResponseCode(), connection.getResponseMessage());
            }
        }
    }


}


//
//    private byte[] buildPostDatas() {
//        StringBuilder builder = new StringBuilder();
//        HashMap<String, Object> params = requestPackage.getParams();
//        if (params != null && params.size() > 0) {
//            for (String key : params.keySet()) {
//                builder.append(key).append("=").append(NetworkUtils.encode(params.get(key) + "")).append("&");
//            }
//            builder.deleteCharAt(builder.length() - 1);
//        }
//        return builder.toString().getBytes();
//    }