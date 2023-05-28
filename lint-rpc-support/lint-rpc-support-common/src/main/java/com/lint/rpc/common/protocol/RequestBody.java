package com.lint.rpc.common.protocol;

import com.lint.rpc.common.util.ByteUtil;

import java.io.Serializable;

/**
 * 请求头
 *
 * @author 周鹏程
 * @date 2023-05-26 11:26 AM
 **/
public class RequestBody implements Serializable {

    private String name;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
    //返回的数据
    private Object res;


    public byte[] toBytesArray(){
        return ByteUtil.toByteArray(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }
}
