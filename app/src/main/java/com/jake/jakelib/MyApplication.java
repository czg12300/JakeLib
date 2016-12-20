package com.jake.jakelib;

import android.app.Application;

import com.jake.jakelib.global.AppController;
import com.jake.jakelib.skin.SkinManager;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/7/19 14:49
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
        AppController.getInstance().init(this);
    }

}
