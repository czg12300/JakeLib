
package com.jake.library.download;

import java.util.ArrayList;

/**
 * 描述:下载任务
 *
 * @author jakechen
 * @since 2016/7/25
 */
public class DownloadJob implements Runnable {
    private ArrayList<IDownloadListener> mDownloadListeners;

    private String mUrl;

    public DownloadJob(String url) {
        this.mUrl = url;
        this.mDownloadListeners = new ArrayList<>();
    }

    @Override
    public void run() {

    }
}
