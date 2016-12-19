package com.jake.library.global;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * 描述：持有所有继承与baseActivity的activity实例
 *
 * @author jakechen
 * @since 2016/12/19 11:19
 */

public interface IActivityHolder {
    void putActivity(String key, WeakReference<Activity> activity);

    void removeActivity(String key);
}
