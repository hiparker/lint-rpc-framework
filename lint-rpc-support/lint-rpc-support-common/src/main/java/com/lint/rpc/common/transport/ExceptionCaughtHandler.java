package com.lint.rpc.common.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 异常处理器
 *
 * @author 周鹏程
 * @date 2023-05-26 19:34:12
 */
public class ExceptionCaughtHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}
