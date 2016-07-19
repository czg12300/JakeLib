
package com.jake.library;

import android.app.Application;
import android.content.Context;

/**
 * 描述：application基类
 *
 * @author jakechen
 * @since 2016/7/19 10:47
 */

public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = createInstance();
    }

    public Context getContext() {
        return getApplicationContext();
    }

    /**
     * 子类创建自己的实例
     * 
     * @return
     */
    protected BaseApplication createInstance() {
        return this;
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }
}
