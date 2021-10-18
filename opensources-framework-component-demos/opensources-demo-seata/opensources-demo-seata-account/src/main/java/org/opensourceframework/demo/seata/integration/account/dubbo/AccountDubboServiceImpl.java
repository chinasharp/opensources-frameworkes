package org.opensourceframework.demo.seata.integration.account.dubbo;

import org.opensourceframework.demo.seata.integration.account.service.IAccountService;
import org.opensourceframework.demo.seata.integration.common.dto.AccountDTO;
import org.opensourceframework.demo.seata.integration.common.dubbo.AccountDubboService;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import io.seata.core.context.RootContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dubbo Api Impl
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@DubboService(version = "1.0.0",protocol = "${dubbo.protocol.id}",group = "${dubbo.application.id}")
public class AccountDubboServiceImpl implements AccountDubboService {

    @Autowired
    private IAccountService accountService;

    @Override
    public ObjectResponse decreaseAccount(AccountDTO accountDTO) {
        System.out.println("全局事务id ：" + RootContext.getXID());
        return accountService.decreaseAccount(accountDTO);
    }
}
