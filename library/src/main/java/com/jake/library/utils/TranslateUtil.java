package com.jake.library.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * TranslateUtil
 * <p/>
 * <pre>翻譯工具
 * PS: 透過google translate
 * </pre>
 *
 * @author catty
 * @version 1.0, Created on 2011/9/2
 */
public class TranslateUtil {

    protected static final String URL_TEMPLATE = "http://translate.google.com/?langpair={0}&text={1}";
    protected static final String ID_RESULTBOX = "result_box";
    protected static final String ENCODING = "UTF-8";

    protected static final String AUTO = "auto"; // google自動判斷來源語系
    protected static final String TAIWAN = "zh-TW"; // 繁中
    protected static final String CHINA = "zh-CN"; // 簡中
    protected static final String ENGLISH = "en"; // 英
    protected static final String JAPAN = "ja"; // 日

    /**
     * <pre>Google翻譯
     * PS: 交由google自動判斷來源語系
     * </pre>
     *
     * @param text
     * @param target_lang 目標語系
     * @return
     * @throws Exception
     */
    public static String translate(final String text, final String target_lang) throws Exception {
        return translate(text, AUTO, target_lang);
    }

    /**
     * <pre>Google翻譯</pre>
     *
     * @param text
     * @param src_lang    來源語系
     * @param target_lang 目標語系
     * @return
     * @throws Exception
     */
    public static String translate(final String text, final String src_lang, final String target_lang)
            throws Exception {
//        InputStream is = null;
//        Document doc = null;
//        Element ele = null;
//        try {
//            // create URL string
        String url = MessageFormat.format(URL_TEMPLATE,
                URLEncoder.encode(src_lang + "|" + target_lang, ENCODING),
                URLEncoder.encode(text, ENCODING));
//
//            // connect & download html
//            is = HttpClientUtil.downloadAsStream(url);
//
//            // parse html by Jsoup
//            doc = Jsoup.parse(is, ENCODING, "");
//            ele = doc.getElementById(ID_RESULTBOX);
//            String result = ele.text();
        httpRequest(url);
        return null;

//        } finally {
//            is = null;
//            doc = null;
//            ele = null;
//        }
    }

    private static void httpRequest(String url) throws IOException {
        //根据请求URL创建一个Request对象
        Request request = new Request.Builder().url(url).build();
//        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
//根据Request对象发起Get同步Http请求
        Response response = client.newCall(request).execute();

        String result = response.body().string();
        InputStream is = response.body().byteStream();
        byte[] bytes = response.body().bytes();
        LogUtil.d("==========="+result);
//根据Request对象发起Get异步Http请求，并添加请求回调
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onResponse(final Response response) throws IOException {
//                //请求成功，此处对请求结果进行处理
//                String result = response.body().string();
//                InputStream is = response.body().byteStream();
//                byte[] bytes = response.body().bytes();
//                LogUtil.d("==========="+result);
//            }
//
//            @Override
//            public void onFailure(Request request, IOException e) {
//                //请求失败
//            }
//        });
    }

    /**
     * <pre>Google翻譯: 簡中-->繁中</pre>
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String cn2tw(final String text) throws Exception {
        return translate(text, CHINA, TAIWAN);
    }
    public static String cn2en(final String text) throws Exception {
        return translate(text, CHINA, ENGLISH);
    }

    /**
     * <pre>Google翻譯: 繁中-->簡中</pre>
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String tw2cn(final String text) throws Exception {
        return translate(text, TAIWAN, CHINA);
    }

    /**
     * <pre>Google翻譯: 英文-->繁中</pre>
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String en2tw(final String text) throws Exception {
        return translate(text, ENGLISH, TAIWAN);
    }

    /**
     * <pre>Google翻譯: 繁中-->英文</pre>
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String tw2en(final String text) throws Exception {
        return translate(text, TAIWAN, ENGLISH);
    }

    /**
     * <pre>Google翻譯: 日文-->繁中</pre>
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String jp2tw(final String text) throws Exception {
        return translate(text, JAPAN, TAIWAN);
    }

    /**
     * <pre>Google翻譯: 繁中-->日</pre>
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String tw2jp(final String text) throws Exception {
        return translate(text, TAIWAN, JAPAN);
    }

}