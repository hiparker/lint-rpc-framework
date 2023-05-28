package com.lint.rpc.common.transport;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class NettyConf {

    private InetSocketAddress address;

    private Consumer<NettyClient> closeCallback;


    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public Consumer<NettyClient> getCloseCallback() {
        return closeCallback;
    }

    public void setCloseCallback(Consumer<NettyClient> closeCallback) {
        this.closeCallback = closeCallback;
    }
}
