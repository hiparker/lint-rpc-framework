/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lint.rpc.common.enums;

/**
 * 核心类 - 消息
 *
 * @author Parker
 * @date 2020-09-13 19:41
 */
public enum RpcMsg implements BaseMsg {

    /** 未设置配置 */
    EXCEPTION_NOT_SET_CONF(2,"The producer spi type is not set in the configuration or is specified in the configuration"),
    /** 服务未启动 */
    EXCEPTION_SERVICE_NOT_STARTED(2,"Service not started"),


    /** 请求超时 */
    EXCEPTION_TIMEOUT(408,"Request timeout"),
    /** 无法建立连接 */
    EXCEPTION_NOT_CONNECTION(501,"Unable to establish connection"),
    /** 请求错误 */
    EXCEPTION_ERROR(500,"Request error"),


    ;

    private final int code;
    private final String message;

    RpcMsg(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
