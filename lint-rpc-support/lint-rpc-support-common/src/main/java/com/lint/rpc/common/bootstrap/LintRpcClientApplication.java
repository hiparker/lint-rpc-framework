package com.lint.rpc.common.bootstrap;

import com.lint.rpc.common.LintConf;
import com.lint.rpc.common.enums.RpcMsg;
import com.lint.rpc.common.exception.RpcException;
import com.lint.rpc.common.pool.ConfPool;
import com.lint.rpc.common.service.ProvideSpi;

/**
 * Lint RPC 启动器
 */
public class LintRpcClientApplication {

    public static void run(LintConf conf, Class<?> baseClazz){
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

        // TODO baseClazz 后序扩展一下
    }

}
