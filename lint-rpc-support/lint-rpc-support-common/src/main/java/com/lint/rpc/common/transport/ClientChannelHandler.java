package com.lint.rpc.common.transport;

import com.lint.rpc.common.pool.CountDownLatchPool;
import com.lint.rpc.common.pool.MsgPool;
import com.lint.rpc.common.protocol.RequestBody;
import com.lint.rpc.common.protocol.RequestContent;
import com.lint.rpc.common.protocol.RequestHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(!(msg instanceof RequestContent)){
            return;
        }

        //System.out.println("客户端处理数据......");

        RequestContent content = (RequestContent) msg;
        RequestHeader requestHeader = content.getRequestHeader();
        RequestBody requestBody = content.getRequestBody();

        MsgPool.put(requestHeader.getRequestId(), requestBody.getRes());
        CountDownLatchPool.countDown(requestHeader.getRequestId());
    }


}