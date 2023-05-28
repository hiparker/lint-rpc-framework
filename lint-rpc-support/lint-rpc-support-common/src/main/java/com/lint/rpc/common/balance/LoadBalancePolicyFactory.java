package com.lint.rpc.common.balance;

import com.lint.rpc.common.annotation.RpcClient;
import com.lint.rpc.common.pool.ClientPool;
import com.lint.rpc.common.transport.NettyClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载策略单例工厂
 *
 * @author 周鹏程
 * @date 2023-05-26 10:53 AM
 **/
public class LoadBalancePolicyFactory {


    private final Object lock = new Object();
    private final Map<Class<? extends ILoadBalancePolicy>,  ILoadBalancePolicy>
            loadBalancePolicyMap = new ConcurrentHashMap<>();


    public NettyClient getClient(Class<?> interfaceClazz) {
        RpcClient rpcClientAnnotation = interfaceClazz.getAnnotation(RpcClient.class);
        ILoadBalancePolicy loadBalancePolicy = getLoadBalancePolicy(rpcClientAnnotation.loadBalancePolicy());
        if(null == loadBalancePolicy){
            return null;
        }

        ClientPool pool = ClientPool.getInstance();
        // 选择客户端
        return pool.get(rpcClientAnnotation.name(), loadBalancePolicy);
    }


    private ILoadBalancePolicy getLoadBalancePolicy(
            Class<? extends ILoadBalancePolicy> loadBalancePolicyClazz){
        ILoadBalancePolicy loadBalancePolicy = loadBalancePolicyMap.get(loadBalancePolicyClazz);
        if(null != loadBalancePolicy){
            return loadBalancePolicy;
        }

        // DCL
        synchronized (lock){
            loadBalancePolicy = loadBalancePolicyMap.get(loadBalancePolicyClazz);
            if(null != loadBalancePolicy){
                return loadBalancePolicy;
            }

            try {
                loadBalancePolicy = loadBalancePolicyClazz.newInstance();
                loadBalancePolicyMap.putIfAbsent(loadBalancePolicyClazz, loadBalancePolicy);
            }catch (Exception e){
                e.printStackTrace();
            }
            return loadBalancePolicy;
        }
    }

    private LoadBalancePolicyFactory(){}

    private static class LazyHolder {
        private static final LoadBalancePolicyFactory INSTANCE = new LoadBalancePolicyFactory();
    }

    public static LoadBalancePolicyFactory getInstance() {
        return LazyHolder.INSTANCE;
    }
}
