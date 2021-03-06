package com.jake.library.global;

import android.content.Context;

/**
 * 描述：全局控制器,本类为单例，用于存储全局变量等东西，需在application的onCreate中调用init方法，生命周期跟application一致
 *
 * @author jakechen
 * @since 2016/9/16 11:45
 */

public interface IAppController {
    Context getApplicationContext();

    /**
     * 此方法会在application创建的时候调用
     *
     * @param context
     */
    void install(Context context);

    void onCreate();

    void onLowMemory();

    /**
     * 用于判断是否调用初始化
     *
     * @return
     */
    boolean isInstall();
}
