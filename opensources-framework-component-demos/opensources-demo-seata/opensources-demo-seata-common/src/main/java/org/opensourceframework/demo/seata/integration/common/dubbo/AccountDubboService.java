package org.opensourceframework.demo.seata.integration.common.dubbo;

import org.opensourceframework.demo.seata.integration.common.dto.AccountDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;

/**
 * 账户服务接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
public interface AccountDubboService {

    /**
     * 从账户扣钱
     *
     * @param accountDTO
     */
    ObjectResponse decreaseAccount(AccountDTO accountDTO);
}
