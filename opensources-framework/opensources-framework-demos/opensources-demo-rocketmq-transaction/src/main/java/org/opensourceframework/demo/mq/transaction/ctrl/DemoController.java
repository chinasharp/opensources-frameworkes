package org.opensourceframework.demo.mq.transaction.ctrl;

import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.mq.transaction.dao.eo.DemoEo;
import org.opensourceframework.demo.mq.transaction.dto.request.user.DemoUserReqDto;
import org.opensourceframework.demo.mq.transaction.dto.response.user.DemoUserRespDto;
import org.opensourceframework.demo.mq.transaction.service.IDemoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Api实现以及RPC服务/RestFull服务注册示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RestController
@RequestMapping("/v1/op")
public class DemoController {
    @Resource
    private IDemoService demoService;

    /**
     * 使用MQ事务消息 来完成数据的保存 使用默认的Transactionlistenter
     *
     * @return
     */
    @PostMapping(value={"/save_by_def_mqtransaction"}, produces={"application/json"})
    public RestResponse<DemoUserRespDto> saveByDefMQTransaction() {
        DemoUserRespDto dto = new DemoUserRespDto();
        dto.setAccount("dubbo_test_" + System.currentTimeMillis());
        dto.setAddress("dubbo_test_" + System.currentTimeMillis());
        dto.setMemberCardNo("dubbo_test_" + System.currentTimeMillis());
        dto.setName("dubbo_test_" + System.currentTimeMillis());
        dto.setPhone("dubbo_test_" + System.currentTimeMillis());
        dto.setCreatePerson("dubbo_test_" + System.currentTimeMillis());

        DemoEo demoEo = new DemoEo();
        BeanHelper.copyProperties(demoEo , dto);
        demoService.saveByDefMQTransaction(demoEo);

        DemoUserRespDto demoUserRespDto = new DemoUserRespDto();
        BeanHelper.copyProperties(demoUserRespDto, demoEo);

        return RestResponse.success(demoUserRespDto);
    }

    private DemoUserReqDto buildReqDto(){
        DemoUserReqDto dto = new DemoUserReqDto();
        Long nowMillis = System.currentTimeMillis();
        String content = "dubbo_test_".concat(nowMillis.toString());
        dto.setAccount(content);
        dto.setAddress(content);
        dto.setMemberCardNo(content);
        dto.setName(content);
        dto.setPhone(content);
        dto.setCreatePerson(content);
        return dto;
    }

    /**
     * 使用MQ事务消息 来完成数据的保存 使用自定义的Transactionlistenter
     *
     * @return
     */
    @PostMapping(value={"/save_by_custom_mqtransaction"}, produces={"application/json"})
    public RestResponse<DemoUserRespDto> saveByCustomMQTransaction() {
        DemoUserReqDto dto = buildReqDto();

        DemoEo demoEo = new DemoEo();
        BeanHelper.copyProperties(demoEo , dto);
        demoService.saveByCustomMQTransaction(demoEo);

        DemoUserRespDto demoUserRespDto = new DemoUserRespDto();
        BeanHelper.copyProperties(demoUserRespDto, demoEo);

        return RestResponse.success(demoUserRespDto);
    }
}
