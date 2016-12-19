package com.jake.library.global;

import android.content.Context;

/**
 * 描述：全局控制器,本类为单例，用于存储全局变量等东西，需在application的onCreate中调用init方法
 *
 * @author jakechen
 * @since 2016/9/16 11:45
 */

public interface IAppController {

    void init(Context context);

    Context getContext();
}
