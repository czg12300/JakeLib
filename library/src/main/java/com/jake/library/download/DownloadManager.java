
package com.jake.library.download;

import com.jake.library.BaseApplication;
import com.jake.library.download.db.DownloadFileOperator;
import com.jake.library.download.db.DownloadFileTable;
import com.jake.library.utils.MD5Util;
import com.jake.library.utils.ThreadPoolUtil;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:下载器,支持多线程，断点续传
 *
 * @author jakechen
 * @since 2016/7/21
 */
public class DownloadManager {
    private static String mDownloadFilePath = BaseApplication.getInstance().getCacheDir()
            .getAbsolutePath();

    private static DownloadFileOperator mDownloadFileOperator = new DownloadFileOperator(
            BaseApplication.getInstance().getContext());

    public static void setDownloadFilePath(String absolutePath) {
        mDownloadFilePath = absolutePath;
    }

    public static void download(String url) {
        final String key = getKey(url);
        if (isExistInCache(key)) {
            return;
        }
        if (isAlreadyDownloadedInSdcard(url)) {
            return;
        }
        DownloadFileTable table = mDownloadFileOperator.query(key, new String[] {
                DownloadFileTable.FILE_PATH
        });
        if (table != null) {
            if (table.isFinish()) {
                if (TextUtils.isEmpty(table.getFilePath())) {
                    File file = new File(table.getFilePath());
                    if (file.exists() && file.isFile()) {
                        success(url, file.getPath());
                        return;
                    }
                } else {
                    startDownload(key, url);
                }
            } else {
                startDownload(key, url);
            }
        }

    }

    private static void startDownload(String key, String url) {
        DownloadJob job = new DownloadJob(url);
        addDownloadJobToCache(key, job);
        ThreadPoolUtil.execute(job);
    }

    private static boolean isAlreadyDownloadedInSdcard(String url) {
        String fileName = formatFileName(url);
        File file = new File(mDownloadFilePath, fileName);
        if (file.exists() && file.isFile()) {
            success(url, file.getPath());
            return true;
        }
        return false;
    }

    public synchronized static void fail(String url) {
        if (mDownloadListeners != null && mDownloadListeners.size() > 0) {
            for (IDownloadListener listener : mDownloadListeners) {
                if (listener != null) {
                    listener.onFail(url);
                }
            }
        }
    }

    public synchronized static void progress(String url, int positionSize, int totalSize) {
        if (mDownloadListeners != null && mDownloadListeners.size() > 0) {
            for (IDownloadListener listener : mDownloadListeners) {
                if (listener != null) {
                    listener.onProgress(url, positionSize, totalSize);
                }
            }
        }
    }

    public synchronized static void success(String url, String path) {
        if (mDownloadListeners != null && mDownloadListeners.size() > 0) {
            for (IDownloadListener listener : mDownloadListeners) {
                if (listener != null) {
                    listener.onSuccess(url, path);
                }
            }
        }
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
    public synchronized static String getKey(String url) {
        return MD5Util.getMd5(url);
    }

    private static ArrayList<IDownloadListener> mDownloadListeners = new ArrayList<>();

    public synchronized static void addDownloadListener(IDownloadListener listener) {
        if (listener != null) {
            mDownloadListeners.add(listener);
        }
    }

    public synchronized static void removeDownloadListener(IDownloadListener listener) {
        if (listener != null) {
            mDownloadListeners.remove(listener);
        }
    }

    public synchronized static ArrayList<IDownloadListener> getAllDownloadListener() {
        return mDownloadListeners;
    }

    // 下载文件缓存
    private static ConcurrentHashMap<String, DownloadJob> mCacheMap = new ConcurrentHashMap<>();

    public synchronized static void addDownloadJobToCache(String key, DownloadJob job) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mCacheMap.put(key, job);
    }

    public synchronized static void removeDownloadJobFromCache(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mCacheMap.remove(key);
    }

    public synchronized static DownloadJob getDownloadJobFromCache(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return mCacheMap.get(key);
    }

    public synchronized static boolean isExistInCache(String key) {
        return getDownloadJobFromCache(key) != null;
    }
}
