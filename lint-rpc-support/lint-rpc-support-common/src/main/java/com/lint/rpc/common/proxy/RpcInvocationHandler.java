package com.lint.rpc.common.proxy;

import com.lint.rpc.common.LintConf;
import com.lint.rpc.common.annotation.RpcClient;
import com.lint.rpc.common.pool.ConfPool;
import com.lint.rpc.common.pool.CountDownLatchPool;
import com.lint.rpc.common.pool.MsgPool;
import com.lint.rpc.common.enums.RpcMsg;
import com.lint.rpc.common.exception.RpcException;
import com.lint.rpc.common.balance.LoadBalancePolicyFactory;
import com.lint.rpc.common.protocol.RequestBody;
import com.lint.rpc.common.protocol.RequestContent;
import com.lint.rpc.common.protocol.RequestHeader;
import com.lint.rpc.common.transport.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Rpc 代理执行类
 *
 * @author 周鹏程
 * @date 2023-05-26 11:53 AM
 **/
public class RpcInvocationHandler implements InvocationHandler {

    private final Class<?> interfaceInfo;

    public RpcInvocationHandler(Class<?> interfaceInfo){
        this.interfaceInfo = interfaceInfo;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        long startTime = System.currentTimeMillis();
        RpcClient annotation = interfaceInfo.getAnnotation(RpcClient.class);

        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();

        RequestBody requestBody = new RequestBody();
        requestBody.setName(annotation.name());
        requestBody.setMethodName(methodName);
        requestBody.setParameterTypes(parameterTypes);
        requestBody.setArgs(args);
        RequestHeader requestHeader = new RequestHeader(requestBody.toBytesArray());
        requestHeader.setVersion(annotation.version());

        RequestContent requestContent = new RequestContent();
        requestContent.setRequestHeader(requestHeader);
        requestContent.setRequestBody(requestBody);

        LoadBalancePolicyFactory loadBalancePolicyFactory = LoadBalancePolicyFactory.getInstance();
        NettyClient nc = loadBalancePolicyFactory
                .getClient(interfaceInfo);


        Object responseMsg = null;
        try {
            boolean sendFlag = nc.sendMsg(requestContent);
            if(!sendFlag){
                // 无法建立连接
                throw new RpcException(RpcMsg.EXCEPTION_NOT_CONNECTION);
            }

            // 如果无返回值 则直接退出
            if(method.getReturnType().equals(Void.TYPE)){
                return responseMsg;
            }

            ConfPool confPool = ConfPool.getInstance();
            LintConf lintConf = confPool.get();

            // 锁定线程 并等待 60秒，如果有结果会直接返回
            CountDownLatchPool.await(
                    requestHeader.getRequestId(), lintConf.getRequestWaitTimeBySeconds());

            // 消息池查询数据 如果查到则序列化返回 ，超时则返回超时 或者 调用兜底补偿
            responseMsg = MsgPool.getAndRemove(requestHeader.getRequestId());
            if(null == responseMsg){
                // 请求超时
                throw new RpcException(RpcMsg.EXCEPTION_TIMEOUT);
            }
        }catch (RpcException re){
            re.printStackTrace();
        }finally {
            CountDownLatchPool.free(requestHeader.getRequestId());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Invoke "+interfaceInfo.getName()+
                "("+annotation.name()+":"+annotation.version()+
                ")  used >>> " + (endTime - startTime) + " ms！");
        // 序列化处理
        return responseMsg;
    }

}
