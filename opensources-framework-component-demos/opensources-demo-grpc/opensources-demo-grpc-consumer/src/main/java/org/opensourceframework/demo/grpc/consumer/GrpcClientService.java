package org.opensourceframework.demo.grpc.consumer;

import org.springframework.stereotype.Service;

import org.opensourceframework.demo.grpc.lib.GreeterGrpc;
import org.opensourceframework.demo.grpc.lib.GreeterOuterClass;
import org.opensourceframework.demo.grpc.lib.HelloWorldGrpc;
import org.opensourceframework.demo.grpc.lib.Helloworld;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class GrpcClientService {

    @GrpcClient("opensources-user-grpc-provider")
    private GreeterGrpc.GreeterBlockingStub stub;

    @GrpcClient("opensources-user-grpc-provider")
    private HelloWorldGrpc.HelloWorldBlockingStub helloWorldBlockingStub;

    public String sendMessage(String name) {
        GreeterOuterClass.HelloReply response = stub.sayHello(GreeterOuterClass.HelloRequest.newBuilder()
            .setName(name)
            .build());
        return response.getMessage();
    }

    public String sendHelloWorldMessage(String name) {
        Helloworld.HelloWorldReply response = helloWorldBlockingStub.sayHello(Helloworld.HelloWorldRequest.newBuilder()
            .setName(name)
            .build());
        return response.getMessage();
    }
}
