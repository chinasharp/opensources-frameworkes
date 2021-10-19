package org.opensourceframework.demo.grpc.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrpcClientController {

    @Autowired
    private GrpcClientService grpcClientService;

    @RequestMapping("/")
    public String printMessage(@RequestParam(defaultValue = "LinShen") String name) {
        return grpcClientService.sendMessage(name);
    }

    @RequestMapping("/helloWorld")
    public String printHelloWorldMessage(@RequestParam(defaultValue = "Chenwei") String name) {
        return grpcClientService.sendHelloWorldMessage(name);
    }
}
