package com.jake.jakelib.http;

import com.jake.library.data.httpold.BaseMultiRequestPackage;
import com.jake.library.data.httpold.IMultiJsonParse;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/7/19 14:53
 */

public class MultiRequestPackage extends BaseMultiRequestPackage {
    public MultiRequestPackage(String cmd, Class<? extends IMultiJsonParse> respClass) {
        super(cmd, respClass);
    }

    @Override
    protected String getCmdKey() {
        return "i";
    }
}
