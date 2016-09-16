package com.jake.jakelib.global;

import com.jake.library.global.BaseResourceController;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/9/16 13:09
 */

public class ResourceController extends BaseResourceController {
    private ResourceController() {
    }

    public static ResourceController getInstance() {
        return InstanceBuilder.instance;
    }

    private static class InstanceBuilder {
        protected static ResourceController instance = new ResourceController();
    }
}
