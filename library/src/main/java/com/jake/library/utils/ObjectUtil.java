
package com.jake.library.utils;

import java.util.List;

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
