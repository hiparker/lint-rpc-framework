package com.lint.rpc.common.balance;


import com.lint.rpc.common.annotation.RpcClient;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮巡负载策略
 *
 * @author 周鹏程
 * @date 2023-05-26 10:12 AM
 **/
public class RoundLoadBalance implements ILoadBalancePolicy{

    private final AtomicInteger loopFlag = new AtomicInteger(0);

    @Override
    public int getClientIndex(int size) {
        if(size <= 0){
            return -1;
        }

        return size != 1
                // random bound 上界不包括 需要+1
                ? loopFlag.incrementAndGet() % size
                : size;
    }



    /**
     * 实验 atomicInteger 会不会 循环转换
     */
//    public static void main(String[] args) {
//        int base = 2 << 6;
//        boolean flag = false;
//        for(;;){
//            int i = atomicInteger.decrementAndGet();
//            int index = atomicInteger.decrementAndGet() & base-1;
//            if(i < 0 && !flag ){
//                System.out.println(index + "----" + i);
//                flag = true;
//            }
//            if(i > 0 && flag ){
//                System.out.println(index + "----" + i);
//                flag = false;
//            }
//        }
//    }
}
