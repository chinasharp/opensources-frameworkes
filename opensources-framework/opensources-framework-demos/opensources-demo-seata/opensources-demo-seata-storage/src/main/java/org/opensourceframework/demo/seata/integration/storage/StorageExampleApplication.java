package org.opensourceframework.demo.seata.integration.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "org.opensourceframework.user.seata.integration.storage")
@EnableDiscoveryClient
@MapperScan({"org.opensourceframework.user.seata.integration.storage.org.opensourceframework.user.bloomfilter.mybatis.mapper"})
public class StorageExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageExampleApplication.class, args);
    }

}

