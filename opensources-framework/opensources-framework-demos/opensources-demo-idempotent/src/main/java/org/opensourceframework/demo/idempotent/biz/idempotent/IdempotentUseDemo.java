package org.opensourceframework.demo.idempotent.biz.idempotent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.base.helper.DateHelper;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.component.idempotent.annotation.IdempotentHandler;
import org.opensourceframework.demo.idempotent.biz.dao.eo.DemoUserEo;
import org.opensourceframework.demo.idempotent.biz.mq.sender.DemoUserSender;
import org.opensourceframework.demo.idempotent.biz.service.IDemoUserService;
import org.opensourceframework.demo.idempotent.dto.request.user.DemoUserReqDto;
import org.opensourceframework.demo.idempotent.dto.response.user.DemoUserRespDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 *
 */
@Api(tags = "幂等组件对MQ处理、业务处理 Demo 消息重复消费处理见IdempotentMQConsumer ")
@RestController
@RequestMapping("/v1/demo/idempotent")
public class IdempotentUseDemo {
    private static Logger logger = LoggerFactory.getLogger(IdempotentUseDemo.class);

    @Autowired
    private DemoUserSender demoUserSender;

    @Autowired
    private IDemoUserService demoUserService;

    @PostMapping("/mq/send")
    @IdempotentHandler(parameter = "demoUserReqDto" , properties = "account,password" , validTime = 60)
    @ApiOperation(value="幂等MQ消息的重复发送,在validTime值的时间内 返回的结果数据一致 0为永远返回一致的结果", notes="幂等MQ消息的重复发送 validTime:幂等数据有效期,单位秒,0表示永久有效" )
    public MessageResponse sendMQMessage(@RequestBody DemoUserReqDto demoUserReqDto){
        return demoUserSender.sendMessage(demoUserReqDto);
    }

    @PostMapping("/biz/save")
    @IdempotentHandler(parameter = "demoUserReqDto" , properties = "account,password" , validTime = 60)
    @ApiOperation(value="幂等重复保存,在validTime值的时间内 返回的结果数据一致 0为永远返回一致的结果 , properties与参数对象的那些属性来确定是否为重复操作", notes="幂等重复保存 validTime:幂等数据有效期,单位秒,0表示永久有效" )
    public RestResponse<Long> save(@RequestBody DemoUserReqDto demoUserReqDto){
        DemoUserEo eo = new DemoUserEo();
        BeanHelper.copyProperties(eo , demoUserReqDto);
        demoUserService.insert(eo);
        return RestResponse.success(eo.getId());
    }

    @PostMapping("/biz/update")
    @IdempotentHandler(validTime = 60)
    @ApiOperation(value="幂等重复更新,在validTime值的时间内 返回的结果数据一致 0为永远返回一致的结果,没有参数", notes="幂等重复更新 validTime:幂等数据有效期,单位秒,0表示永久有效" )
    public RestResponse<DemoUserRespDto> update(){
        Long demoUserId = 304648474241064960L;
        DemoUserEo eo = new DemoUserEo();
        eo.setId(demoUserId);
        eo.setAccount("IdempotentUpdate_".concat(DateHelper.YYYYMMDDHHMMSS(new Date())));
        demoUserService.updateNotNull(eo);

        eo = demoUserService.findById(demoUserId);
        DemoUserRespDto respDto = new DemoUserRespDto();
        BeanHelper.copyProperties(respDto , eo);
        return RestResponse.success(respDto);
    }

    @PostMapping("/biz/bizOp")
    @IdempotentHandler(validTime = 60)
    @ApiOperation(value="幂等重复操作,在validTime值的时间内 返回的结果数据一致 0为永远返回一致的结果", notes="幂等重复操作 validTime:幂等数据有效期,单位秒,0表示永久有效" )
    public RestResponse<Long> bizOp(){
        Long timeMillis = System.currentTimeMillis();
        return RestResponse.success(timeMillis);
    }
}
