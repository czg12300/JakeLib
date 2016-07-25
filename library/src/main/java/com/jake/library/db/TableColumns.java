
package com.jake.library.db;

import android.provider.BaseColumns;

/**
 * 描述:表属性
 *
 * @author jakechen
 * @since 2016/7/21
 */
public interface TableColumns extends BaseColumns {
    /**
     * 创建时间
     */
    public static final String CREATE_AT = "create_at";

    /**
     * 修改时间
     */
    public static final String MODIFIED_AT = "modified_at";


}
