package com.lint.rpc.common.balance;

/**
 * 负载策略
 *
 * @author 周鹏程
 * @date 2023-05-26 10:10 AM
 **/
public interface ILoadBalancePolicy {

    /**
     * 获取客户端
     */
    int getClientIndex(int size);

}
