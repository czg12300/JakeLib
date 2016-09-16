package com.jake.library.global;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

/**
 * 描述：资源管理器,初始化资源管理器
 *
 * @author jakechen
 * @since 2016/9/16 12:11
 */

public class BaseResourceController {


    protected Context mContext;

    public void init(Context context) {
        this.mContext = context;
    }


    public Point getSreenDimens() {
        Point point = new Point();
        point.set(getDisplayMetrics().widthPixels, getDisplayMetrics().heightPixels);
        return point;
    }

    public float dip(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getDisplayMetrics());
    }

    public int dip(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getDisplayMetrics());
    }

    /**
     * 默认缓存文件夹
     *
     * @return
     */
    public File getCacheDir() {
        return mContext.getCacheDir();
    }

    /**
     * 获取包管理器
     *
     * @return
     */
    public PackageManager getPackageManager() {
        return mContext.getPackageManager();
    }

    /**
     * 获取资源管理器
     *
     * @return
     */
    public Resources getResources() {
        return mContext.getResources();
    }

    /**
     * 获取屏幕参数
     *
     * @return
     */
    public DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    /**
     * 获取assets文件管理器
     *
     * @return
     */
    public AssetManager getAssets() {
        return mContext.getAssets();
    }

    /**
     * 获取layout布局文件
     *
     * @param layoutId
     * @return
     */
    public View inflate(int layoutId) {
        return inflate(layoutId, null);
    }

    public View inflate(int layoutId, ViewGroup root) {
        return View.inflate(mContext, layoutId, root);
    }


    /**
     * 获取资源文件的颜色值
     *
     * @param id
     * @return
     */
    public int getColor(int id) {
        return getResources().getColor(id);
    }

    /**
     * 获取string
     *
     * @param id
     * @return
     */
    public String getString(int id) {
        return getResources().getString(id);
    }

    /**
     * 获取资源文件的尺寸
     *
     * @param id
     * @return
     */
    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    /**
     * 获取资源文件int数组
     *
     * @param id
     * @return
     */
    public int[] getIntArray(int id) {
        return getResources().getIntArray(id);
    }

    /**
     * 获取资源文件string数组
     *
     * @param id
     * @return
     */
    public String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

}
