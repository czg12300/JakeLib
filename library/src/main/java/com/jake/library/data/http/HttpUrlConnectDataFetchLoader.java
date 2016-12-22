package com.jake.library.data.http;

import java.util.concurrent.ExecutorService;

/**
 * 描述：httpUrlConnection 的loader
 * *
 *
 * @author jakechen
 * @since 2016/12/22 11:39
 */

public class HttpUrlConnectDataFetchLoader implements HttpDataFetchLoader {
    @Override
    public HttpDataFetch getDataFetch(ExecutorService executorService) {
        return new HttpUrlConnectDataFetch(executorService);
    }
}
