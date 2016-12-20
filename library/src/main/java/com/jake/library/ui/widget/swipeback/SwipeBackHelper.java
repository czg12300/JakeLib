package com.jake.library.ui.widget.swipeback;

import java.util.ArrayList;

/**
 * 滑动的全局管理类
 */
public class SwipeBackHelper {

    private static final ArrayList<SwipeBackPage> mPageStack = new ArrayList<>();


    public static void removeActivity(SwipeBackPage swipeBackPage) {
        if (swipeBackPage != null) {
            mPageStack.remove(swipeBackPage);
        }
    }

    public static void addActivity(SwipeBackPage swipeBackPage) {
        if (swipeBackPage != null) {
            mPageStack.add(swipeBackPage);
        }
    }

    protected static SwipeBackPage getPrePage(SwipeBackPage swipeBackPage) {
        int index = 0;
        for (int i = 0; i < mPageStack.size(); i++) {
            if (mPageStack.get(i) == swipeBackPage) {
                index = i;
                break;
            }
        }
        if (index > 0) {
            return mPageStack.get(index - 1);
        } else {
            return null;
        }
    }

}
