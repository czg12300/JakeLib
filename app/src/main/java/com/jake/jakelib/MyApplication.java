package com.jake.jakelib;

import com.jake.library.BaseApplication;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/7/19 14:49
 */

public class MyApplication extends BaseApplication {
    @Override
    protected BaseApplication createInstance() {
        return this;
    }
}
