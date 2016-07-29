
package com.jake.jakelib;

import com.jake.jakelib.http.Cmd;
import com.jake.jakelib.http.MyResponse;
import com.jake.jakelib.http.MyResponse1;
import com.jake.jakelib.http.RequestTask;
import com.jake.library.download.DownloadManager;
import com.jake.library.download.IDownloadListener;
import com.jake.library.http.IMultiHttpCallback;
import com.jake.library.ui.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ConcurrentHashMap;

public class DownloadActivity extends BaseActivity {
    private TextView textView;

    ProgressBar progressBar;

    private String url = "http://dlsw.baidu.com/sw-search-sp/soft/18/17521/91zhushoupc_Windows_1011080p.1460712485.exe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        textView = (TextView) findViewById(R.id.result);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        DownloadManager.addDownloadListener(new IDownloadListener() {
            @Override
            public void onSuccess(String url, String filePath) {
                textView.setText("成功 ：" + url + "  文件地址：" + filePath);
            }

            @Override
            public void onFail(String url) {
                textView.setText("失败 ：" + url);
                textView.setText("失败 ：" + url);
            }

            @Override
            public void onProgress(String url, long positionSize, long totalSize) {

                progressBar.setProgress((int) ((100 * positionSize) / totalSize));
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.request) {
            if (view.getTag() != null) {
                DownloadManager.pause(url);
                view.setTag(null);
                ((Button) view).setText("暂停");
            } else {
                ((Button) view).setText("下载中");
                DownloadManager.restart(url);
                view.setTag(url);
            }
        }
    }
}
