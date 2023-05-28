package com.lint.rpc.common.transport;

import com.lint.rpc.common.protocol.RequestBody;
import com.lint.rpc.common.protocol.RequestContent;
import com.lint.rpc.common.protocol.RequestHeader;
import com.lint.rpc.common.service.ProvideServiceSpi;
import com.lint.rpc.common.spi.LintService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof RequestContent)){
            return;
        }

        //System.out.println("服务端处理数据......");

        RequestContent content = (RequestContent) msg;
        RequestHeader requestHeader = content.getRequestHeader();
        RequestBody requestBody = content.getRequestBody();

        ProvideServiceSpi spi = ProvideServiceSpi.getInstance();
        LintService service = spi.getService(requestBody.getName(), requestHeader.getVersion());
        if(null == service){
            return;
        }

        Method method = service.getClass().getMethod(requestBody.getMethodName());
        try {
            Object res = method.invoke(service, requestBody.getArgs());
            requestBody.setRes(res);
        }catch (Exception e){
            e.printStackTrace();
        }

        requestHeader.setLength(requestBody.toBytesArray().length);
        ctx.channel().writeAndFlush(content);
    }
}