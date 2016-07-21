
package com.jake.library.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * 描述:数据库的操作
 *
 * @author jakechen
 * @since 2016/7/21
 */
public abstract class BaseDbOperator<TableEntity> {

    private Context mContext;

    public BaseDbOperator(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 获取可读数据库
     *
     * @return
     */
    protected abstract SQLiteDatabase getReadableDatabase();

    /**
     * 获取可读写数据库
     *
     * @return
     */
    protected abstract SQLiteDatabase getWritableDatabase();

    /**
     * 新增
     *
     * @param tableEntity
     * @return
     */
    public abstract long insert(TableEntity tableEntity);

    /**
     * 更新
     *
     * @param cv
     * @param selection
     * @param selectionArgs
     * @return
     */
    public abstract long update(ContentValues cv, String selection, String[] selectionArgs);

    /**
     * 删除
     *
     * @param selection
     * @param selectionArgs
     * @return
     */
    public abstract long delete(String selection, String[] selectionArgs);

    /**
     * 查询
     *
     * @param selection
     * @param selectionArgs
     * @param orderby
     * @return
     */
    public abstract List<TableEntity> query(String selection, String[] selectionArgs, String orderby);

    /**
     * 查询
     *
     * @param selection
     * @param selectionArgs
     * @param orderby
     * @param limit
     * @return
     */
    public abstract List<TableEntity> query(String selection, String[] selectionArgs,
                                            String orderby, int limit);

    /**
     * 查询
     *
     * @param selection
     * @param selectionArgs
     * @return
     */
    public abstract TableEntity query(String selection, String[] selectionArgs);

    /**
     * 获取数量
     *
     * @param selection
     * @param selectionArgs
     * @return
     */
    public abstract int getCount(String selection, String[] selectionArgs);


}
