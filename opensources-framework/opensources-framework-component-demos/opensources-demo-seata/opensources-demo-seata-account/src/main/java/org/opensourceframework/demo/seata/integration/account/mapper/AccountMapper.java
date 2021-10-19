package org.opensourceframework.demo.seata.integration.account.mapper;

import org.opensourceframework.component.dao.base.BaseMapper;
import org.opensourceframework.demo.seata.integration.account.entity.Account;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface AccountMapper extends BaseMapper<Account, Long> {

    /**
     *
     * @param userId
     * @param amount
     * @return
     */
    Integer decreaseAccount(@Param("userId") String userId, @Param("amount") Double amount);

    /**
     *
     * @param userId
     * @return
     */
    Integer testGlobalLock(@Param("userId") String userId);
}
