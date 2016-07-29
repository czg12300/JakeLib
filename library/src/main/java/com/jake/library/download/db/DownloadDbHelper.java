
package com.jake.library.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jake.library.BaseApplication;
import com.jake.library.db.BaseDbHelper;

/**
 * 描述:下载数据
 *
 * @author jakechen
 * @since 2016/7/22
 */
public class DownloadDbHelper extends BaseDbHelper {
    private static final String DB_NAME = "_download.db";

    private static DownloadDbHelper mInstance;

    public synchronized static DownloadDbHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadDbHelper(BaseApplication.getInstance().getContext());
        }
        return mInstance;
    }

    public DownloadDbHelper(Context context) {
        super(context, "DB_NAME", 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        db.execSQL(DownloadFile.CREATE_SQL);
        db.execSQL(DownloadPart.CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
