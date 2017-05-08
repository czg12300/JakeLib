package com.jake.library;

import android.app.Application;
import android.support.annotation.CallSuper;

import com.jake.library.global.IAppController;
import com.jake.library.global.LibraryController;

import java.util.ArrayList;

/**
 * application基类
 *
 * @author jake
 * @since 2017/5/8 上午10:21
 */

public class BaseApplication extends Application {
    protected ArrayList<IAppController> mAppControllerList = new ArrayList<>();

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        registerAppController(LibraryController.get());
        for (IAppController appController : mAppControllerList) {
            appController.onCreate();
        }
    }

    private void registerAppController(IAppController appController) {
        if (appController != null) {
            appController.install(getApplicationContext());
            mAppControllerList.add(appController);
        }
    }

    @CallSuper
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (IAppController appController : mAppControllerList) {
            appController.onLowMemory();
        }
    }

}
