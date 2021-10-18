package org.opensourceframework.starter.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.io.Serializable;

/**
 * 阿里云oss链接配置类
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@RefreshScope
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig implements Serializable {
    private String bucketName;

    private String endPoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String accessUrlPrefix;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getAccessUrlPrefix() {
        return accessUrlPrefix;
    }

    public void setAccessUrlPrefix(String accessUrlPrefix) {
        this.accessUrlPrefix = accessUrlPrefix;
    }
}
