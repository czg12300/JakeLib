
package com.jake.library.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 描述: SDCard工具类
 * 
 * @author chenys
 * @since 2013-7-11 下午4:25:27
 */
public class SdCardUtil {

    /**
     * sdcard
     */
    public static final String SDCARD_FOLDER = Environment.getExternalStorageDirectory().toString();

    private SdCardUtil() {
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

}
