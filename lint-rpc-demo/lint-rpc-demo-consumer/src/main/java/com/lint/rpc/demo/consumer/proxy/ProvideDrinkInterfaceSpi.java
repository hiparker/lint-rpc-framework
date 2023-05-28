package com.lint.rpc.demo.consumer.proxy;

import com.lint.rpc.common.annotation.RpcClient;
import com.lint.rpc.demo.spi.ProvideDrinkInterface;

@RpcClient(name = "provide2", version = 1)
public interface ProvideDrinkInterfaceSpi extends ProvideDrinkInterface {

}
