package com.lint.rpc.common.pool;

import com.lint.rpc.common.proxy.RpcProxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProxyPool {

    private static final Map<Class<?>, Object> PROXY_MAP = new ConcurrentHashMap<>();

    public static <T>T getProxyGet(Class<T> interfaceClazz){
        Object provideInterface = PROXY_MAP
                .computeIfAbsent(interfaceClazz, k -> RpcProxy.proxyGet(interfaceClazz));
        return (T) provideInterface;
    }

    private ProxyPool(){}
}
