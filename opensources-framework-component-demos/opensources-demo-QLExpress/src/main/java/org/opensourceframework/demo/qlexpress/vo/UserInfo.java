package org.opensourceframework.demo.qlexpress.vo;

import java.util.Set;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
public class UserInfo {
    /**
     * 用户id
     */
    Long userId;

    /**
     * 用户标签列表
     */
    Set<Integer> userTags;

    /**
     * 用户姓名
     */
    String userName;

    public UserInfo() {
    }

    public UserInfo(Long userId, Set<Integer> userTags, String userName) {
        this.userId = userId;
        this.userTags = userTags;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Integer> getUserTags() {
        return userTags;
    }

    public void setUserTags(Set<Integer> userTags) {
        this.userTags = userTags;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
