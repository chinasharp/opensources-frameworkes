package org.opensourceframework.demo.seata.integration.account.dao;

import org.opensourceframework.demo.seata.integration.account.entity.Account;
import org.opensourceframework.demo.seata.integration.account.mapper.AccountMapper;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Repository
public class AccountDao extends BizBaseDao<Account, Long> {
	private static Logger logger = LoggerFactory.getLogger(AccountDao.class);
	@Resource
	private AccountMapper accountMapper;

	public Integer decreaseAccount(String userId, Double amount){
		return accountMapper.decreaseAccount(userId , amount);
	}

	public Integer testGlobalLock(String userId){
		return accountMapper.testGlobalLock(userId);
	}
}
