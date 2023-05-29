package com.lint.rpc.common.transport;

import com.lint.rpc.common.protocol.RequestBody;
import com.lint.rpc.common.protocol.RequestContent;
import com.lint.rpc.common.protocol.RequestHeader;
import com.lint.rpc.common.service.ProvideServiceSpi;
import com.lint.rpc.common.spi.LintService;
import com.lint.rpc.common.thread.ExecuteThread;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.Method;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(!(msg instanceof RequestContent)){
            return;
        }

        //System.out.println("服务端处理数据......");

        RequestContent content = (RequestContent) msg;
        RequestHeader requestHeader = content.getRequestHeader();
        RequestBody requestBody = content.getRequestBody();

        // 本身可以受到 NettyEventLoop线程 进行多线程执行
        ProvideServiceSpi spi = ProvideServiceSpi.getInstance();
        LintService service = spi.getService(requestBody.getName(), requestHeader.getVersion());
        if(null == service){
            return;
        }

        try {
            Method method = service.getClass().getMethod(requestBody.getMethodName());
            Object res = method.invoke(service, requestBody.getArgs());
            requestBody.setRes(res);
        }catch (Exception e){
            e.printStackTrace();
        }

        requestHeader.setLength(requestBody.toBytesArray().length);
        ctx.channel().writeAndFlush(content);

        // 转多线程处理
//        ExecuteThread et = ExecuteThread.getInstance();
//        et.execute(()->{
//            ProvideServiceSpi spi = ProvideServiceSpi.getInstance();
//            LintService service = spi.getService(requestBody.getName(), requestHeader.getVersion());
//            if(null == service){
//                return;
//            }
//
//            try {
//                Method method = service.getClass().getMethod(requestBody.getMethodName());
//                Object res = method.invoke(service, requestBody.getArgs());
//                requestBody.setRes(res);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            requestHeader.setLength(requestBody.toBytesArray().length);
//            ctx.channel().writeAndFlush(content);
//        });
    }
}