package com.jake.library.global;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadManager {
    // 线程池核心线程数
    private static int CORE_POOL_SIZE = 10;

    // 线程池最大线程数
    private static int MAX_POOL_SIZE = 500;

    // 额外线程空状态生存时间
    private static int KEEP_ALIVE_TIME = 10000;
    private static int DEFAULT_CAPACITY = 10;
    // 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    protected BlockingQueue<Runnable> workQueue;
    // 线程工厂
    protected ThreadFactory threadFactory;
    // 线程池
    protected ThreadPoolExecutor threadPool;
    private boolean isInstall = false;

    private ThreadManager() {
    }

    /**
     * 初始化
     *
     * @param corePoolSize 线程池核心线程数
     * @param maxPoolSize  线程池最大线程数
     * @param capacity     保存任务的队列的队列容量
     */
    public void install(int corePoolSize, int maxPoolSize, int capacity) {
        if (capacity < 1) {
            capacity = DEFAULT_CAPACITY;
        }
        if (corePoolSize < 1) {
            corePoolSize = CORE_POOL_SIZE;
        }
        if (maxPoolSize < 1) {
            maxPoolSize = MAX_POOL_SIZE;
        }
        workQueue = new ArrayBlockingQueue<>(capacity);
        threadFactory = new ThreadFactory() {
            private final AtomicInteger integer = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "myThreadPool thread:" + integer.getAndIncrement());
            }
        };
        threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
        isInstall = true;
    }

    private void checkInstall() {
        if (!isInstall) {
            install(CORE_POOL_SIZE, MAX_POOL_SIZE, DEFAULT_CAPACITY);
        }
    }

    /**
     * 从线程池中抽取线程，执行指定的Runnable对象
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        checkInstall();
        threadPool.execute(runnable);
    }

    /**
     * 提交执行线程
     *
     * @param runnable
     */
    public void submit(Runnable runnable) {
        checkInstall();
        threadPool.submit(runnable);
    }

    public static ThreadManager getInstance() {
        return InstanceBuilder.instance;
    }

    /**
     * 单例构造器
     */
    private static class InstanceBuilder {
        protected static ThreadManager instance = new ThreadManager();
    }
}
