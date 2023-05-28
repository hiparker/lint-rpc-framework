package com.lint.rpc.demo.consumer.proxy;

import com.lint.rpc.common.annotation.RpcClient;
import com.lint.rpc.demo.spi.ProvideEatInterface;

@RpcClient(name = "provide1", version = 1)
public interface ProvideEatInterfaceSpi extends ProvideEatInterface {

}
