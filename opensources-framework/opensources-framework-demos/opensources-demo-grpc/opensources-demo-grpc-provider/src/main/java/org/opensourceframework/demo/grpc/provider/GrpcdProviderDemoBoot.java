package org.opensourceframework.demo.grpc.provider;

import org.opensourceframework.demo.grpc.provider.interceptor.GrpcDemoInterceptor;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GrpcdProviderDemoBoot {

    public static void main(String[] args) {
        SpringApplication.run(GrpcdProviderDemoBoot.class, args);
    }

    @GrpcGlobalServerInterceptor
    ServerInterceptor logServerInterceptor() {
        return new GrpcDemoInterceptor();
    }

}
