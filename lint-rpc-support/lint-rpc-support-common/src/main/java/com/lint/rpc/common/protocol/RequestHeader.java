package com.lint.rpc.common.protocol;

import com.lint.rpc.common.util.ByteUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * 请求头
 *
 * @author 周鹏程
 * @date 2023-05-26 11:26 AM
 **/
public class RequestHeader implements Serializable {

    /** 标识 */
    private int flag;

    /** 请求ID */
    private long requestId;

    /** 长度 */
    private int length;

    public RequestHeader(byte[] requestBodyBytes){
        requestId = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        length = requestBodyBytes.length;
    }

    public void setVersion(byte version){
        flag ^= version;
    }

    public byte getVersion(){
        return (byte)((flag << 24) >> 24);
    }

    public byte[] toBytesArray(){
        return ByteUtil.toByteArray(this);
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
