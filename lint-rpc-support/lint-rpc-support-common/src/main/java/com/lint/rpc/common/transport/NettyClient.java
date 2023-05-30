package com.lint.rpc.common.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class NettyClient {

    private final NettyConf conf;
    private volatile NioSocketChannel ch;

    public NettyClient(NettyConf conf){
        this.conf = conf;
    }

    public boolean sendMsg(Object msg){
        if(ch == null){
            synchronized (this){
                if(ch == null){
                    this.connect();
                }
            }
        }

        if(ch == null){
            return false;
        }
        try {
            ch.writeAndFlush(msg).sync();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public NettyConf getConf() {
        return conf;
    }

    private void connect(){
        if(null == conf.getAddress()){
            return;
        }
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();
        bs.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new InternalServerMsgCodec());
                        pipeline.addLast(new ClientChannelHandler());
                    }
                });
        try {
            ChannelFuture f = bs.connect(conf.getAddress()).sync();
            if (!f.isSuccess()) {
                return;
            }

            ch = (NioSocketChannel) f.channel();
            ch.closeFuture().addListener(this::onLoseConnect);

            System.out.println(">>> 连接到业务服务器成功! "+conf.getAddress()+" <<<");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当失去连接时
     *
     * @param f 预期
     */
    private void onLoseConnect(Future<?> f) {
        System.out.println("系统通知 - 注意: 服务器连接关闭! >>> " + conf.getAddress());
        this.ch = null;
        final Consumer<NettyClient> closeCallback = conf.getCloseCallback();
        if(null != closeCallback){
            closeCallback.accept(this);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(conf.getAddress().getHostName(), conf.getAddress().getPort());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        NettyConf selfConf = this.getConf();
        NettyClient client = (NettyClient) obj;
        NettyConf clientConf = client.getConf();

        return selfConf.getAddress().getHostName().equals(clientConf.getAddress().getHostName())
                && selfConf.getAddress().getPort() == clientConf.getAddress().getPort();
    }
}
