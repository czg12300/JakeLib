
package com.jake.library.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jake.library.global.IActivityHolder;

/**
 * 描述：activity基类
 *
 * @author jakechen
 * @since 2016/7/15 13:53
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isCallSuperSaveInstanceState()) {
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * 用于状态保存的时候判断是否调用父类的方法，（默认不调用，因为父类主要是做了保存Fragment的状态，容易造成mvp模式下fragment的presenter空指针异常）
     *
     * @return
     */
    protected boolean isCallSuperSaveInstanceState() {
        return false;
    }

}
