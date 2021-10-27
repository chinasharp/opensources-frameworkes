package org.opensourceframework.demo.seata.integration.common.response;

import java.io.Serializable;

import lombok.Data;

/**
 * 基本返回
 *
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@Data
public class BaseResponse implements Serializable {

    private int status = 200;

    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
