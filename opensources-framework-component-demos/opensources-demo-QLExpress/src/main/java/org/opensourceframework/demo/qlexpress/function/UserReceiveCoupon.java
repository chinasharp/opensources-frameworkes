package org.opensourceframework.demo.qlexpress.function;

import org.opensourceframework.demo.qlexpress.vo.UserInfo;
import com.ql.util.express.ExpressRunner;

import java.util.Set;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
public class UserReceiveCoupon {
    private ExpressRunner runner = new ExpressRunner();

    /**
     * 判断一个用户标签是否符合
     * @param userInfo
     * @param userTag
     * @return
     */
    public boolean userTagAccordWith(UserInfo userInfo, Integer userTag){
        Set<Integer> userTags = userInfo.getUserTags();
        boolean isAccordWith = userTags.contains(userTag);
        return isAccordWith;
    }

    /**
     * 判断一个用户是否领取过某一类型的优惠劵
     *
     * @param userInfo
     * @param couponType
     * @return
     */
    public boolean hasReceiveCoupon(UserInfo userInfo, Integer couponType){
        //随机模拟一个
        return false;
    }
}
