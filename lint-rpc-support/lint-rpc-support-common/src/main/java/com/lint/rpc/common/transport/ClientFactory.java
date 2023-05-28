package com.lint.rpc.common.transport;


/**
 * 获取 client
 *
 * @author 周鹏程
 * @date 2023-05-26 3:59 PM
 **/
public class ClientFactory {


    public NettyClient create(NettyConf nettyConf){
        return new NettyClient(nettyConf);
    }

    private static class LazyHolder {
        private static final ClientFactory INSTANCE = new ClientFactory();
    }

    public static ClientFactory getInstance() {
        return ClientFactory.LazyHolder.INSTANCE;
    }

    private ClientFactory(){}
}
