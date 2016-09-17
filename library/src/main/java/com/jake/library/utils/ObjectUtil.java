
package com.jake.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ObjectUtil {

    public static boolean isListAvailable(List<?> list) {
        return list != null && list.size() > 0;
    }

    public static boolean isNotNull(Object object) {
        return object != null;
    }

    public static boolean isSameObject(Object obj1, Object obj2) {
        return obj1 == obj2;
    }
}
