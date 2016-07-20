package com.jake.jakelib;

import com.jake.library.BaseApplication;
import com.jake.library.ui.skin.SkinManager;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/7/19 14:49
 */

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }

    @Override
    protected BaseApplication createInstance() {
        return this;
    }
}
