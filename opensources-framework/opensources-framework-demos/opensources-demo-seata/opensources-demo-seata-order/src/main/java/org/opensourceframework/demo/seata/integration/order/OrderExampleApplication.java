package org.opensourceframework.demo.seata.integration.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "org.opensourceframework.user.seata.integration.order")
@EnableDiscoveryClient
@MapperScan({"org.opensourceframework.user.seata.integration.order.org.opensourceframework.user.bloomfilter.mybatis.mapper"})
public class OrderExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderExampleApplication.class, args);
    }

}

