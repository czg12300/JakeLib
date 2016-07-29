
package com.jake.library.download;

import com.jake.library.download.db.DownloadFile;
import com.jake.library.download.db.DownloadFileOperator;
import com.jake.library.download.db.DownloadPart;
import com.jake.library.download.db.DownloadPartOperator;
import com.jake.library.download.db.DownloadState;
import com.jake.library.http.OkHttpClientManager;
import com.jake.library.utils.FileUtil;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 描述:下载任务
 *
 * @author jakechen
 * @since 2016/7/25
 */
public class DownloadJob
        implements Runnable, DownloadPartTask.IDownloadPartListener, Handler.Callback {
    private static final int MSG_SUCCESS = 0x001;

    private static final int MSG_FAIL = 0x002;

    private static final int MSG_PROGRESS = 0x003;

    private DownloadFile mDownloadFile;

    private Handler mHandler;

    private String mUrl;

    private String mKey;

    private boolean isFinish = false;

    private ArrayList<DownloadPartTask> mTaskList;

    public DownloadJob(String url) {
        this.mUrl = url;
        mKey = DownloadManager.getKey(mUrl);
        mTaskList = new ArrayList<>();
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    @Override
    public void run() {
        mDownloadFile = DownloadFileOperator.getInstance().query(mKey);
        if (mDownloadFile != null) {
            if (mDownloadFile.partIds != null && mDownloadFile.partIds.length > 0) {
                for (int i = 0; i < mDownloadFile.partIds.length; i++) {
                    startPartTask(mDownloadFile.partIds[i]);
                }
            } else {
                startNewPartTask();
            }
        } else {
            startNewPartTask();
        }
        mDownloadFile.state = DownloadState.START;
        DownloadFileOperator.getInstance().update(mKey, mDownloadFile);
        while (!isFinish) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mDownloadFile.state = DownloadState.DOWNLOADING;
            DownloadFileOperator.getInstance().update(mKey, mDownloadFile);
            mHandler.sendEmptyMessage(MSG_PROGRESS);
        }

    }

    private void startNewPartTask() {
        long totalSize = getFileSizeByUrl(true);
        mDownloadFile = new DownloadFile();
        mDownloadFile.id = mKey;
        mDownloadFile.path = DownloadManager.getDownloadDir() + File.separator
                + DownloadManager.formatFileName(mUrl);
        mDownloadFile.url = mUrl;
        mDownloadFile.positionSize = 0;
        mDownloadFile.state = DownloadState.START;
        mDownloadFile.totalSize = totalSize;
        // 统一创建下载文件
        FileUtil.createFile(mDownloadFile.path, true);
        int partCount = getPartCount(totalSize);
        int temp = (int) (totalSize / partCount);
        String[] partIds = new String[partCount];
        for (int i = 0; i < partCount; i++) {
            long rangeStart = i * temp;
            long rangeEnd = 0;
            if (i < partCount - 1) {
                rangeEnd = rangeStart + temp;
            } else {
                rangeEnd = totalSize;
            }
            final String partId = mKey + i;
            partIds[i] = partId;
            DownloadPart part = new DownloadPart();
            part.id = partId;
            part.rangeStart = rangeStart;
            part.rangeEnd = rangeEnd;
            part.fileId = mDownloadFile.id;
            part.path = mDownloadFile.path;
            part.positionSize = 0;
            part.totalSize = rangeEnd - rangeStart;
            part.state = mDownloadFile.state;
            part.url = mDownloadFile.url;
            // 插入数据库
            DownloadPartOperator.getInstance().insert(part);
            startPartTask(part);
        }
        mDownloadFile.partIds = partIds;
        mDownloadFile.partCount = partCount;
        // 插入数据库
        DownloadFileOperator.getInstance().insert(mDownloadFile);
    }

    private int getPartCount(long totalSize) {
        // 按不同文件的大小使用不同的线程数
        final int little = 1 * 1024 * 1024;
        final int middle = 50 * 1024 * 1024;
        int partCount = 1;
        if (totalSize > little && totalSize <= middle) {
            partCount = 5;
        } else if (totalSize > middle) {
            partCount = 8;
        }
        return partCount;
    }

    private long getFileSizeByUrl(boolean tryAgain) {
        long totalSize = 0;
        try {
            Request request = new Request.Builder().url(mUrl).header("Accept-Encoding", "identity")
                    .build();
            Response response = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request)
                    .execute();
            ResponseBody responseBody = response.body();
            totalSize = responseBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            if (tryAgain) {
                totalSize = getFileSizeByUrl(false);
            } else {
                mHandler.sendEmptyMessage(MSG_FAIL);
            }

        }
        return totalSize > 0 ? totalSize : 0;
    }

    private void startPartTask(String partId) {
        if (!TextUtils.isEmpty(partId)) {
            DownloadPart part = DownloadPartOperator.getInstance().query(partId);
            if (part != null) {
                startPartTask(part);
            }
        }
    }

    private void startPartTask(DownloadPart part) {
        DownloadPartTask task = new DownloadPartTask(part, this);
        DownloadExecutor.submit(task);
        mTaskList.add(task);
    }

    public void pause() {
        if (mTaskList != null && mTaskList.size() > 0) {
            for (DownloadPartTask task : mTaskList) {
                task.pause();
            }
            mDownloadFile.state = DownloadState.PAUSE;
            DownloadFileOperator.getInstance().update(mKey, mDownloadFile);
        }
    }

    public void restart() {
        if (mTaskList != null && mTaskList.size() > 0) {
            for (DownloadPartTask task : mTaskList) {
                DownloadExecutor.submit(task);
            }
        }
    }

    @Override
    public synchronized void downloadChange(DownloadPart part) {
        mDownloadFile.positionSize += part.positionSize;
        if (mDownloadFile.positionSize >= mDownloadFile.totalSize) {
            isFinish = true;
            mDownloadFile.state = DownloadState.FINISH;
            DownloadFileOperator.getInstance().update(mKey, mDownloadFile);
            DownloadManager.removeDownloadJobFromCache(mKey);
            mHandler.sendEmptyMessage(MSG_SUCCESS);
        }
    }

    @Override
    public synchronized void fail(DownloadPart part) {
        isFinish = true;
        pause();
        DownloadManager.removeDownloadJobFromCache(mKey);
        mHandler.sendEmptyMessage(MSG_FAIL);
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MSG_FAIL:
                DownloadManager.fail(mUrl);
                break;
            case MSG_SUCCESS:
                DownloadManager.success(mUrl, mDownloadFile.path);
                break;
            case MSG_PROGRESS:
                DownloadManager.progress(mUrl, mDownloadFile.positionSize, mDownloadFile.totalSize);
                break;
        }
        return true;
    }
}
