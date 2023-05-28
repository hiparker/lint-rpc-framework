package com.lint.rpc.common.proxy;

import com.lint.rpc.common.annotation.RpcClient;

import java.lang.reflect.Proxy;

/**
 * 代理类
 *
 * @author 周鹏程
 * @date 2023-05-26 11:47 AM
 **/
public final class RpcProxy {

    public static <T> T proxyGet(Class<T> interfaceInfo){
        if(null == interfaceInfo){
            return null;
        }

        RpcClient annotation = interfaceInfo.getAnnotation(RpcClient.class);
        if(null == annotation){
            return null;
        }

        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] methodArray = {interfaceInfo};

        return (T) Proxy.newProxyInstance(classLoader, methodArray, new RpcInvocationHandler(interfaceInfo));
    }

    private RpcProxy(){}
}
