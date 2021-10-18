package org.opensourceframework.base.app;

import java.io.Serializable;

/**
 * 各应用接入参数对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class AppInfo implements Serializable {
    private String appId;
    private String appSecret;

    public AppInfo() {
    }

    public AppInfo(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String toString() {
        return "appId:" + this.appId + ", appSecret:" + this.appSecret;
    }
}

