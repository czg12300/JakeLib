
package com.jake.library.data.download.db;

/**
 * 描述:下载状态
 *
 * @author jakechen
 * @since 2016/7/29
 */
public interface DownloadState {
    /**
     * 下载中
     */
    int DOWNLOADING = 0x001;

    /**
     * 完成
     */
    int FINISH = 0x002;

    /**
     * 暂停
     */
    int PAUSE = 0x003;
    /**
     * 开始
     */
    int START = 0x004;
}
