package com.lint.rpc.common.balance;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载策略
 *
 * @author 周鹏程
 * @date 2023-05-26 10:12 AM
 **/
public class RandomLoadBalance implements ILoadBalancePolicy{

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Override
    public int getClientIndex(int size) {
        if(size <= 0){
            return -1;
        }

        return size != 1
                // random bound 上界不包括 需要+1
                ? random.nextInt(size+1)
                : size;
    }
}
