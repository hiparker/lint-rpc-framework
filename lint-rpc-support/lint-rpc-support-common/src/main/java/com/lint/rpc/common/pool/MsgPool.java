package com.lint.rpc.common.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息池
 *
 * @author 周鹏程
 * @date 2023-05-26 12:45 PM
 **/
public final class MsgPool {

    private static final Map<Long, Object> MSG_POOL_MAP = new ConcurrentHashMap<>();

    public static void put(long requestId, Object msg){
        if(null == msg){
            return;
        }
        MSG_POOL_MAP.putIfAbsent(requestId, msg);
    }

    public static Object get(long requestId){
        return MSG_POOL_MAP.get(requestId);
    }

    public static Object getAndRemove(long requestId){
        return MSG_POOL_MAP.remove(requestId);
    }

    private MsgPool(){}
}
