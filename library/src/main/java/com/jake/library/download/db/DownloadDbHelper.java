
package com.jake.library.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jake.library.db.BaseDbHelper;

/**
 * 描述:下载数据
 *
 * @author jakechen
 * @since 2016/7/22
 */
public class DownloadDbHelper extends BaseDbHelper {
    public DownloadDbHelper(Context context, String name, int version) {
        super(context, name, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        db.execSQL(DownloadFileTable.CREATE_SQL);
    }
}
