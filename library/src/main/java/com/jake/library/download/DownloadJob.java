
package com.jake.library.download;

import com.jake.library.download.db.DownloadFileOperator;
import com.jake.library.download.db.DownloadFileTable;

import java.util.ArrayList;

/**
 * 描述:下载任务
 *
 * @author jakechen
 * @since 2016/7/25
 */
public class DownloadJob implements Runnable {
    private DownloadFileTable mDownloadFileTable;

    private String mUrl;

    private String mKey;

    public DownloadFileTable getDownloadFileTable() {
        return mDownloadFileTable;
    }

    public DownloadJob(String url) {
        this.mUrl = url;
        mKey = DownloadManager.getKey(mUrl);
    }

    @Override
    public void run() {
        mDownloadFileTable = DownloadFileOperator.getInstance().query(mKey, new String[] {
                DownloadFileTable.FILE_ID
        });
        if (mDownloadFileTable == null) {
            mDownloadFileTable = new DownloadFileTable();
        }

    }
}
