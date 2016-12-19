package com.jake.jakelib.global;

import android.content.Context;

import com.jake.library.global.IAppController;
import com.jake.library.global.LibraryController;

/**
 * 描述：全局变量
 *
 * @author jakechen
 * @since 2016/9/16 13:06
 */

public class AppController implements IAppController {
    private Context mContext;

    private AppController() {
    }

    @Override
    public void init(Context context) {
        mContext = context;
        ResourceController.getInstance().init(context);
        LibraryController.getInstance().init(context);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public static AppController getInstance() {
        return InstanceBuilder.instance;
    }

    private static class InstanceBuilder {
        protected static AppController instance = new AppController();
    }
}
