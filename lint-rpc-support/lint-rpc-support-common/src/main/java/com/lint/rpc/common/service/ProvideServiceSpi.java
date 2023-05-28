package com.lint.rpc.common.service;

import com.lint.rpc.common.annotation.RpcService;
import com.lint.rpc.common.spi.LintService;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public final class ProvideServiceSpi {


    private final Map<String, LintService> serviceMap = new ConcurrentHashMap<>();

    private volatile boolean isInit = false;

    public void init(){
        if(!isInit){
            synchronized (this){
                if(!isInit){
                    ServiceLoader<LintService> loader = ServiceLoader.load(LintService.class);
                    for (LintService ls : loader) {
                        RpcService annotation = ls.getClass().getAnnotation(RpcService.class);
                        if(null == annotation){
                            System.out.println(
                                    "warning spi class not found @RpcService >>> " +
                                    ls.getClass().getName());
                            continue;
                        }


                        serviceMap.putIfAbsent(annotation.name()+":"+annotation.version(), ls);
                    }
                    isInit = true;
                }
            }
        }
    }

    public LintService getService(String serviceName, short version){
        return serviceMap.get(serviceName+":"+version);
    }


    private static class LazyHolder {
        private static final ProvideServiceSpi INSTANCE = new ProvideServiceSpi();
    }

    public static ProvideServiceSpi getInstance() {
        return LazyHolder.INSTANCE;
    }

    private ProvideServiceSpi(){}

}
