
package com.jake.library.download.db;

import com.jake.library.db.TableColumns;

/**
 * 描述:下载文件包
 *
 * @author jakechen
 * @since 2016/7/22
 */
public class DownloadFileTable implements TableColumns {
    static interface State {
        int FINISH = 1;
    }

    /**
     * 下载的文件标识
     */
    public static final String FILE_ID = "file_id";
    /**
     * 下载片段id
     */
    public static final String FILE_PART_ID = "file_part_id";
    public static final String RANGE_START = "range_start";
    public static final String RANGE_END = "range_end";
    public static final String PART_COUNT = "part_count";
    public static final String PART_POSITION_SIZE = "part_position_size";
    public static final String PART_TOTAL_SIZE = "part_total_size";


    public static final String FILE_PATH = "file_path";

    public static final String CREATE_SQL = "";
    private int state;
    private String filePath;
    private String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isFinish() {
        return state == State.FINISH;
    }
}
