package org.opensourceframework.starter.mixcache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Mix Cache 配置信息
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@RefreshScope
@ConfigurationProperties
public class MixCacheConfig {
    @Value("${opensourceframework.mix.cache.local.flag:true}")
    private Boolean localCacheFlag;

    @Value("${opensourceframework.mix.cache.remote.flag:true}")
    private Boolean remoteCacheFlag;

    @Value("${opensourceframework.mix.cache.enable.async:false}")
    private Boolean cacheEnableAsync;

    public MixCacheConfig() {
    }

    public Boolean getLocalCacheFlag() {
        return localCacheFlag;
    }

    public void setLocalCacheFlag(Boolean localCacheFlag) {
        this.localCacheFlag = localCacheFlag;
    }

    public Boolean getRemoteCacheFlag() {
        return remoteCacheFlag;
    }

    public void setRemoteCacheFlag(Boolean remoteCacheFlag) {
        this.remoteCacheFlag = remoteCacheFlag;
    }

    public Boolean getCacheEnableAsync() {
        return cacheEnableAsync;
    }

    public void setCacheEnableAsync(Boolean cacheEnableAsync) {
        this.cacheEnableAsync = cacheEnableAsync;
    }
}
