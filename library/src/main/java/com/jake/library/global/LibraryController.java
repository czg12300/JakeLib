package com.jake.library.global;

import android.content.Context;

/**
 * 描述：数据库全局变量控制器
 *
 * @author jakechen
 * @since 2016/9/16 13:15
 */

public class LibraryController implements IController {
    private Context context;


    public static LibraryController getInstance() {
        return InstanceBuilder.instance;
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    private static class InstanceBuilder {
        protected static LibraryController instance = new LibraryController();
    }
}
