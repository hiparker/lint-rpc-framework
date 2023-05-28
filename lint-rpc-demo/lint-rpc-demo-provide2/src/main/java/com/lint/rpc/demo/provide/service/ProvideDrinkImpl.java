package com.lint.rpc.demo.provide.service;

import com.lint.rpc.common.annotation.RpcService;
import com.lint.rpc.demo.spi.ProvideDrinkInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RpcService(name = "provide2", version = 1)
public class ProvideDrinkImpl implements ProvideDrinkInterface {

    private final static String[] drink = {"coffee", "tea", "soda", "milk"};

    private final Random random = new Random();

    @Override
    public String choose() {
        return drink[random.nextInt(drink.length)];
    }

    @Override
    public List<String> getDrinkListAll() {
        return new ArrayList<>(Arrays.asList(drink));
    }
}
