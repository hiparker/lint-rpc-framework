package com.lint.rpc.demo.provide.service;

import com.lint.rpc.common.annotation.RpcService;
import com.lint.rpc.common.pool.ProxyPool;
import com.lint.rpc.demo.provide.proxy.ProvideDrinkInterfaceSpi;
import com.lint.rpc.demo.spi.ProvideEatInterface;

import java.util.Random;

@RpcService(name = "provide1", version = 1)
public class ProvideEatImpl implements ProvideEatInterface {

    private final static String[] food = {"tomato", "pineapple", "banana"};

    private final Random random = new Random();

//    @Override
//    public String choose() {
//        return food[random.nextInt(food.length)];
//    }

    @Override
    public String choose() {
        String f = food[random.nextInt(food.length)];
        ProvideDrinkInterfaceSpi proxyGet = ProxyPool.getProxyGet(ProvideDrinkInterfaceSpi.class);
        if(null != proxyGet){
            String d = proxyGet.choose();
            return "[ eta -> "+f+" And drink -> "+d+"]";
        }else {
            return food[random.nextInt(food.length)];
        }
    }
}
