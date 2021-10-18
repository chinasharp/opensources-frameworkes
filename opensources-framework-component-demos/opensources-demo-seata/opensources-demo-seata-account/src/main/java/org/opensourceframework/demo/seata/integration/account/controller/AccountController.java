package org.opensourceframework.demo.seata.integration.account.controller;

import org.opensourceframework.demo.seata.integration.account.service.IAccountService;
import org.opensourceframework.demo.seata.integration.common.dto.AccountDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 账户扣钱
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private IAccountService accountService;

    @PostMapping("/dec_account")
    ObjectResponse decreaseAccount(@RequestBody AccountDTO accountDTO) {
        LOGGER.info("请求账户微服务：{}", accountDTO.toString());
        return accountService.decreaseAccount(accountDTO);
    }

    @GetMapping("/test_global_lock")
    void testGlobalLock() {
        LOGGER.info("testGlobalLock");
        accountService.testGlobalLock();
    }
}

