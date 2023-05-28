package com.lint.rpc.common.exception;


import com.lint.rpc.common.enums.BaseMsg;

/**
 * 框架服务异常
 *
 * @author Parker
 * @date 2020-09-13 19:41
 */
public class RpcException extends RuntimeException{

    private Integer code;

    private String errorMessage;

    public RpcException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public RpcException(Integer code, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public RpcException(BaseMsg msg) {
        super(msg.getMessage());
        this.code = msg.getCode();
        this.errorMessage = msg.getMessage();
    }

    public RpcException(BaseMsg msg, Throwable e) {
        super(msg.getMessage(), e);
        this.code = msg.getCode();
        this.errorMessage = msg.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
