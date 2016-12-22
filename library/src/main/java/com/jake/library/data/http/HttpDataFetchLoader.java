package com.jake.library.data.http;

import java.util.concurrent.ExecutorService;

/**
 * 描述：数据加载器
 *
 * @author jakechen
 * @since 2016/12/22 11:21
 */

public interface HttpDataFetchLoader {
    HttpDataFetch getDataFetch(ExecutorService executorService);
}
