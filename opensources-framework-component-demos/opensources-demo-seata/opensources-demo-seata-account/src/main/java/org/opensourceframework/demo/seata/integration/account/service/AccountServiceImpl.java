package org.opensourceframework.demo.seata.integration.account.service;

import org.opensourceframework.demo.seata.integration.account.dao.AccountDao;
import org.opensourceframework.demo.seata.integration.account.entity.Account;
import org.opensourceframework.demo.seata.integration.common.dto.AccountDTO;
import org.opensourceframework.demo.seata.integration.common.enums.RspStatusEnum;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.opensourceframework.starter.mybatis.base.service.impl.BizBaseServiceImpl;
import io.seata.spring.annotation.GlobalLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@Service
public class AccountServiceImpl extends BizBaseServiceImpl<Account, Long> implements IAccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public ObjectResponse decreaseAccount(AccountDTO accountDTO) {
        int account = accountDao.decreaseAccount(accountDTO.getUserId(), accountDTO.getAmount().doubleValue());
        ObjectResponse<Object> response = new ObjectResponse<>();
        if (account > 0){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }

    @Override
    @GlobalLock
    @Transactional(rollbackFor = {Throwable.class})
    public void testGlobalLock() {
        accountDao.testGlobalLock("1");
        System.out.println("Hi, i got lock, i will do some thing with holding this lock.");
    }

    /**
     * 获取操作的Dao Bean
     *
     * @return dao的操作bean
     */
    @Override
    public BizBaseDao<Account, Long> getBizDao() {
        return accountDao;
    }

    /**
     * 根据id列表查询数据列表
     *
     * @param idList 主键集合
     * @return 数据列表
     */
    @Override
    public List<Account> findByIds(List<Long> idList) {
        return null;
    }
}
