package org.opensourceframework.demo.seata.integration.account.service;

import org.opensourceframework.demo.seata.integration.account.entity.Account;
import org.opensourceframework.demo.seata.integration.common.dto.AccountDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import org.opensourceframework.starter.mybatis.base.service.IBizBaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface IAccountService extends IBizBaseService<Account, Long> {

    /**
     * 扣用户钱
     */
    ObjectResponse decreaseAccount(AccountDTO accountDTO);

    void testGlobalLock();
}
