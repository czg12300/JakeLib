
package com.jake.library.download;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewStub;

import com.jake.library.BaseApplication;
import com.jake.library.download.db.DownloadFileOperator;
import com.jake.library.download.db.DownloadFileTable;
import com.jake.library.utils.MD5Util;
import com.jake.library.utils.ThreadPoolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述:下载器,支持多线程，断点续传
 *
 * @author jakechen
 * @since 2016/7/21
 */
public class DownloadManager {
    private static String mDownloadFilePath = BaseApplication.getInstance().getCacheDir()
            .getAbsolutePath();

    private static ArrayList<IDownloadListener> mDownloadListeners = new ArrayList<>();

    private static DownloadFileOperator mDownloadFileOperator = new DownloadFileOperator(
            BaseApplication.getInstance().getContext());
    public static void setDownloadFilePath(String absolutePath) {
        mDownloadFilePath = absolutePath;
    }

    public static void download(String url) {
        start(url);
        if (!isAlreadyDownloadedInSdcard(url)) {

            DownloadFileTable table = mDownloadFileOperator.query(getKey(url), new String[] {
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
                    }else{
                        DownloadJob job=new DownloadJob(url);
                        ThreadPoolUtil.execute(job);
                    }
                } else {
                    DownloadJob job=new DownloadJob(url);
                    ThreadPoolUtil.execute(job);
                }
            }

        }
    }

    private static boolean hasDownloadedPart(String url) {
        DownloadFileTable table = mDownloadFileOperator.query(getKey(url), new String[] {
                DownloadFileTable.FILE_PATH
        });
        if (table != null && TextUtils.equals(getKey(url), table.getFileId())) {
            return true;
        }
        return false;
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

    private static void start(String url) {
        if (mDownloadListeners != null && mDownloadListeners.size() > 0) {
            for (IDownloadListener listener : mDownloadListeners) {
                if (listener != null) {
                    listener.onStart(url);
                }
            }
        }
    }

    private static void fail(String url) {
        if (mDownloadListeners != null && mDownloadListeners.size() > 0) {
            for (IDownloadListener listener : mDownloadListeners) {
                if (listener != null) {
                    listener.onFail(url);
                }
            }
        }
    }

    private static void progress(String url, int positionSize, int totalSize) {
        if (mDownloadListeners != null && mDownloadListeners.size() > 0) {
            for (IDownloadListener listener : mDownloadListeners) {
                if (listener != null) {
                    listener.onProgress(url, positionSize, totalSize);
                }
            }
        }
    }

    private static void success(String url, String path) {
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
    private static String getKey(String url) {
        return MD5Util.getMd5(url);
    }
}
