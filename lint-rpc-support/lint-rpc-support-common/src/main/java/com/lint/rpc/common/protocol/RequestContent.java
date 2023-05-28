package com.lint.rpc.common.protocol;

import java.io.Serializable;

/**
 * 综合体
 *
 * @author 周鹏程
 * @date 2023-05-26 11:26 AM
 **/
public class RequestContent implements Serializable {

    private RequestHeader requestHeader;

    private RequestBody requestBody;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public void free() {
        requestHeader = null;
        requestBody = null;
    }

}
