package org.opensourceframework.component.httpclient.vo;

/**
 * http响应vo
 *
 * @author maihaixian
 *
 * @since 1.0.0
 */
public class HttpResponse {
    /**
     * 状态码
     */
    private int statusCode;
    /**
     * 消息体
     */
    private String body;

    public HttpResponse() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", body='" + body + '\'' +
                '}';
    }
}

