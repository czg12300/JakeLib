
package com.jake.library.download;

import com.jake.library.utils.MD5Util;

/**
 * 描述:下载器,支持多线程，断点续传
 *
 * @author jakechen
 * @since 2016/7/21
 */
public class DownloadManager {

    public static void download(String url) {

    }

    private static String formatFileName(String url) {
        if (url != null && url.contains("/")) {
            return url.substring(url.lastIndexOf("/"));
        }
        return url;
    }

    /**
     * 作为数据库保存的唯一标识
     * 
     * @param url
     * @return
     */
    private static String getKey(String url) {
        return MD5Util.getMd5(url);
    }
}
