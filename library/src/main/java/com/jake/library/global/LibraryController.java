package com.jake.library.global;

import android.content.Context;

/**
 * 描述：库全局变量控制器
 *
 * @author jakechen
 * @since 2016/9/16 13:15
 */

public class LibraryController implements IAppController {
    private static LibraryController mInstance = new LibraryController();
    private Context mAppContext;


    public static LibraryController get() {
        return mInstance;
    }

    @Override
    public Context getApplicationContext() {
        return mAppContext;
    }

    @Override
    public void install(Context context) {
        if (context != null) {
            mAppContext = context.getApplicationContext();
        }
        this.mAppContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public boolean isInstall() {
        return mAppContext!=null;
    }
}
