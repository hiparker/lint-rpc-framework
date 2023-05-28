package com.lint.rpc.common.annotation;

import com.lint.rpc.common.balance.ILoadBalancePolicy;
import com.lint.rpc.common.balance.RoundLoadBalance;

import java.lang.annotation.*;

/**
 * RPC Consumer 客户端注解
 *
 * @author 周鹏程
 * @date 2023-05-26 10:04 AM
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcClient {

    /** 负载服务名 */
    String name();

    /** 负载服务版本 */
    byte version();

    /** 负载策略 */
    Class<? extends ILoadBalancePolicy> loadBalancePolicy() default RoundLoadBalance.class;

    /** 拒绝策略 */

}
