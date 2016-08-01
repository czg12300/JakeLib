
package com.jake.jakelib;

import com.jake.jakelib.http.Cmd;
import com.jake.jakelib.http.MyResponse;
import com.jake.jakelib.http.MyResponse1;
import com.jake.jakelib.http.RequestTask;
import com.jake.library.download.DownloadManager;
import com.jake.library.download.IDownloadListener;
import com.jake.library.http.IMultiHttpCallback;
import com.jake.library.ui.BaseActivity;
import com.jake.library.utils.SDCardUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

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
        DownloadManager.setDownloadDir(SDCardUtil.getSdCardRootDir() + File.separator + "下载");
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
