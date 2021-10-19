package org.opensourceframework.demo.grpc.provider.service;

import org.opensourceframework.demo.grpc.lib.HelloWorldGrpc;
import org.opensourceframework.demo.grpc.lib.Helloworld;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * GRPC HelloWorld服务
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@GrpcService
@Slf4j
public class HelloWorldService extends HelloWorldGrpc.HelloWorldImplBase {
    @Override
    public void sayHello(Helloworld.HelloWorldRequest request,
        StreamObserver<Helloworld.HelloWorldReply> responseObserver) {

        String message = "Hi " + request.getName();
        final Helloworld.HelloWorldReply.Builder replyBuilder = Helloworld.HelloWorldReply.newBuilder().setMessage(message);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
        log.info("返回消息 " + message);
    }
}
