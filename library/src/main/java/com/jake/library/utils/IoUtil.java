package com.jake.library.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 描述：处理io事件
 *
 * @author jakechen
 * @since 2016/12/12 14:46
 */

public class IoUtil {
    public static void close(Closeable ca) {
        if (ca != null) {
            try {
                ca.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}