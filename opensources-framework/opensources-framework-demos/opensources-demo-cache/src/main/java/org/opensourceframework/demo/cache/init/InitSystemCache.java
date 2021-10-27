package org.opensourceframework.demo.cache.init;

import org.opensourceframework.demo.cache.cache.local.UserLocalCache;
import org.opensourceframework.demo.cache.cache.mix.UserMixCache;
import org.opensourceframework.demo.cache.cache.remote.UserRemoteCache;
import org.opensourceframework.demo.cache.entity.UserFactory;
import org.opensourceframework.demo.cache.entity.UserInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
@Component
public class InitSystemCache implements InitializingBean {
    @Resource
    private UserLocalCache userLocalCache;

    @Resource
    private UserRemoteCache userRemoteCache;

    @Resource
    private UserMixCache userMixCache;

    @Override
    public void afterPropertiesSet() throws Exception {
        UserInfo localUserInfo = UserFactory.buildUserInfo(1L);
        userLocalCache.setCache(localUserInfo);

        UserInfo remoteUserInfo = UserFactory.buildUserInfo(2L);
        userRemoteCache.setCache(remoteUserInfo);

        UserInfo mixUserInfo = UserFactory.buildUserInfo(3L);
        userMixCache.setCache(mixUserInfo);


    }
}
