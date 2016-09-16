
package com.jake.jakelib.demo;

import com.jake.jakelib.R;
import com.jake.library.data.download.DownloadManager;
import com.jake.library.data.download.IDownloadListener;
import com.jake.library.ui.BaseActivity;
import com.jake.library.utils.SdCardUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class DownloadActivity extends BaseActivity implements View.OnClickListener {
    private TextView textView;

    ProgressBar progressBar;

    private String url = "http://shouji.360tpcdn.com/160707/6eae95920eb8912044e41adeb8de5fe4/com.gbits.atm.qihoo_201607050.apk";
    // private String url =
    // "http://dlsw.baidu.com/sw-search-sp/soft/18/17521/91zhushoupc_Windows_1011080p.1460712485.exe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        textView = (TextView) findViewById(R.id.result);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.restart).setOnClickListener(this);
        progressBar.setMax(100);
        DownloadManager.setDownloadDir(SdCardUtil.getSdCardRootDir() + File.separator + "下载");
        DownloadManager.addDownloadListener(new IDownloadListener() {
            @Override
            public void onSuccess(String url, String filePath) {
                textView.setText("成功 ：" + url + "  文件地址：" + filePath);
            }

            @Override
            public void onFail(String url) {
                textView.setText("失败 ：" + url);
            }

            @Override
            public void onPause(String url) {
                textView.setText("暂停中");
            }

            @Override
            public void onProgress(String url, long positionSize, long totalSize) {
                int str = (int) ((100 * positionSize) / totalSize);
                textView.setText(" positionSize=" + positionSize + " totalSize= " + totalSize
                        + "进度：" + str + "%");
                progressBar.setProgress(str);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start) {
            textView.setText("下载");
            DownloadManager.reDownload(url);
        } else if (view.getId() == R.id.pause) {
            textView.setText("暂停");
            DownloadManager.pause(url);
        }else if (view.getId() == R.id.restart){
            textView.setText("继续下载");
            DownloadManager.restart(url);
        }

    }
}
