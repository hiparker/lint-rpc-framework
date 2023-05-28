package com.lint.rpc.common.bootstrap;

import com.lint.rpc.common.LintConf;
import com.lint.rpc.common.enums.RpcMsg;
import com.lint.rpc.common.exception.RpcException;
import com.lint.rpc.common.pool.ConfPool;
import com.lint.rpc.common.service.ProvideServiceSpi;
import com.lint.rpc.common.service.ProvideSpi;
import com.lint.rpc.common.transport.NettyServer;
import com.lint.rpc.common.transport.NettyServerConf;

/**
 * Lint RPC 启动器
 */
public class LintRpcServerApplication {

    public static void run(int port, LintConf conf, Class<?> baseClazz){
        if (null == conf
                || null == conf.getProvideSpiType() || "".equals(conf.getProvideSpiType())){
            throw new RpcException(RpcMsg.EXCEPTION_NOT_SET_CONF);
        }

        // 1. 加载全局配置
        ConfPool confPool = ConfPool.getInstance();
        confPool.init(conf);

        // 2. 加载SPI 并初始化
        ProvideSpi provideSpi = ProvideSpi.getInstance();
        provideSpi.init();

        // 3. 加载服务端SPI
        ProvideServiceSpi provideServiceSpi = ProvideServiceSpi.getInstance();
        provideServiceSpi.init();

        // TODO baseClazz 后序扩展一下

        // 4. 启动服务端
        NettyServerConf nettyServerConf = new NettyServerConf();
        nettyServerConf.setPort(port);
        NettyServer server = new NettyServer(nettyServerConf);
        server.init();
    }

}
