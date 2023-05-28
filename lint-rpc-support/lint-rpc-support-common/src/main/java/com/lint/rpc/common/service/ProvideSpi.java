package com.lint.rpc.common.service;

import com.lint.rpc.common.LintConf;
import com.lint.rpc.common.pool.ConfPool;
import com.lint.rpc.common.spi.IProvideService;

import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;

public final class ProvideSpi {

    private IProvideService provideService = null;

    private volatile boolean isInit = false;

    public void init(){
        if(!isInit){
            synchronized (this){
                if(!isInit){
                    ConfPool confPool = ConfPool.getInstance();
                    LintConf lintConf = confPool.get();
                    ServiceLoader<IProvideService> loader = ServiceLoader.load(IProvideService.class);
                    for (IProvideService ps : loader) {
                        if (!lintConf.getProvideSpiType().equals(ps.getType())) {
                            continue;
                        }

                        provideService = ps;
                        break;
                    }
                    isInit = true;
                }
            }
        }
    }

    public LinkedHashSet<InetSocketAddress> getAddressByServiceName(String serviceName){
        if(null == provideService || null == provideService.getProvide()){
            return null;
        }
        return provideService.getProvide().get(serviceName);
    }


    private static class LazyHolder {
        private static final ProvideSpi INSTANCE = new ProvideSpi();
    }

    public static ProvideSpi getInstance() {
        return ProvideSpi.LazyHolder.INSTANCE;
    }

    private ProvideSpi(){}

}
