
package com.jake.jakelib.demo;

import com.jake.jakelib.R;
import com.jake.jakelib.http.Cmd;
import com.jake.jakelib.http.MyResponse;
import com.jake.jakelib.http.MyResponse1;
import com.jake.jakelib.http.RequestTask;
import com.jake.library.http.IMultiHttpCallback;
import com.jake.library.ui.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ConcurrentHashMap;

public class RequestDemoActivity extends BaseActivity {
    private TextView textView;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.result);
        editText = (EditText) findViewById(R.id.edit);
    }

    public void onClick(View view) {
        startActivity(new Intent(this,DownloadActivity.class));
        RequestTask task = new RequestTask();
        task.setRequestPackages(Cmd.test(), Cmd.test1());
        task.request(new IMultiHttpCallback() {
            @Override
            public void onFailure(final String json) {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(textView.getText() + json);
                    }
                });
            }

            @Override
            public void onResponse(ConcurrentHashMap<String, Object> responseMap) {
                if (responseMap != null) {
                    final MyResponse response = (MyResponse) responseMap.get(MyResponse.class
                            .getSimpleName());
                    final MyResponse1 response1 = (MyResponse1) responseMap.get(MyResponse1.class
                            .getSimpleName());
                    if (response != null && response1 != null) {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(response1.getJson() + "\n" + response.getJson());
                            }
                        });

                    }
                }
            }
        });
    }
}
