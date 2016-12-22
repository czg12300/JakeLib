package com.jake.library.data.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 描述：http请求管理类
 *
 * @author jakechen
 * @since 2016/12/22 9:58
 */

public class HttpEngine {
    private static volatile HttpEngine engine;
    private ExecutorService mExecutorService;
    private HttpDataFetchLoader mDataFetchLoader;

    private HttpEngine(ExecutorService service, HttpDataFetchLoader dataFetchLoader) {
        mExecutorService = service;
        mDataFetchLoader = dataFetchLoader;
    }

    /**
     * 初始化,在application的onCreate方法里面调用
     *
     * @param builder
     */
    public static void install(Builder builder) {
        engine = builder.build();
    }

    /**
     * 生成数据加载器，通过请求方法
     *
     * @param method
     * @return
     */
    public static HttpDataFetch with(Method method) {
        checkInstallOrNot();
        HttpDataFetch dataFetch = engine.mDataFetchLoader.getDataFetch(engine.mExecutorService);
        dataFetch.setMethod(method);
        return dataFetch;
    }

    /**
     * 检查是否初始化引擎，如果没有就默认初始化
     */
    private static void checkInstallOrNot() {
        if (engine == null) {
            engine = Builder.create().build();
        }
    }

    public enum Method {
        POST, GET
    }

    public static class Builder {
        private ExecutorService executorService;
        private HttpDataFetchLoader dataFetchLoader;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder setExecutorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder setDataFetchLoader(HttpDataFetchLoader dataFetchLoader) {
            this.dataFetchLoader = dataFetchLoader;
            return this;
        }

        public HttpEngine build() {
            if (executorService == null) {
                executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                        new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable runnable) {
                                Thread result = new Thread(runnable, "HttpEngine Dispatcher");
                                result.setDaemon(false);
                                return result;
                            }
                        });
            }
            if (dataFetchLoader == null) {
                dataFetchLoader = new HttpUrlConnectDataFetchLoader();
            }
            return new HttpEngine(executorService, dataFetchLoader);
        }
    }
}
