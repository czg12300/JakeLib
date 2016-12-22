package com.jake.jakelib.http;

import com.jake.library.data.http.HttpDataFetch;
import com.jake.library.data.http.HttpDataFetchLoader;

import java.util.concurrent.ExecutorService;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/12/22 17:57
 */

public class OkHttpDataFetchLoader implements HttpDataFetchLoader {
    @Override
    public HttpDataFetch getDataFetch(ExecutorService executorService) {
        return new OkHttpDataFetch(executorService);
    }
}
