package com.lint.rpc.common.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 请求门闩池
 *
 * @author 周鹏程
 * @date 2023-05-26 12:45 PM
 **/
public final class CountDownLatchPool {

    private static final int TIME_OUT = 60;
    private static final Map<Long, CountDownLatch> MSG_POOL_MAP = new ConcurrentHashMap<>();

    public static void put(long requestId, CountDownLatch countDownLatch){
        MSG_POOL_MAP.putIfAbsent(requestId, countDownLatch);
    }

    public static void await(long requestId){
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            // 超时自动释放
            MSG_POOL_MAP.putIfAbsent(requestId, countDownLatch);
            countDownLatch.await(TIME_OUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void await(long requestId, int timeoutSeconds){
        try {
            if(timeoutSeconds < 0){
                timeoutSeconds = 0;
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            // 超时自动释放
            MSG_POOL_MAP.putIfAbsent(requestId, countDownLatch);
            countDownLatch.await(timeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void countDown(long requestId){
        // 超时自动释放
        CountDownLatch countDownLatch = free(requestId);
        if(null != countDownLatch){
            countDownLatch.countDown();
        }
    }

    public static CountDownLatch free(long requestId){
        // 回收
        return MSG_POOL_MAP.remove(requestId);
    }

    private CountDownLatchPool(){}
}
