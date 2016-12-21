
package com.jake.library.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 描述: SDCard工具类
 *
 * @author chenys
 * @since 2013-7-11 下午4:25:27
 */
public class CacheUtils {

    /**
     * sdcard
     */
    public static final String SDCARD_FOLDER = Environment.getExternalStorageDirectory().toString();

    private CacheUtils() {
    }

    public static String getSdCardRootDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 判断是否存在SDCard
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    /**
     * SDCard剩余大小
     *
     * @return 字节
     */
    public static long getAvailableSize() {
        if (hasSDCard()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return availableBlocks * blockSize;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 是否有足够的空间
     *
     * @param minSize 最小值
     * @return
     */
    public static boolean hasEnoughSpace(long minSize) {
        return getAvailableSize() > minSize;
    }

    /**
     * SDCard总容量大小
     *
     * @return 字节
     */
    public static long getTotalSize() {
        if (hasSDCard()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long totalBlocks = stat.getBlockCount();
                return totalBlocks * blockSize;

            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 这个是手机内存的可用空间大小
     *
     * @return
     */
    public static long getAvailableInternalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 这个是手机内存的总空间大小
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static File getCacheDir(Context context) {
        return context != null ? context.getCacheDir() : null;
    }

    public static File getFilesDir(Context context) {
        return context != null ? context.getFilesDir() : null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static File getCodeCacheDir(Context context) {
        return context != null ? context.getCodeCacheDir() : null;
    }

    public static File getDatabasePath(Context context, String dbName) {
        return context != null ? context.getDatabasePath(dbName) : null;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static File getDataDir(Context context, String dbName) {
        return context != null ? context.getDataDir() : null;
    }
}
