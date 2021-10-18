package org.opensourceframework.component.seclog.helper;

/**
 * 用户ThreadLocal
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
public class UserHelper {

    private static final ThreadLocal<String> CURRENT_USER_NAME = new ThreadLocal<>();

    /**
     * 设置当前用户名称
     * @param userName
     */
    public static void setCurrentUserName(String userName) {
        CURRENT_USER_NAME.set(userName);
    }

    /**
     * 获取当前用户名称
     * @return
     */
    public static String getCurrentUserName() {
        return CURRENT_USER_NAME.get();
    }

    /**
     * 删除当前用户名称
     */
    public static void removeCurrentUserName() {
        CURRENT_USER_NAME.remove();
    }


}
