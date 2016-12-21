package com.jake.library.global;

import android.content.Context;

/**
 * 描述：库全局变量控制器
 *
 * @author jakechen
 * @since 2016/9/16 13:15
 */

public class LibraryController implements IAppController {
    private Context context;


    public static LibraryController getInstance() {
        return InstanceBuilder.instance;
    }

    @Override
    public void install(Context context) {
        this.context = context;
        ResourceController.getInstance().init(context);
    }

    @Override
    public Context getContext() {
        return context;
    }

    private static class InstanceBuilder {
        protected static LibraryController instance = new LibraryController();
    }
}
