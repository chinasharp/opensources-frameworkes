package org.opensourceframework.demo.grpc.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GrpcConsumerDemoBoot {
    public static void main(String[] args) {
        SpringApplication.run(GrpcConsumerDemoBoot.class, args);
    }
}
