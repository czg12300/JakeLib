
package com.jake.library.download;

/**
 * 描述:下载监听器
 *
 * @author jakechen
 * @since 2016/7/21
 */
public interface IDownloadListener {
    void onStart(String url);

    void onSuccess(String url, String filePath);

    void onFail(String url);

    void onProgress(String url, int positionSize, int totalSize);
}
