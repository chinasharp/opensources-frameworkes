package org.opensourceframework.demo.seata.integration.common.response;

import java.io.Serializable;

/**
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
public class ObjectResponse<T> extends BaseResponse implements Serializable {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
