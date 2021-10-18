package org.opensourceframework.demo.seata.integration.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "org.opensourceframework.user.seata.integration.account")
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan({"org.opensourceframework.user.seata.integration.account.org.opensourceframework.user.bloomfilter.mybatis.mapper"})
public class AccountExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountExampleApplication.class, args);
    }

}

