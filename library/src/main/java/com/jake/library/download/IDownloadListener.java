
package com.jake.library.download;

/**
 * 描述:下载监听器
 *
 * @author jakechen
 * @since 2016/7/21
 */
public interface IDownloadListener {
    void onStart();

    void onSuccess();

    void onFail();

    void onProgress(int positionSize, int totalSize);
}
