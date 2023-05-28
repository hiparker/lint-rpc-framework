package com.lint.rpc.demo.consumer;


import com.lint.rpc.common.LintConf;
import com.lint.rpc.common.bootstrap.LintRpcClientApplication;
import com.lint.rpc.common.pool.ProxyPool;
import com.lint.rpc.demo.consumer.proxy.ProvideDrinkInterfaceSpi;
import com.lint.rpc.demo.consumer.proxy.ProvideEatInterfaceSpi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsumerApplication {

    public static void main(String[] args) throws IOException {
        LintConf conf = new LintConf().setProvideSpiType("local").setClientMaxConnCount((byte)3);
        LintRpcClientApplication.run(conf, ConsumerApplication.class);

        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);
        System.out.println("schedule 方法添加任务：" + LocalDateTime.now());
        threadPool.scheduleAtFixedRate(() -> {
            CountDownLatch c = new CountDownLatch(20);
            // 拟并发请求
            for (int i = 0; i < c.getCount(); i++) {
                new Thread(()->{
                    // 调用链路1 生产者1 -> 生产者2 ， 输出合并结果
                    ProvideEatInterfaceSpi provide1 =
                            ProxyPool.getProxyGet(ProvideEatInterfaceSpi.class);
                    // 调用链路2 生产这2 ，输出一个List集合
                    ProvideDrinkInterfaceSpi provide2 =
                            ProxyPool.getProxyGet(ProvideDrinkInterfaceSpi.class);
                    String choose = provide1.choose();

                    StringBuilder sb = new StringBuilder();
                    List<String> drinkListAll = provide2.getDrinkListAll();
                    for (String s : drinkListAll) {
                        sb.append(s).append(" ");
                    }

                    System.out.println(Thread.currentThread().getName()+
                            " choose >>> "+choose +" ---- drinkListAll >>> "+sb.toString());
                    c.countDown();
                }).start();
            }
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程执行完毕");
        }, 0,20, TimeUnit.SECONDS); // 3s 之后执行

        System.in.read();
    }

}
