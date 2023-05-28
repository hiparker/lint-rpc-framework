package com.lint.rpc.common.transport;

import com.lint.rpc.common.protocol.RequestBody;
import com.lint.rpc.common.protocol.RequestContent;
import com.lint.rpc.common.protocol.RequestHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.*;
import java.util.List;

/**
 * 内部服务器消息编解码器
 *
 * @author 周鹏程
 * @date 2023-05-26 19:34:07
 */
public final class InternalServerMsgCodec extends
        CombinedChannelDuplexHandler<InternalServerMsgCodec.Decoder, InternalServerMsgCodec.Encoder> {

    /**
     * 类默认构造器
     */
    public InternalServerMsgCodec() {
        super.init(new Decoder(), new Encoder());
    }


    /**
     * 消息解码器
     */
    static final class Decoder extends ByteToMessageDecoder {

        private static final int HEAD_LENGTH = 107;

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf buff, List<Object> out) {
            // 如果消息不满足上面这个两个条件 直接不处理
            if(buff.readableBytes() < HEAD_LENGTH){
                return;
            }

            // 标记读取位置
            buff.markReaderIndex();

            byte[] headByteArray = new byte[HEAD_LENGTH];
            buff.readBytes(headByteArray);

            RequestHeader requestHeader = null;
            try(ByteArrayInputStream in = new ByteArrayInputStream(headByteArray);
                ObjectInputStream ois = new ObjectInputStream(in);
                ) {

                requestHeader = (RequestHeader) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // 如果消息体长度不够 直接退出
            if(null == requestHeader ||
                    buff.readableBytes() < requestHeader.getLength()){
                // 回到标记读取位置
                // 什么时候 消息读全了 什么时候再继续往后执行
                buff.resetReaderIndex();
                return;
            }

            byte[] bodyByteArray = new byte[requestHeader.getLength()];
            buff.readBytes(bodyByteArray);
            RequestBody requestBody;
            try(ByteArrayInputStream in = new ByteArrayInputStream(bodyByteArray);
                ObjectInputStream ois = new ObjectInputStream(in);
            ) {
                requestBody = (RequestBody) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                requestBody = new RequestBody();
            }

//            System.out.println("收到消息 => " +
//                    "requestId = "+requestHeader.getRequestId() +
//                    ", flag = "+requestHeader.getFlag()+
//                    ", bodyName = "+requestBody.getName()+
//                    ", bodyMethodName = "+requestBody.getMethodName());

            RequestContent requestContent = new RequestContent();
            requestContent.setRequestHeader(requestHeader);
            requestContent.setRequestBody(requestBody);

            // 出发消息读取事件
            ctx.fireChannelRead(requestContent);
        }
    }

    /**
     * 消息编码器
     */
    static final class Encoder extends MessageToByteEncoder<RequestContent> {
        @Override
        protected void encode(ChannelHandlerContext ctx, RequestContent innerMsg, ByteBuf byteBuf) {
            // 写出head
            byteBuf.writeBytes(innerMsg.getRequestHeader().toBytesArray());
            // 写出body
            byteBuf.writeBytes(innerMsg.getRequestBody().toBytesArray());
            // 释放内存
            innerMsg.free();
        }
    }

}
