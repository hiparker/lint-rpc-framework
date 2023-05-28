package com.lint.rpc.common.annotation;

import java.lang.annotation.*;

/**
 * RPC Provide 服务端注解
 *
 * @author 周鹏程
 * @date 2023-05-26 10:04 AM
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcService {

    /** 负载服务名 */
    String name();

    /** 负载服务版本 */
    byte version();

}
