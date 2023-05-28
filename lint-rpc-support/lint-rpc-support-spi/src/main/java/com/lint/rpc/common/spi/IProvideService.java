package com.lint.rpc.common.spi;

import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * SPI 接口 可支持多种 provide 方式
 * 1. 直接列表
 * 2. Nacos
 * 3. ...
 *
 * @author 周鹏程
 * @date 2023-05-26 3:59 PM
 **/
public interface IProvideService {

    String getType();

    Map<String, LinkedHashSet<InetSocketAddress>> getProvide();

}
