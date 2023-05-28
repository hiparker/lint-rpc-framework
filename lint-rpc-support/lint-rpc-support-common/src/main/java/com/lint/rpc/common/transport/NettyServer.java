package com.lint.rpc.common.transport;

import com.lint.rpc.common.enums.RpcMsg;
import com.lint.rpc.common.exception.RpcException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {

    private final NettyServerConf conf;

    public NettyServer(NettyServerConf conf){
        this.conf = conf;
    }

    public NettyServerConf getConf() {
        return conf;
    }

    public void init(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bs = new ServerBootstrap();
        ChannelFuture bind = bs.channel(NioServerSocketChannel.class)
                .group(bossGroup, workerGroup)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new InternalServerMsgCodec());
                        p.addLast(new ServerChannelHandler());
                    }
                })
                .bind(conf.getPort());

        try {
            System.out.println("Lint Server port: " + conf.getPort());
            ChannelFuture f = bind.sync();
            if (!f.isSuccess()) {
                throw new RpcException(RpcMsg.EXCEPTION_SERVICE_NOT_STARTED);
            }

            bind.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
