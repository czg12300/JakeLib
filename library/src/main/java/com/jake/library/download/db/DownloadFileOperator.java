
package com.jake.library.download.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jake.library.db.BaseDbOperator;

import java.util.List;

/**
 * 描述:下载文件文件
 *
 * @author jakechen
 * @since 2016/7/22
 */
public class DownloadFileOperator extends BaseDbOperator<DownloadFileTable> {
    public DownloadFileOperator(Context context) {
        super(context);
    }

    @Override
    protected SQLiteDatabase getReadableDatabase() {
        return null;
    }

    @Override
    protected SQLiteDatabase getWritableDatabase() {
        return null;
    }

    @Override
    public long insert(DownloadFileTable downloadFileTable) {
        return 0;
    }

    @Override
    public long update(ContentValues cv, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public long delete(String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public List<DownloadFileTable> query(String selection, String[] selectionArgs, String orderby) {
        return null;
    }

    @Override
    public List<DownloadFileTable> query(String selection, String[] selectionArgs, String orderby,
            int limit) {
        return null;
    }

    @Override
    public DownloadFileTable query(String selection, String[] selectionArgs) {
        return new DownloadFileTable();
    }

    @Override
    public int getCount(String selection, String[] selectionArgs) {
        return 0;
    }
}
