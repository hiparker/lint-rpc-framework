package com.lint.rpc.common.pool;

import com.lint.rpc.common.LintConf;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 配置池
 *
 * @author 周鹏程
 * @date 2023-05-26 12:45 PM
 **/
public final class ConfPool {

    private static final AtomicReference<LintConf> LINT_CONF_ATOMIC_REFERENCE = new AtomicReference<>();

    public LintConf init(final LintConf conf){
        if(null == LINT_CONF_ATOMIC_REFERENCE.get()){
            synchronized(ConfPool.class) {
                if (null == LINT_CONF_ATOMIC_REFERENCE.get()) {
                    return LINT_CONF_ATOMIC_REFERENCE.getAndSet(conf);
                }
            }
        }
        return LINT_CONF_ATOMIC_REFERENCE.get();
    }

    public LintConf get(){
        return LINT_CONF_ATOMIC_REFERENCE.get();
    }


    private static class LazyHolder {
        private static final ConfPool INSTANCE = new ConfPool();
    }

    public static ConfPool getInstance() {
        return ConfPool.LazyHolder.INSTANCE;
    }

    private ConfPool(){}
}
