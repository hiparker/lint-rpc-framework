package com.lint.rpc.common.spi.impl;

import com.lint.rpc.common.spi.IProvideService;

import java.net.InetSocketAddress;
import java.util.*;

public class LocalProvideService implements IProvideService {

    @Override
    public String getType() {
        return "local";
    }

    @Override
    public Map<String, LinkedHashSet<InetSocketAddress>> getProvide() {
        LinkedHashSet<InetSocketAddress> set1 = new LinkedHashSet<>();
        set1.add(new InetSocketAddress("127.0.0.1", 8080));


        LinkedHashSet<InetSocketAddress> set2 = new LinkedHashSet<>();
        set2.add(new InetSocketAddress("127.0.0.1", 9090));

        Map<String, LinkedHashSet<InetSocketAddress>> map = new HashMap<>();
        map.put("provide1", set1);
        map.put("provide2", set2);
        return map;
    }
}
