package com.lint.rpc.common;

public class LintConf {

    /**
     * 客户端最大连接数量
     */
    private byte clientMaxConnCount = 1;

    /**
     * 加载 provide spi 列表类型
     * 如 local nacos 等
     */
    private String provideSpiType;

    /**
     * 客户端最大请求等待时间
     */
    private int requestWaitTimeBySeconds = 60;


    public byte getClientMaxConnCount() {
        return clientMaxConnCount;
    }

    public LintConf setClientMaxConnCount(byte clientMaxConnCount) {
        this.clientMaxConnCount = clientMaxConnCount;
        return this;
    }

    public String getProvideSpiType() {
        return provideSpiType;
    }

    public LintConf setProvideSpiType(String provideSpiType) {
        this.provideSpiType = provideSpiType;
        return this;
    }

    public int getRequestWaitTimeBySeconds() {
        return requestWaitTimeBySeconds;
    }

    public LintConf setRequestWaitTimeBySeconds(int requestWaitTimeBySeconds) {
        this.requestWaitTimeBySeconds = requestWaitTimeBySeconds;
        return this;
    }
}
