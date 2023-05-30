package com.lint.rpc.common.pool;

import com.lint.rpc.common.LintConf;
import com.lint.rpc.common.balance.ILoadBalancePolicy;
import com.lint.rpc.common.service.ProvideSpi;
import com.lint.rpc.common.transport.ClientFactory;
import com.lint.rpc.common.transport.NettyClient;
import com.lint.rpc.common.transport.NettyConf;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 客户端连接池
 *
 * @author 周鹏程
 * @date 2023-05-26 7:38 PM
 **/
public final class ClientPool {

    // serviceName 为服务名
    // hostname+port 为组名
    // 连接池
    private final Map<String, LinkedHashSet<NettyClient>> groupPool = new ConcurrentHashMap<>();
    private final Map<String, LinkedHashSet<String>> servicePool = new ConcurrentHashMap<>();
    private final ReentrantLock rLock = new ReentrantLock();

    public NettyClient get(String serviceName, ILoadBalancePolicy loadBalancePolicy){
        ConfPool confPool = ConfPool.getInstance();
        LintConf lintConf = confPool.get();
        ProvideSpi provideSpi = ProvideSpi.getInstance();
        LinkedHashSet<InetSocketAddress> addressSet =
                provideSpi.getAddressByServiceName(serviceName);
        if(null == addressSet){
            return null;
        }

        LinkedHashSet<String> groupNameSet = servicePool.get(serviceName);
        if(null == groupNameSet || groupNameSet.isEmpty()){
            try {
                rLock.lock();
                groupNameSet = servicePool.get(serviceName);
                if(null == groupNameSet || groupNameSet.isEmpty()){
                    int clientIndex = loadBalancePolicy.getClientIndex(addressSet.size());
                    InetSocketAddress inetSocketAddress =
                            linkedHashSetGetByIndex(addressSet, clientIndex);

                    NettyClient ch = createClient(serviceName, inetSocketAddress);
                    return put(serviceName, ch);
                }
            }finally {
                rLock.unlock();
            }
        }


        NettyClient ch = null;
        // 如果不相等 优先使用为开辟的新链接
        if(groupNameSet.size() != addressSet.size()){
            try {
                rLock.lock();
                if(groupNameSet.size() != addressSet.size()) {
                    InetSocketAddress address = null;
                    Iterator<InetSocketAddress> iterator = addressSet.stream().iterator();
                    while (iterator.hasNext()){
                        address = iterator.next();
                        String groupName = getGroupName(address);

                        boolean contains = groupNameSet.contains(groupName);
                        if(!contains){
                            break;
                        }
                    }

                    ch = createClient(serviceName, address);
                    if(ch != null){
                        return put(serviceName, ch);
                    }
                }
            }finally {
                rLock.unlock();
            }
        }

        // 如果上述操作 ch == null 或者 group相等 那就准备 从现有队列中选择一位连接
        int groupIndex = loadBalancePolicy.getClientIndex(groupNameSet.size());
        String groupName = linkedHashSetGetByIndex(groupNameSet, groupIndex);
        LinkedHashSet<NettyClient> ncSet = groupPool.get(groupName);
        if(null != ncSet){
            // 如果当前开辟的连接数 还未达到自定义配置的最大值 则继续开辟连接
            if(ncSet.size() < lintConf.getClientMaxConnCount()){
                try {
                    rLock.lock();
                    if(ncSet.size() < lintConf.getClientMaxConnCount()){
                        String[] address = groupName.split(":");
                        InetSocketAddress inetSocketAddress = new InetSocketAddress(
                                address[0], Integer.parseInt(address[1]));
                        ch = createClient(serviceName, inetSocketAddress);
                        return put(serviceName, ch);
                    }
                }finally {
                    rLock.unlock();
                }
            }
            int chIndex = loadBalancePolicy.getClientIndex(groupNameSet.size());
            ch = linkedHashSetGetByIndex(ncSet, chIndex);
        }
        return ch;
    }

    private NettyClient put(
            String serviceName, NettyClient nc){
        if(null == nc){
            return null;
        }
        int lockCount = 0;
        try {
            Set<String> groupSet = servicePool.get(serviceName);
            if(null == groupSet){
                rLock.lock();
                lockCount++;
                groupSet = servicePool.computeIfAbsent(serviceName, k -> new LinkedHashSet<>());
            }

            NettyConf conf = nc.getConf();

            String groupName = getGroupName(conf.getAddress());

            Set<NettyClient> nettyClientSet = groupPool.get(groupName);
            if(null == nettyClientSet){
                rLock.lock();
                lockCount++;
                nettyClientSet = groupPool.computeIfAbsent(groupName, k -> new LinkedHashSet<>());
            }

            rLock.lock();
            lockCount++;
            if(nettyClientSet.isEmpty()){
                groupSet.add(groupName);
            }
            nettyClientSet.add(nc);
        }finally {
            if(lockCount > 0){
                for (int i = 0; i < lockCount; i++) {
                    rLock.unlock();
                }
            }
        }
        return nc;
    }


    private NettyClient createClient(String serviceName, InetSocketAddress address){
        NettyConf conf = new NettyConf();
        conf.setAddress(address);
        conf.setCloseCallback((c)->{
            try {
                rLock.lock();
                String groupName = getGroupName(address);
                Set<NettyClient> nettyClients = groupPool.get(groupName);
                nettyClients.remove(c);
                if(nettyClients.isEmpty()){
                    groupPool.remove(groupName);
                    Set<String> groupSet = servicePool.get(serviceName);
                    groupSet.remove(groupName);
                    if(groupSet.isEmpty()){
                        servicePool.remove(serviceName);
                    }
                }
            }finally {
                rLock.unlock();
            }
        });
        ClientFactory factory = ClientFactory.getInstance();
        return factory.create(conf);
    }

    private String getGroupName(InetSocketAddress address){
        String hostName = address.getHostName();
        int port = address.getPort();
        return hostName+":"+port;
    }

    private <T> T linkedHashSetGetByIndex(LinkedHashSet<T> linkedHashSet, int index){
        if(null == linkedHashSet){
            return null;
        }
        T t = null;
        int chCurrIndex = 0;
        Iterator<T> chIterator = linkedHashSet.stream().iterator();
        while (chIterator.hasNext() && chCurrIndex++ <= index){
            t = chIterator.next();
        }
        return t;
    }

    private static class LazyHolder {
        private static final ClientPool INSTANCE = new ClientPool();
    }

    public static ClientPool getInstance() {
        return ClientPool.LazyHolder.INSTANCE;
    }

    private ClientPool(){}
}
