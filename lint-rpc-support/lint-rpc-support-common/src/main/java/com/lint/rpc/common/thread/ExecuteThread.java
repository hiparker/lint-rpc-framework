package com.lint.rpc.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ExecuteThread {

    private final int cpuCount = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService = new ThreadPoolExecutor(
            getThreadCoreCount(getThreadMaxCount(cpuCount)),
            getThreadMaxCount(cpuCount),
            60, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(1024),
            new ThreadPoolExecutor.CallerRunsPolicy());


    public void execute(Runnable r){
        try {
            executorService.execute(new ThreadWrapper(r));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getThreadCoreCount(int threadCoreCount){
        int coreCount = threadCoreCount >> 1;
        return Math.max(coreCount, 1);
    }

    private int getThreadMaxCount(int cpuCount){
        int threadCoreCount = cpuCount > 2
                ?  cpuCount - 1
                : cpuCount;
        return Math.max(threadCoreCount, 1);
    }

    public static class ThreadWrapper implements Runnable {

        private final Runnable r;

        public ThreadWrapper(Runnable r){
            this.r = r;
        }

        @Override
        public void run() {
            try {
                r.run();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static class LazyHolder {
        private static final ExecuteThread INSTANCE = new ExecuteThread();
    }

    public static ExecuteThread getInstance() {
        return ExecuteThread.LazyHolder.INSTANCE;
    }

    private ExecuteThread(){}

}
