package org.opensourceframework.demo.cache.entity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
public class UserFactory {
    private static AtomicInteger age = new AtomicInteger(20);

    public static UserInfo buildUserInfo(Long id){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setAddress("address_".concat(id.toString()));
        userInfo.setAccount("account_".concat(id.toString()));
        userInfo.setPassword("password_".concat(id.toString()));
        userInfo.setAge(age.incrementAndGet());
        return userInfo;
    }
}
